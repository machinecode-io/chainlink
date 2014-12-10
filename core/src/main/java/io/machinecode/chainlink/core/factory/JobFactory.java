package io.machinecode.chainlink.core.factory;

import io.machinecode.chainlink.core.element.JobImpl;
import io.machinecode.chainlink.core.element.ListenersImpl;
import io.machinecode.chainlink.core.element.PropertiesImpl;
import io.machinecode.chainlink.core.element.execution.ExecutionImpl;
import io.machinecode.chainlink.core.expression.Expression;
import io.machinecode.chainlink.core.expression.JobPropertyContextImpl;
import io.machinecode.chainlink.core.factory.execution.Executions;
import io.machinecode.chainlink.core.validation.InvalidJobException;
import io.machinecode.chainlink.core.validation.JobValidator;
import io.machinecode.chainlink.core.validation.visitor.VisitorNode;
import io.machinecode.chainlink.spi.element.Job;

import java.util.List;
import java.util.Properties;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class JobFactory {

    public static final JobFactory INSTANCE = new JobFactory();

    public static JobImpl produce(final Job that, final Properties parameters) throws InvalidJobException {
        return INSTANCE.produceExecution(that, parameters);
    }

    private JobImpl produceExecution(final Job that, final Properties parameters) throws InvalidJobException {
        final VisitorNode before = JobValidator.INSTANCE.visit(that);
        if (JobValidator.hasFailed(before)) {
            throw new InvalidJobException(before);
        }
        final JobPropertyContextImpl context = new JobPropertyContextImpl(parameters);

        final String id = Expression.resolveExecutionProperty(that.getId(), context);
        final String version = Expression.resolveExecutionProperty(that.getVersion(), context);
        final String restartable = Expression.resolveExecutionProperty(that.getRestartable(), context);
        final PropertiesImpl properties = PropertiesFactory.INSTANCE.produceExecution(that.getProperties(), context);
        final ListenersImpl listeners = JobListenersFactory.INSTANCE.produceExecution(that.getListeners(), context);
        final List<ExecutionImpl> executions = Executions.immutableCopyExecutionsDescriptor(that.getExecutions(), context);
        return new JobImpl(
                id,
                version,
                restartable,
                properties,
                listeners,
                executions
        );
    }

    public static VisitorNode validate(final Job job) {
        final VisitorNode node = JobValidator.INSTANCE.visit(job);
        boolean failed = JobValidator.findProblems(node);
        if (failed) {
            throw new InvalidJobException(node);
        }
        return node;
    }
}
