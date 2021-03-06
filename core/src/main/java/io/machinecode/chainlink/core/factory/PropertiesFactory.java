package io.machinecode.chainlink.core.factory;

import io.machinecode.chainlink.core.expression.Expression;
import io.machinecode.chainlink.core.expression.JobPropertyContext;
import io.machinecode.chainlink.core.expression.PartitionPropertyContext;
import io.machinecode.chainlink.core.jsl.impl.PropertiesImpl;
import io.machinecode.chainlink.core.jsl.impl.PropertyImpl;
import io.machinecode.chainlink.core.util.Copy;
import io.machinecode.chainlink.core.util.Copy.ExpressionTransformer;
import io.machinecode.chainlink.spi.jsl.Properties;
import io.machinecode.chainlink.spi.jsl.Property;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class PropertiesFactory {

    private static final ExpressionTransformer<Property, PropertyImpl, JobPropertyContext> PROPERTY_BUILD_TRANSFORMER = new ExpressionTransformer<Property, PropertyImpl, JobPropertyContext>() {
        @Override
        public PropertyImpl transform(final Property that, final JobPropertyContext context) {
            return PropertyFactory.produceExecution(that, context);
        }
    };
    private static final ExpressionTransformer<PropertyImpl, PropertyImpl, PartitionPropertyContext> PROPERTY_PARTITION_TRANSFORMER = new ExpressionTransformer<PropertyImpl, PropertyImpl, PartitionPropertyContext>() {
        @Override
        public PropertyImpl transform(final PropertyImpl that, final PartitionPropertyContext context) {
            return PropertyFactory.producePartitioned(that, context);
        }
    };

    public static PropertiesImpl produceExecution(final Properties that, final JobPropertyContext context) {
        final String partition;
        final List<PropertyImpl> properties;
        if (that == null) {
            partition = null;
            properties = Collections.emptyList();
        } else {
            partition = Expression.resolveExecutionProperty(that.getPartition(), context);
            properties = Copy.immutableCopy(that.getProperties(), context, PROPERTY_BUILD_TRANSFORMER);
        }
        return new PropertiesImpl(
                partition,
                properties
        );
    }

    public static PropertiesImpl producePartitioned(final PropertiesImpl that, final PartitionPropertyContext context) {
        final String partition = Expression.resolvePartitionProperty(that.getPartition(), context);
        final List<PropertyImpl> properties = Copy.immutableCopy(that.getProperties(), context, PROPERTY_PARTITION_TRANSFORMER);
        return new PropertiesImpl(
                partition,
                properties
        );
    }
}
