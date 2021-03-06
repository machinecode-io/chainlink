package io.machinecode.chainlink.core.validation;

import io.machinecode.chainlink.core.validation.visitor.ValidatingVisitor;
import io.machinecode.chainlink.core.validation.visitor.VisitorNode;
import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.jsl.Listener;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class ListenerValidator extends ValidatingVisitor<Listener> {

    public static final ListenerValidator INSTANCE = new ListenerValidator();

    protected ListenerValidator() {
        super(Listener.ELEMENT);
    }

    @Override
    public void doVisit(final Listener that, final VisitorNode context) {
        if (that.getRef() == null) {
            context.addProblem(Messages.format("CHAINLINK-002102.validation.required.attribute", "ref"));
        }
        if (that.getProperties() != null) {
            PropertiesValidator.INSTANCE.visit(that.getProperties(), context);
        }
    }
}
