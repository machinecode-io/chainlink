package io.machinecode.chainlink.core.validation;

import io.machinecode.chainlink.core.validation.visitor.ValidatingVisitor;
import io.machinecode.chainlink.core.validation.visitor.VisitorNode;
import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.jsl.Listener;
import io.machinecode.chainlink.spi.jsl.Listeners;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class ListenersValidator extends ValidatingVisitor<Listeners> {

    public static final ListenersValidator INSTANCE = new ListenersValidator();

    protected ListenersValidator() {
        super(Listeners.ELEMENT);
    }

    @Override
    public void doVisit(final Listeners that, final VisitorNode context) {
        for(final Listener listener : that.getListeners()) {
            if (listener == null) {
                context.addProblem(Messages.format("CHAINLINK-002100.validation.not.null.element", "listener"));
                continue;
            }
            ListenerValidator.INSTANCE.visit(listener, context);
        }
    }
}
