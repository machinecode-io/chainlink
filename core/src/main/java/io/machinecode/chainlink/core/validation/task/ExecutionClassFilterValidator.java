package io.machinecode.chainlink.core.validation.task;

import io.machinecode.chainlink.core.validation.visitor.ValidatingVisitor;
import io.machinecode.chainlink.core.validation.visitor.VisitorNode;
import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.jsl.task.ExceptionClass;
import io.machinecode.chainlink.spi.jsl.task.ExceptionClassFilter;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class ExecutionClassFilterValidator extends ValidatingVisitor<ExceptionClassFilter> {

    public static final ExecutionClassFilterValidator SKIPPABLE = new ExecutionClassFilterValidator("skippable-exception-classes");
    public static final ExecutionClassFilterValidator RETRYABLE = new ExecutionClassFilterValidator("retryable-exception-classes");
    public static final ExecutionClassFilterValidator NO_ROLLBACK = new ExecutionClassFilterValidator("no-rollback-exception-classes");

    protected ExecutionClassFilterValidator(final String element) {
        super(element);
    }

    @Override
    public void doVisit(final ExceptionClassFilter that, final VisitorNode context) {
        if (that.getIncludes() != null) {
            for (final ExceptionClass clazz : that.getIncludes()) {
                if (clazz == null) {
                    context.addProblem(Messages.format("CHAINLINK-002100.validation.not.null.element", "includes"));
                    continue;
                }
                if (clazz.getClassName() == null) {
                    context.addProblem(Messages.format("CHAINLINK-002102.validation.required.attribute", "class"));
                }
            }
        }
        if (that.getExcludes() != null) {
            for (final ExceptionClass clazz : that.getExcludes()) {
                if (clazz == null) {
                    context.addProblem(Messages.format("CHAINLINK-002100.validation.not.null.element", "excludes"));
                    continue;
                }
                if (clazz.getClassName() == null) {
                    context.addProblem(Messages.format("CHAINLINK-002102.validation.required.attribute", "class"));
                }
            }
        }
    }
}
