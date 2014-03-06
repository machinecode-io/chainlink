package io.machinecode.chainlink.core.element;

import io.machinecode.chainlink.core.element.execution.ExecutionImpl;
import io.machinecode.chainlink.core.work.ExecutionExecutable;
import io.machinecode.chainlink.core.util.Repository;
import io.machinecode.chainlink.core.util.Statuses;
import io.machinecode.chainlink.core.validation.InvalidJobException;
import io.machinecode.chainlink.core.validation.JobValidator;
import io.machinecode.chainlink.core.validation.JobTraversal;
import io.machinecode.chainlink.spi.repository.ExecutionRepository;
import io.machinecode.chainlink.spi.repository.ExtendedJobExecution;
import io.machinecode.chainlink.spi.context.ExecutionContext;
import io.machinecode.chainlink.spi.context.ThreadId;
import io.machinecode.chainlink.spi.deferred.Deferred;
import io.machinecode.chainlink.spi.element.Job;
import io.machinecode.chainlink.spi.execution.Executable;
import io.machinecode.chainlink.spi.execution.Executor;
import io.machinecode.chainlink.spi.util.Messages;
import io.machinecode.chainlink.spi.work.ExecutionWork;
import io.machinecode.chainlink.spi.work.JobWork;
import org.jboss.logging.Logger;

import javax.batch.api.listener.JobListener;
import javax.batch.runtime.BatchStatus;
import java.util.List;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class JobImpl implements Job, JobWork {

    private static final Logger log = Logger.getLogger(JobImpl.class);

    private final String id;
    private final String version;
    private final String restartable;
    private final PropertiesImpl properties;
    private final ListenersImpl listeners;
    private final List<ExecutionImpl> executions;
    private final JobTraversal traversal;

    public JobImpl(final String id, final String version, final String restartable, final PropertiesImpl properties,
                   final ListenersImpl listeners, final List<ExecutionImpl> executions) throws InvalidJobException {
        this.id = id;
        this.version = version;
        this.restartable = restartable;
        this.properties = properties;
        this.listeners = listeners;
        this.executions = executions;
        this.traversal = new JobTraversal(id, JobValidator.INSTANCE.visit(this));
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getRestartable() {
        return this.restartable;
    }

    @Override
    public boolean isRestartable() {
        return Boolean.parseBoolean(this.restartable);
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
    public List<ExecutionImpl> getExecutions() {
        return this.executions;
    }

    // Lifecycle

    private transient List<ListenerImpl> _listeners;

    private List<ListenerImpl> _listeners(final Executor executor, final ExecutionContext context) throws Exception {
        if (this._listeners == null) {
            this._listeners = this.listeners.getListenersImplementing(executor, context, JobListener.class);
        }
        return _listeners;
    }

    @Override
    public Deferred<?> before(final Executor executor, final ThreadId threadId, final Executable callback,
                              final ExecutionContext context) throws Exception {
        final ExecutionRepository repository = executor.getRepository();
        long jobExecutionId = context.getJobExecutionId();
        Repository.startedJob(repository, jobExecutionId);
        log.debugf(Messages.get("CHAINLINK-018000.job.create.job.context"), context);
        Exception exception = null;
        for (final ListenerImpl listener : this._listeners(executor, context)) {
            try {
                log.debugf(Messages.get("CHAINLINK-018001.job.listener.before.job"), context);
                listener.beforeJob(executor, context);
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

        final BatchStatus batchStatus = context.getJobContext().getBatchStatus();
        if (Statuses.isStopping(batchStatus)) {
            log.debugf(Messages.get("CHAINLINK-018002.job.status.early.termination"), context, batchStatus);
            return executor.callback(callback, context);
        }

        final ExtendedJobExecution restartJobExecution = context.getRestartJobExecution();
        if (restartJobExecution == null) {
            return _runNext(executor, callback, context, this.executions.get(0));
        }
        repository.linkJobExecutions(jobExecutionId, restartJobExecution);
        final String restartId = restartJobExecution.getRestartElementId();
        if (restartId == null) {
            return _runNext(executor, callback, context, this.executions.get(0));
        }
        log.debugf(Messages.get("CHAINLINK-018004.job.restart.transition"), context, restartId);
        return _runNext(executor, callback, context, context.getJob().getNextExecution(restartId));
    }

    @Override
    public void after(final Executor executor, final ThreadId threadId, final Executable callback,
                      final ExecutionContext context) throws Exception {
        Exception exception = null;
        for (final ListenerImpl listener : this._listeners(executor, context)) {
            try {
                log.debugf(Messages.get("CHAINLINK-018003.job.listener.after.job"), context);
                listener.afterJob(executor, context);
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
    }

    @Override
    public ExecutionWork getNextExecution(final String next) {
        return traversal.next(next);
    }

    private static Deferred<?> _runNext(final Executor executor, final Executable callback,
                                        final ExecutionContext context, final ExecutionWork next) throws Exception {
        return executor.execute(new ExecutionExecutable(callback, next, context));
    }
}
