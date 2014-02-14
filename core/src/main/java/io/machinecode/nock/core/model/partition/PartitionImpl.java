package io.machinecode.nock.core.model.partition;

import io.machinecode.nock.core.expression.PropertyContextImpl;
import io.machinecode.nock.core.impl.ExecutionContextImpl;
import io.machinecode.nock.core.impl.JobContextImpl;
import io.machinecode.nock.core.impl.StepContextImpl;
import io.machinecode.nock.core.work.ItemImpl;
import io.machinecode.nock.core.work.TaskExecutable;
import io.machinecode.nock.spi.ExecutionRepository;
import io.machinecode.nock.spi.PartitionExecution;
import io.machinecode.nock.spi.context.ExecutionContext;
import io.machinecode.nock.spi.context.MutableStepContext;
import io.machinecode.nock.spi.element.partition.Partition;
import io.machinecode.nock.spi.execution.Executable;
import io.machinecode.nock.spi.execution.Executor;
import io.machinecode.nock.spi.execution.Item;
import io.machinecode.nock.spi.util.Messages;
import io.machinecode.nock.spi.work.PartitionTarget;
import io.machinecode.nock.spi.work.PartitionWork;
import io.machinecode.nock.spi.work.StrategyWork;
import io.machinecode.nock.spi.work.TaskWork;
import org.jboss.logging.Logger;

