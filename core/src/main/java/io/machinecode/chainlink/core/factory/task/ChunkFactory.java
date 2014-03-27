package io.machinecode.chainlink.core.factory.task;

import io.machinecode.chainlink.core.element.ListenersImpl;
import io.machinecode.chainlink.core.element.partition.PartitionImpl;
import io.machinecode.chainlink.core.element.task.CheckpointAlgorithmImpl;
import io.machinecode.chainlink.core.element.task.ChunkImpl;
import io.machinecode.chainlink.core.element.task.ExceptionClassFilterImpl;
import io.machinecode.chainlink.core.element.task.ItemProcessorImpl;
import io.machinecode.chainlink.core.element.task.ItemReaderImpl;
import io.machinecode.chainlink.core.element.task.ItemWriterImpl;
import io.machinecode.chainlink.core.expression.Expression;
import io.machinecode.chainlink.spi.element.task.Chunk;
import io.machinecode.chainlink.spi.expression.JobPropertyContext;
import io.machinecode.chainlink.spi.expression.PropertyContext;
import io.machinecode.chainlink.core.factory.TaskFactory;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class ChunkFactory implements TaskFactory<Chunk, ChunkImpl, ListenersImpl, PartitionImpl<?>> {

    public static final ChunkFactory INSTANCE = new ChunkFactory();

    @Override
    public ChunkImpl produceExecution(final Chunk that, final ListenersImpl listeners, final PartitionImpl<?> partition, final JobPropertyContext context) {
        final String checkpointPolicy = Expression.resolveExecutionProperty(that.getCheckpointPolicy(), context);
        final String itemCount = Expression.resolveExecutionProperty(that.getItemCount(), context);
        final String timeLimit = Expression.resolveExecutionProperty(that.getTimeLimit(), context);
        final String skipLimit = Expression.resolveExecutionProperty(that.getSkipLimit(), context);
        final String retryLimit = Expression.resolveExecutionProperty(that.getRetryLimit(), context);
        final ItemReaderImpl reader = ItemReaderFactory.INSTANCE.produceExecution(that.getReader(), context); //TODO Should not be null but needs validation
        final ItemProcessorImpl processor = that.getProcessor() == null
                ? null
                : ItemProcessorFactory.INSTANCE.produceExecution(that.getProcessor(), context);
        final ItemWriterImpl writer = ItemWriterFactory.INSTANCE.produceExecution(that.getWriter(), context);
        final CheckpointAlgorithmImpl checkpointAlgorithm = that.getCheckpointAlgorithm() == null
                ? null
                : CheckpointAlgorithmFactory.INSTANCE.produceExecution(that.getCheckpointAlgorithm(), context);
        final ExceptionClassFilterImpl skippableExceptionClasses = ExceptionClassFilterFactory.INSTANCE.produceExecution(that.getSkippableExceptionClasses(), context);
        final ExceptionClassFilterImpl retryableExceptionClasses = ExceptionClassFilterFactory.INSTANCE.produceExecution(that.getRetryableExceptionClasses(), context);
        final ExceptionClassFilterImpl noRollbackExceptionClasses = ExceptionClassFilterFactory.INSTANCE.produceExecution(that.getNoRollbackExceptionClasses(), context);
        return new ChunkImpl(
                checkpointPolicy,
                itemCount,
                timeLimit,
                skipLimit,
                retryLimit,
                reader,
                processor,
                writer,
                checkpointAlgorithm,
                skippableExceptionClasses,
                retryableExceptionClasses,
                noRollbackExceptionClasses,
                listeners,
                partition
        );
    }

    @Override
    public ChunkImpl producePartitioned(final ChunkImpl that, final ListenersImpl listeners, final PartitionImpl<?> partition, final PropertyContext context) {
        final String checkpointPolicy = Expression.resolvePartitionProperty(that.getCheckpointPolicy(), context);
        final String itemCount = Expression.resolvePartitionProperty(that.getItemCount(), context);
        final String timeLimit = Expression.resolvePartitionProperty(that.getTimeLimit(), context);
        final String skipLimit = Expression.resolvePartitionProperty(that.getSkipLimit(), context);
        final String retryLimit = Expression.resolvePartitionProperty(that.getRetryLimit(), context);
        final ItemReaderImpl reader = ItemReaderFactory.INSTANCE.producePartitioned(that.getReader(), context); //TODO Should not be null but needs validation
        final ItemProcessorImpl processor = that.getProcessor() == null
                ? null
                : ItemProcessorFactory.INSTANCE.producePartitioned(that.getProcessor(), context);
        final ItemWriterImpl writer = ItemWriterFactory.INSTANCE.producePartitioned(that.getWriter(), context);
        final CheckpointAlgorithmImpl checkpointAlgorithm = that.getCheckpointAlgorithm() == null
                ? null
                : CheckpointAlgorithmFactory.INSTANCE.producePartitioned(that.getCheckpointAlgorithm(), context);
        final ExceptionClassFilterImpl skippableExceptionClasses = ExceptionClassFilterFactory.INSTANCE.producePartitioned(that.getSkippableExceptionClasses(), context);
        final ExceptionClassFilterImpl retryableExceptionClasses = ExceptionClassFilterFactory.INSTANCE.producePartitioned(that.getRetryableExceptionClasses(), context);
        final ExceptionClassFilterImpl noRollbackExceptionClasses = ExceptionClassFilterFactory.INSTANCE.producePartitioned(that.getNoRollbackExceptionClasses(), context);
        return new ChunkImpl(
                checkpointPolicy,
                itemCount,
                timeLimit,
                skipLimit,
                retryLimit,
                reader,
                processor,
                writer,
                checkpointAlgorithm,
                skippableExceptionClasses,
                retryableExceptionClasses,
                noRollbackExceptionClasses,
                listeners,
                partition
        );
    }
}