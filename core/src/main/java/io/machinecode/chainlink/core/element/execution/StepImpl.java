package io.machinecode.chainlink.core.element.execution;

import io.machinecode.chainlink.core.Constants;
import io.machinecode.chainlink.core.context.ExecutionContextImpl;
import io.machinecode.chainlink.core.context.StepContextImpl;
import io.machinecode.chainlink.core.element.ListenerImpl;
import io.machinecode.chainlink.core.element.ListenersImpl;
import io.machinecode.chainlink.core.element.PropertiesImpl;
import io.machinecode.chainlink.core.element.PropertyImpl;
import io.machinecode.chainlink.core.element.partition.PartitionImpl;
import io.machinecode.chainlink.core.element.transition.TransitionImpl;
import io.machinecode.chainlink.core.util.PropertiesConverter;
import io.machinecode.chainlink.core.util.Repository;
import io.machinecode.chainlink.core.work.TaskExecutable;
import io.machinecode.chainlink.spi.configuration.RuntimeConfiguration;
import io.machinecode.chainlink.spi.repository.ExecutionRepository;
import io.machinecode.chainlink.spi.repository.ExtendedStepExecution;
import io.machinecode.chainlink.spi.context.ExecutionContext;
import io.machinecode.chainlink.spi.context.MutableJobContext;
import io.machinecode.chainlink.spi.context.MutableStepContext;
import io.machinecode.chainlink.spi.transport.ExecutableId;
import io.machinecode.chainlink.spi.transport.ExecutionRepositoryId;
import io.machinecode.chainlink.spi.transport.WorkerId;
import io.machinecode.chainlink.spi.element.execution.Step;
import io.machinecode.chainlink.spi.execution.Executable;
import io.machinecode.chainlink.spi.execution.Executor;
import io.machinecode.chainlink.spi.util.Messages;
import io.machinecode.chainlink.spi.deferred.Deferred;
import io.machinecode.chainlink.spi.work.PartitionTarget;
import io.machinecode.chainlink.spi.work.StrategyWork;
import io.machinecode.chainlink.spi.work.TaskWork;
import io.machinecode.chainlink.spi.work.TransitionWork;
import org.jboss.logging.Logger;

