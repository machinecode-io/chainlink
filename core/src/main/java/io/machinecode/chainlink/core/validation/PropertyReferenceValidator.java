package io.machinecode.chainlink.core.validation;

import io.machinecode.chainlink.core.validation.visitor.ValidatingVisitor;
import io.machinecode.chainlink.core.validation.visitor.VisitorNode;
import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.jsl.PropertyReference;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public abstract class PropertyReferenceValidator<T extends PropertyReference> extends ValidatingVisitor<T> {

    protected PropertyReferenceValidator(final String element) {
        super(element);
    }

    @Override
    public void doVisit(final T that, final VisitorNode context) {
        if (that.getRef() == null) {
            context.addProblem(Messages.format("CHAINLINK-002102.validation.required.attribute", "ref"));
        }
        if (that.getProperties() != null) {
            PropertiesValidator.INSTANCE.visit(that.getProperties(), context);
        }
    }
}
