package io.machinecode.chainlink.core.factory.task;

import io.machinecode.chainlink.core.element.ListenersImpl;
import io.machinecode.chainlink.core.element.PropertiesImpl;
import io.machinecode.chainlink.core.element.partition.PartitionImpl;
import io.machinecode.chainlink.core.element.task.BatchletImpl;
import io.machinecode.chainlink.core.expression.Expression;
import io.machinecode.chainlink.core.factory.PropertiesFactory;
import io.machinecode.chainlink.core.factory.TaskFactory;
import io.machinecode.chainlink.core.inject.ArtifactReferenceImpl;
import io.machinecode.chainlink.spi.element.task.Batchlet;
import io.machinecode.chainlink.spi.expression.JobPropertyContext;
import io.machinecode.chainlink.spi.expression.PropertyContext;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
public class BatchletFactory implements TaskFactory<Batchlet, BatchletImpl, ListenersImpl, PartitionImpl<?>> {

    public static final BatchletFactory INSTANCE = new BatchletFactory();

    @Override
    public BatchletImpl produceExecution(final Batchlet that, final ListenersImpl _listeners, PartitionImpl<?> partition, final JobPropertyContext context) {
        final String ref = Expression.resolveExecutionProperty(that.getRef(), context);
        final PropertiesImpl properties = PropertiesFactory.INSTANCE.produceExecution(that.getProperties(), context);
        return new BatchletImpl(
                context.getReference(new ArtifactReferenceImpl(ref)),
                properties,
                partition
        );
    }

    @Override
    public BatchletImpl producePartitioned(final BatchletImpl that, final ListenersImpl _listeners, PartitionImpl<?> partition, final PropertyContext context) {
        final String ref = Expression.resolvePartitionProperty(that.getRef(), context);
        final PropertiesImpl properties = PropertiesFactory.INSTANCE.producePartitioned(that.getProperties(), context);
        return new BatchletImpl(
                context.getReference(new ArtifactReferenceImpl(ref)),
                properties,
                partition
        );
    }
}
