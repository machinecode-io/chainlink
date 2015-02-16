package io.machinecode.chainlink.core.factory.transition;

import io.machinecode.chainlink.core.expression.Expression;
import io.machinecode.chainlink.core.expression.JobPropertyContext;
import io.machinecode.chainlink.core.expression.PropertyContext;
import io.machinecode.chainlink.core.factory.ElementFactory;
import io.machinecode.chainlink.core.jsl.impl.transition.EndImpl;
import io.machinecode.chainlink.spi.jsl.transition.End;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class EndFactory implements ElementFactory<End, EndImpl> {

    public static final EndFactory INSTANCE = new EndFactory();

    @Override
    public EndImpl produceExecution(final End that, final JobPropertyContext context) {
        final String on = Expression.resolveExecutionProperty(that.getOn(), context);
        final String exitStatus = Expression.resolveExecutionProperty(that.getExitStatus(), context);
        return new EndImpl(on, exitStatus);
    }

    @Override
    public EndImpl producePartitioned(final EndImpl that, final PropertyContext context) {
        final String on = Expression.resolvePartitionProperty(that.getOn(), context);
        final String exitStatus = Expression.resolvePartitionProperty(that.getExitStatus(), context);
        return new EndImpl(on, exitStatus);
    }
}