import javax.batch.api.listener.StepListener;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.StepExecution;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static javax.batch.api.partition.PartitionReducer.PartitionStatus;
import static javax.batch.api.partition.PartitionReducer.PartitionStatus.COMMIT;
import static javax.batch.api.partition.PartitionReducer.PartitionStatus.ROLLBACK;
import static javax.batch.runtime.BatchStatus.COMPLETED;
import static javax.batch.runtime.BatchStatus.FAILED;
import static javax.batch.runtime.BatchStatus.STARTED;
import static javax.batch.runtime.BatchStatus.STARTING;
import static javax.batch.runtime.BatchStatus.STOPPED;
import static javax.batch.runtime.BatchStatus.STOPPING;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class StepImpl<T extends TaskWork, U extends StrategyWork> extends ExecutionImpl implements Step<T, U> {

    private static final Logger log = Logger.getLogger(StepImpl.class);

    private final String next;
    private final String startLimit;
    private final String allowStartIfComplete;
    private final PropertiesImpl properties;
    private final ListenersImpl listeners;
    private final List<TransitionImpl> transitions;
    private final T task;
    private final PartitionImpl<U> partition;

    private transient Integer _timeout;
    private transient List<ListenerImpl> _listeners;
    private transient int _partitions;
    private transient int _completed;
    private transient PartitionStatus _partitionStatus;
    private transient TransactionManager _transactionManager;
    private transient Transaction _transaction;

    public StepImpl(
            final String id,
            final String next,
            final String startLimit,
            final String allowStartIfComplete,
            final PropertiesImpl properties,
            final ListenersImpl listeners,
            final List<TransitionImpl> transitions,
            final T task,
            final PartitionImpl<U> partition
    ) {
        super(id);
        this.next = next;
        this.startLimit = startLimit;
        this.allowStartIfComplete = allowStartIfComplete;
        this.listeners = listeners;
        this.properties = properties;
        this.transitions = transitions;
        this.task = task;
        this.partition = partition;
    }

    @Override
    public String getNext() {
        return this.next;
    }

    @Override
    public String getStartLimit() {
        return this.startLimit;
    }

    @Override
    public String getAllowStartIfComplete() {
        return this.allowStartIfComplete;
    }

    @Override
    public PropertiesImpl getProperties() {
        return this.properties;
    }

    @Override
    public ListenersImpl getListeners() {
        return this.listeners;
    }

    @Override
    public List<TransitionImpl> getTransitions() {
        return this.transitions;
    }

    @Override
    public T getTask() {
        return this.task;
    }

    @Override
    public PartitionImpl<U> getPartition() {
        return this.partition;
    }

    private int _timeout(final ExecutionContext context) {
        if (_timeout != null) {
            return this._timeout;
        }
        this._timeout = 180;
        for (final PropertyImpl property : this.properties.getProperties()) {
            try {
                if (Constants.JAVAX_TRANSACTION_GLOBAL_TIMEOUT.equals(property.getName())) {
                    this._timeout = Integer.parseInt(property.getValue());
                    return this._timeout;
                }
            } catch (final NumberFormatException e) {
                throw new IllegalStateException(Messages.format("CHAINLINK-010002.step.transaction.timeout.not.integer", context, this.id, property.getValue()));
            }
        }
        return this._timeout;
    }

    // Lifecycle

    private boolean isPartitioned() {
        //TODO This looks like a bug in the xsl
        return this.partition != null && this.partition.getStrategy() != null;
    }

    private List<ListenerImpl> _listeners(final RuntimeConfiguration configuration, final ExecutionContext context) throws Exception {
        if (this._listeners == null) {
            this._listeners = this.listeners.getListenersImplementing(configuration, context, StepListener.class);
        }
        return _listeners;
    }

    @Override
    public Deferred<?> before(final RuntimeConfiguration configuration, final ExecutionRepositoryId executionRepositoryId,
                              final WorkerId workerId, final ExecutableId callbackId, final ExecutableId parentId,
                              final ExecutionContext context) throws Exception {
        log.debugf(Messages.get("CHAINLINK-010100.step.before"), context, this.id);
        final ExecutionRepository repository = configuration.getExecutionRepository(executionRepositoryId);
        final MutableJobContext jobContext = context.getJobContext();
        final JobExecution jobExecution = repository.getJobExecution(jobContext.getExecutionId());
        StepExecution stepExecution;
        Serializable persistentData = null;
        Long restartStepExecutionId = null;
        if (context.isRestarting()) {
            final long restartJobExecutionId = context.getRestartJobExecutionId();
            try {
                final ExtendedStepExecution restartStepExecution = repository.getLatestStepExecution(restartJobExecutionId, this.id);
                restartStepExecutionId = restartStepExecution.getStepExecutionId();
                log.debugf(Messages.get("CHAINLINK-010206.step.found.existing.execution"), context, this.id, restartStepExecutionId, restartJobExecutionId);
                persistentData = restartStepExecution.getPersistentUserData();
                final BatchStatus batchStatus = restartStepExecution.getBatchStatus();
                final String exitStatus = restartStepExecution.getExitStatus();
                switch (batchStatus) {
                    case COMPLETED:
                        if (!Boolean.parseBoolean(this.getAllowStartIfComplete())) {
                            context.setLastStepExecutionId(restartStepExecutionId);
                            final TransitionWork transition = this.transition(context, this.transitions, batchStatus, exitStatus);
                            if (transition != null && transition.isTerminating()) {
                                return runCallback(configuration, context, parentId);
                            } else {
                                return this.next(configuration, workerId, context, parentId, executionRepositoryId, this.next, transition);
                            }
                        }
                        break;
                    case FAILED:
                    case STOPPED:
                        break;
                    default:
                        throw new IllegalStateException(Messages.format("CHAINLINK-010215.step.invalid.restart.batch.status", context, batchStatus));
                }
            } catch (final NoSuchJobExecutionException e) {
                log.debugf(Messages.get("CHAINLINK-010207.step.no.existing.execution"), context, this.id, restartJobExecutionId);
            }
            final int startLimit = Integer.parseInt(this.getStartLimit());
            final int startCount = repository.getStepExecutionCount(jobContext.getExecutionId(), this.id);
            if (startLimit != 0 && startCount >= startLimit) {
                log.infof(Messages.get("CHAINLINK-010213.step.start.limit.exceeded"), context, this.id, startCount, startLimit);
                jobContext.setBatchStatus(FAILED);
                return runCallback(configuration, context, parentId);
            } else {
                log.debugf(Messages.get("CHAINLINK-010214.step.start.limit"), context, this.id, startCount, startLimit);
            }
        }
        stepExecution = repository.createStepExecution(jobExecution, this.id, new Date());

        final long stepExecutionId = stepExecution.getStepExecutionId();
        final MutableStepContext stepContext = new StepContextImpl(stepExecutionId, this, PropertiesConverter.convert(this.properties));
        context.setStepContext(stepContext);
        log.debugf(Messages.get("CHAINLINK-010203.step.create.step.context"), context);
        stepContext.setPersistentUserData(persistentData);
        if (stepExecution.getBatchStatus() != STARTING) {
            throw new IllegalStateException(Messages.format("CHAINLINK-010001.step.not.starting", context, stepExecution.getBatchStatus()));
        }
        Exception exception = null;
        for (final ListenerImpl listener : this._listeners(configuration, context)) {
            try {
                log.debugf(Messages.get("CHAINLINK-010200.step.listener.before.step"), context);
                listener.beforeStep(configuration, context);
            } catch (final Exception e) {
                if (exception == null) {
                    exception = e;
                } else {
                    exception.addSuppressed(e);
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
        context.setLastStepExecutionId(stepExecutionId);
        int timeout = _timeout(context);

        repository.startStepExecution(stepExecutionId, new Date());
        stepContext.setBatchStatus(STARTED);

        if (!isPartitioned()) {
            this._partitions = 1;
            this._completed = 0;
            final ExecutionContext clonedContext = new ExecutionContextImpl(
                    context.getJob(),
                    context.getJobContext(),
                    context.getStepContext(),
                    context.getJobExecutionId(),
                    context.getRestartJobExecutionId(),
                    context.getRestartElementId(),
                    null
            );
            return configuration.getExecutor().execute(
                    new TaskExecutable(callbackId, this.task, clonedContext, executionRepositoryId, timeout)
            );
        } else {
            final PartitionTarget target = this.partition.map(configuration, executionRepositoryId, this.task, callbackId, context, timeout, restartStepExecutionId);
            this._partitions = target.executables.length;
            this._completed = 0;
            return configuration.getExecutor().distribute(
                    target.threads,
                    target.executables
            );
        }
    }

    @Override
    public Deferred<?> after(final RuntimeConfiguration configuration, final ExecutionRepositoryId executionRepositoryId,
                             final WorkerId workerId, final ExecutableId parentId, final ExecutionContext context,
                             final ExecutionContext childContext) throws Exception {
        log.debugf(Messages.get("CHAINLINK-010101.step.after"), context, childContext);
        final long jobExecutionId = context.getJobExecutionId();
        final long stepExecutionId = context.getStepExecutionId();
        final ExecutionRepository repository = configuration.getExecutionRepository(executionRepositoryId);
        final MutableStepContext stepContext = context.getStepContext();
        ++this._completed;
        try {
            if (this.isPartitioned()) {
                if (this._completed == 1) {
                    this._transactionManager = configuration.getTransactionManager();
                    this._partitionStatus = COMMIT;
                    int timeout = _timeout(context);
                    log.debugf(Messages.get("CHAINLINK-010208.step.set.transaction.timeout"), childContext, timeout);
                    this._transactionManager.setTransactionTimeout(timeout);
                    log.debugf(Messages.get("CHAINLINK-010210.step.begin.transaction"), childContext);
                    this._transactionManager.begin();
                } else if (this._partitionStatus == ROLLBACK) {
                    return null;
                } else {
                    log.debugf(Messages.get("CHAINLINK-010212.step.resume.transaction"), childContext, this._transaction);
                    this._transactionManager.resume(this._transaction);
                }
                try {
                    switch (childContext.getStepContext().getBatchStatus()) {
                        case FAILED:
                            stepContext.setBatchStatus(FAILED);
                        case STOPPING:
                            if (stepContext.getBatchStatus() != FAILED) {
                                stepContext.setBatchStatus(STOPPING);
                            }
                    }
                    final PartitionStatus partitionStatus = this.partition.analyse(configuration, context, childContext.getItems());
                    this._partitionStatus = this._partitionStatus == ROLLBACK
                            ? ROLLBACK
                            : partitionStatus;
                } catch (final Exception e) {
                    log.infof(e, Messages.get("CHAINLINK-010209.step.analyse.exception"), childContext);
                    this._partitionStatus = ROLLBACK;
                }
                if (this._completed < this._partitions) {
                    this._transaction = this._transactionManager.suspend();
                    log.debugf(Messages.get("CHAINLINK-010211.step.suspend.transaction"), childContext, this._transaction);
                    return null;
                }
                this.partition.reduce(configuration, stepContext.getBatchStatus() == FAILED ? ROLLBACK : this._partitionStatus, context);
            }
        } catch (final Throwable e) {
            log.debugf(e, Messages.get("CHAINLINK-010205.step.after.caught.exception"), context);
            this._partitionStatus = ROLLBACK;
            context.getJobContext().setBatchStatus(FAILED);
        }
        try {
            Exception exception = null;
            for (final ListenerImpl listener : this._listeners(configuration, context)) {
                try {
                    log.debugf(Messages.get("CHAINLINK-010201.step.listener.after.step"), context);
                    listener.afterStep(configuration, context);
                } catch (final Exception e) {
                    if (exception == null) {
                        exception = e;
                    } else {
                        exception.addSuppressed(e);
                    }
                }
            }
            try {
                log.debugf(Messages.get("CHAINLINK-010202.step.update.persistent.data"), context);
                Repository.updateStep(
                        repository,
                        jobExecutionId,
                        stepExecutionId,
                        stepContext.getMetrics(),
                        stepContext.getPersistentUserData()
                );
            } catch (final Exception e) {
                if (exception == null) {
                    exception = e;
                } else {
                    exception.addSuppressed(e);
                }
            }
            if (exception != null) {
                stepContext.setException(exception);
                stepContext.setBatchStatus(FAILED);
                throw exception;
            }
            final BatchStatus batchStatus;
            switch (stepContext.getBatchStatus()) {
                case STARTED:
                    batchStatus = COMPLETED;
                    break;
                case STOPPING:
                    batchStatus = STOPPED;
                    break;
                case FAILED:
                    batchStatus = FAILED;
                    break;
                default:
                    throw new IllegalStateException(Messages.format("CHAINLINK-010000.step.illegal.batch.status", context, stepContext.getBatchStatus()));
            }
            context.getJobContext().setBatchStatus(stepContext.getBatchStatus());
            final String exitStatus = stepContext.getExitStatus();
            final TransitionWork transition = this.transition(context, this.transitions, batchStatus, exitStatus);
            Repository.finishStep(
                    repository,
                    jobExecutionId,
                    stepExecutionId,
                    stepContext.getMetrics(),
                    batchStatus,
                    exitStatus
            );
            log.debugf(Messages.get("CHAINLINK-010204.step.destroy.step.context"), context);
            context.setStepContext(null);
            if (transition != null && transition.isTerminating()) {
                return runCallback(configuration, context, parentId);
            } else {
                return this.next(configuration, workerId, context, parentId, executionRepositoryId, this.next, transition);
            }
        } catch (final Throwable e) {
            log.debugf(e, Messages.get("CHAINLINK-010205.step.after.caught.exception"), context);
            context.getJobContext().setBatchStatus(FAILED);
            Repository.finishStep(
                    repository,
                    jobExecutionId,
                    stepExecutionId,
                    stepContext.getMetrics(),
                    FAILED,
                    stepContext.getExitStatus()
            );
            log.debugf(Messages.get("CHAINLINK-010204.step.destroy.step.context"), context);
            context.setStepContext(null);
            return runCallback(configuration, context, parentId);
        }
    }

    @Override
    protected Logger log() {
        return log;
    }
}