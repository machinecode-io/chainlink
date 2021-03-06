package io.machinecode.chainlink.core.validation.execution;

import io.machinecode.chainlink.core.validation.PropertyReferenceValidator;
import io.machinecode.chainlink.core.validation.transition.TransitionValidator;
import io.machinecode.chainlink.core.validation.visitor.VisitorNode;
import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.jsl.execution.Decision;
import io.machinecode.chainlink.spi.jsl.transition.Transition;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class DecisionValidator extends PropertyReferenceValidator<Decision> {

    public static final DecisionValidator INSTANCE = new DecisionValidator();

    protected DecisionValidator() {
        super(Decision.ELEMENT);
    }

    @Override
    public void doVisit(final Decision that, final VisitorNode context) {
        if (that.getId() == null) {
            context.addProblem(Messages.format("CHAINLINK-002102.validation.required.attribute", "id"));
        }

        if (that.getTransitions() != null) {
            for (final Transition transition : that.getTransitions()) {
                if (transition == null) {
                    context.addProblem(Messages.format("CHAINLINK-002100.validation.not.null.element", "transitions"));
                }
                TransitionValidator.visit(transition, context);
            }
        }
    }
}
