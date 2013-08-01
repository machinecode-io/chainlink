package io.machinecode.nock.jsl.validation.execution;

import io.machinecode.nock.jsl.api.execution.Step;
import io.machinecode.nock.jsl.api.transition.Transition;
import io.machinecode.nock.jsl.validation.ListenersValidator;
import io.machinecode.nock.jsl.validation.PartValidator;
import io.machinecode.nock.jsl.validation.PropertiesValidator;
import io.machinecode.nock.jsl.validation.ValidationContext;
import io.machinecode.nock.jsl.validation.Validator;
import io.machinecode.nock.jsl.validation.transition.TransitionValidator;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class StepValidator extends Validator<Step> {

    public static final StepValidator INSTANCE = new StepValidator();

    protected StepValidator() {
        super("step");
    }

    @Override
    public void doValidate(final Step that, final ValidationContext context) {
        if (that.getId() == null) {
            context.addProblem("Attribute 'id' is required.");
        }

        if (that.getTransitions() != null) {
            for (final Object transition : that.getTransitions()) {
                if (transition == null) {
                    context.addProblem("Must not have null 'transition' element.");
                }
                TransitionValidator.validate((Transition) transition, context);
            }
        }

        if (that.getPart() != null) {
            PartValidator.validate(that.getPart(), context);
        }
        if (that.getPartition() != null) {
            //TODO
        }
        if (that.getListeners() != null) {
            ListenersValidator.INSTANCE.validate(that.getListeners(), context);
        }
        if (that.getProperties() != null) {
            PropertiesValidator.INSTANCE.validate(that.getProperties(), context);
        }
        //TODO Traverse that.next for cycles
        if (that.getStartLimit() < 0) {
            context.addProblem("Attribute 'start-limit' must be positive. Found '" + that.getStartLimit() + "'.");
        }
    }
}
