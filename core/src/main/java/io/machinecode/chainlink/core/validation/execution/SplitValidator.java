package io.machinecode.chainlink.core.validation.execution;

import io.machinecode.chainlink.core.validation.visitor.ValidatingVisitor;
import io.machinecode.chainlink.core.validation.visitor.VisitorNode;
import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.jsl.execution.Flow;
import io.machinecode.chainlink.spi.jsl.execution.Split;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class SplitValidator extends ValidatingVisitor<Split> {

    public static final SplitValidator INSTANCE = new SplitValidator();

    protected SplitValidator() {
        super(Split.ELEMENT);
    }

    @Override
    public void doVisit(final Split that, final VisitorNode context) {
        if (that.getId() == null) {
            context.addProblem(Messages.format("CHAINLINK-002102.validation.required.attribute", "id"));
        } else {
            context.addTransition(Messages.get("CHAINLINK-002301.validation.next.attribute"), that.getNext());
        }

        if (that.getFlows() != null) {
            for (final Flow flow : that.getFlows()) {
                if (flow == null) {
                    context.addProblem(Messages.format("CHAINLINK-002100.validation.not.null.element", "flow"));
                    continue;
                }
                context.addChildTransition(Messages.get("CHAINLINK-002303.validation.split.implicit"), flow.getId());
                FlowValidator.INSTANCE.visit(flow, context);
            }
        }
    }
}