import javax.batch.api.partition.PartitionAnalyzer;
import javax.batch.api.partition.PartitionCollector;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionReducer;
import javax.batch.api.partition.PartitionReducer.PartitionStatus;
import javax.batch.runtime.BatchStatus;
import javax.transaction.Status;
import javax.transaction.TransactionManager;
import java.util.Date;
import java.util.Properties;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class PartitionImpl<T extends StrategyWork> implements Partition<T>, PartitionWork<T> {

    private static final Logger log = Logger.getLogger(PartitionImpl.class);

    private final CollectorImpl collector;
    private final AnalyserImpl analyser;
    private final ReducerImpl reducer;
    private final T strategy;

    private transient ThreadLocal<PartitionCollector> _collector;
    private transient PartitionReducer _reducer;
    private transient PartitionPlan _plan;
    private transient PartitionAnalyzer _analyser;

    public PartitionImpl(final CollectorImpl collector, final AnalyserImpl analyser, final ReducerImpl reducer, final T strategy) {
        this.collector = collector;
        this.analyser = analyser;
        this.reducer = reducer;
        this.strategy = strategy;
    }

    @Override
    public CollectorImpl getCollector() {
        return this.collector;
    }

    @Override
    public AnalyserImpl getAnalyzer() {
        return this.analyser;
    }

    @Override
    public ReducerImpl getReducer() {
         return this.reducer;
    }

    @Override
    public T getStrategy() {
        return this.strategy;
    }

    public PartitionCollector loadPartitionCollector(final Executor executor, final ExecutionContext context) throws Exception {
        if (collector == null) {
            return null;
        }
        synchronized (collector) {
            if (_collector == null) {
                _collector = new ThreadLocal<PartitionCollector>();
            }
            if (_collector.get() == null) {
                _collector.set(collector.load(executor, context));
            }
        }
        return _collector.get();
    }

    public PartitionReducer loadPartitionReducer(final Executor executor, final ExecutionContext context) throws Exception {
        if (reducer == null) {
            return null;
        }
        if (_reducer == null) {
            _reducer = reducer.load(executor, context);
        }
        return _reducer;
    }

    public PartitionPlan loadPartitionPlan(final Executor executor, final ExecutionContext context) throws Exception {
        if (strategy == null) {
            return null;
        }
        if (_plan == null) {
            _plan = strategy.getPartitionPlan(executor, context);
        }
        return _plan;
    }

    public PartitionAnalyzer loadPartitionAnalyzer(final Executor executor, final ExecutionContext context) throws Exception {
        if (analyser == null) {
            return null;
        }
        if (_analyser == null) {
            _analyser = analyser.load(executor, context);
        }
        return _analyser;
    }

    // Lifecycle

    @Override
    public PartitionTarget map(final TaskWork task, final Executor executor, final Executable callback, final ExecutionContext context, final int timeout, final Long restartStepExecutionId) throws Exception {
        final PartitionReducer reducer = this.loadPartitionReducer(executor, context);
        final PartitionPlan plan = loadPartitionPlan(executor, context);
        final boolean restarting = context.isRestarting();
        final boolean override = plan.getPartitionsOverride();
        if (reducer != null) {
            //TODO Find out why not
            //if (restarting && !override) {
            //    reducer.rollbackPartitionedStep();
            //}
            log.debugf(Messages.get("NOCK-011300.partition.reducer.before.partitioned.step"), context);
            reducer.beginPartitionedStep();
        }
        final MutableStepContext stepContext = context.getStepContext();
        final String stepName = stepContext.getStepName();
        final ExecutionRepository repository = executor.getRepository();
        final Properties[] properties = plan.getPartitionProperties();
        final Executable[] executables;
        final long stepExecutionId = context.getStepExecutionId();
        if (restarting && !override) {
            final PartitionExecution[] partitionExecutions = repository.getUnfinishedPartitionExecutions(restartStepExecutionId);
            executables = new Executable[partitionExecutions.length];
            for (int id = 0; id < partitionExecutions.length; ++id) {
                final PartitionExecution execution = partitionExecutions[id];
                //final Properties properties = execution.getPartitionProperties();
                final int partitionId = execution.getPartitionId();
                repository.createPartitionExecution(stepExecutionId, execution, new Date());
                //TODO Which step execution should this be linked to?
                final MutableStepContext partitionStepContext = new StepContextImpl(stepContext, execution.getMetrics(), execution.getPersistentUserData());
                final ExecutionContext partitionContext = new ExecutionContextImpl(
                        context.getJob(),
                        new JobContextImpl(context.getJobContext()),
                        partitionStepContext,
                        context.getJobExecution(),
                        context.getRestartJobExecution(),
                        partitionId
                );
                final Properties props = partitionId >= properties.length ? null : properties[partitionId];
                executables[id] = new TaskExecutable(
                        callback,
                        task.partition(new PropertyContextImpl(props)),
                        partitionContext,
                        stepName,
                        partitionId,
                        timeout
                );
            }
        } else {
            final int partitions = plan.getPartitions();
            executables = new Executable[partitions];
            for (int partitionId = 0; partitionId < partitions; ++partitionId) {
                final ExecutionContext partitionContext = new ExecutionContextImpl(
                        context.getJob(),
                        new JobContextImpl(context.getJobContext()),
                        new StepContextImpl(context.getStepContext()),
                        context.getJobExecution(),
                        context.getRestartJobExecution(),
                        partitionId
                );
                //if (partitions != properties.length) {
                //    throw new IllegalStateException(Messages.format("NOCK01101.partition.properties.length", jobExecutionId, partitions, properties.length));
                //}
                //TODO Not really sure if this is how properties are meant to be distributed
                final Properties partitionProperties = partitionId < properties.length ? properties[partitionId] : null;
                repository.createPartitionExecution(stepExecutionId, partitionId, partitionProperties, new Date());
                executables[partitionId] = new TaskExecutable(
                        callback,
                        task.partition(new PropertyContextImpl(partitionProperties)),
                        partitionContext,
                        stepName,
                        partitionId,
                        timeout
                );
            }
        }
        return new PartitionTarget(
                executables,
                plan.getThreads()
        );
    }

    @Override
    public Item collect(final TaskWork task, final Executor executor, final ExecutionContext context, final BatchStatus batchStatus, final String exitStatus) throws Exception {
        final PartitionCollector collector = loadPartitionCollector(executor, context);
        if (collector != null) {
            log.debugf(Messages.get("NOCK-011100.partition.collect.partitioned.data"), context, this.collector.getRef());
            return new ItemImpl(
                    collector.collectPartitionData(),
                    batchStatus,
                    exitStatus
            );
        }
        return new ItemImpl(null, batchStatus, exitStatus);
    }

    @Override
    public PartitionStatus analyse(final Executor executor, final ExecutionContext context, final TransactionManager transactionManager, final Item... items) throws Exception {
        PartitionStatus partitionStatus = PartitionStatus.COMMIT;
        try {
            final PartitionAnalyzer analyzer = loadPartitionAnalyzer(executor, context);
            if (items != null && analyzer != null) {
                if (this.collector != null) {
                    for (final Item that : items) {
                        log.debugf(Messages.get("NOCK-011200.partition.analyse.collector.data"), context, this.analyser.getRef());
                        analyzer.analyzeCollectorData(that.getData());
                    }
                }
                for (final Item item : items) {
                    analyzer.analyzeStatus(item.getBatchStatus(), item.getExitStatus());
                }
            }
        } catch (final Exception e) {
            log.infof(e, Messages.get("NOCK-011201.partition.analyser.caught"), context, this.analyser.getRef());
            partitionStatus = PartitionStatus.ROLLBACK;
            if (transactionManager.getStatus() == Status.STATUS_ACTIVE) {
                transactionManager.setRollbackOnly();
            }
        }
        return partitionStatus;
    }

    @Override
    public void reduce(final PartitionStatus partitionStatus, final Executor executor, final ExecutionContext context, final TransactionManager transactionManager) throws Exception {
        final PartitionReducer reducer = loadPartitionReducer(executor, context);
        try {
            switch (partitionStatus) {
                case COMMIT:
                    try {
                        if (reducer != null) {
                            log.debugf(Messages.get("NOCK-011301.partition.reducer.before.partitioned.step.complete"), context, this.reducer.getRef());
                            reducer.beforePartitionedStepCompletion();
                        }
                        transactionManager.commit();
                    } catch (final Exception e) {
                        log.warnf(e, Messages.get("NOCK-011304.partition.reducer.caught"), context, this.reducer.getRef());
                        transactionManager.rollback();
                    }
                    break;
                case ROLLBACK:
                    try {
                        if (reducer != null) {
                            log.debugf(Messages.get("NOCK-011302.partition.reducer.rollback.partitioned.step"), context, this.reducer.getRef());
                            reducer.rollbackPartitionedStep();
                        }
                    } finally {
                        transactionManager.rollback();
                    }
                    break;
            }
        } finally {
            if (reducer != null) {
                log.debugf(Messages.get("NOCK-011303.partition.reducer.after.partitioned.step"), context, this.reducer.getRef(), partitionStatus);
                reducer.afterPartitionedStepCompletion(partitionStatus);
            }
        }
    }
}
