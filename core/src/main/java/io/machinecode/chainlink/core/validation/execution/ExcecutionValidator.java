package io.machinecode.chainlink.core.validation.execution;

import io.machinecode.chainlink.core.validation.visitor.VisitorNode;
import io.machinecode.chainlink.spi.jsl.execution.Decision;
import io.machinecode.chainlink.spi.jsl.execution.Execution;
import io.machinecode.chainlink.spi.jsl.execution.Flow;
import io.machinecode.chainlink.spi.jsl.execution.Split;
import io.machinecode.chainlink.spi.jsl.execution.Step;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public final class ExcecutionValidator {

    private ExcecutionValidator(){}

    public static void visit(final Execution that, final VisitorNode context) {
        if (that instanceof Split) {
            SplitValidator.INSTANCE.visit((Split) that, context);
        } else if (that instanceof Decision) {
            DecisionValidator.INSTANCE.visit((Decision) that, context);
        } else if (that instanceof Step) {
            StepValidator.INSTANCE.visit((Step) that, context);
        } else if (that instanceof Flow) {
            FlowValidator.INSTANCE.visit((Flow) that, context);
        }
    }
}
