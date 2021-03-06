package io.machinecode.chainlink.core.factory.partition;

import io.machinecode.chainlink.core.expression.Expression;
import io.machinecode.chainlink.core.expression.JobPropertyContext;
import io.machinecode.chainlink.core.expression.PartitionPropertyContext;
import io.machinecode.chainlink.core.factory.PropertiesFactory;
import io.machinecode.chainlink.core.inject.ArtifactReferenceImpl;
import io.machinecode.chainlink.core.jsl.impl.PropertiesImpl;
import io.machinecode.chainlink.core.jsl.impl.partition.CollectorImpl;
import io.machinecode.chainlink.spi.jsl.partition.Collector;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class CollectorFactory {

    public static CollectorImpl produceExecution(final Collector that, final JobPropertyContext context) {
        final String ref = Expression.resolveExecutionProperty(that.getRef(), context);
        final PropertiesImpl properties = PropertiesFactory.produceExecution(that.getProperties(), context);
        return new CollectorImpl(
                context.getReference(new ArtifactReferenceImpl(ref)),
                properties
        );
    }

    public static CollectorImpl producePartitioned(final CollectorImpl that, final PartitionPropertyContext context) {
        final String ref = Expression.resolvePartitionProperty(that.getRef(), context);
        final PropertiesImpl properties = PropertiesFactory.producePartitioned(that.getProperties(), context);
        return new CollectorImpl(
                context.getReference(new ArtifactReferenceImpl(ref)),
                properties
        );
    }
}
