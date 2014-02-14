package io.machinecode.nock.jsl.validation;

import io.machinecode.nock.jsl.visitor.ValidatingVisitor;
import io.machinecode.nock.jsl.visitor.VisitorNode;
import io.machinecode.nock.spi.element.Property;
import io.machinecode.nock.spi.util.Messages;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class PropertyValidator extends ValidatingVisitor<Property> {

    public static final PropertyValidator INSTANCE = new PropertyValidator();

    protected PropertyValidator() {
        super(Property.ELEMENT);
    }

    @Override
    public void doVisit(final Property that, final VisitorNode context) {
        if (that.getName() == null) {
            context.addProblem(Messages.format("NOCK-002102.validation.required.attribute", "name"));
        }
        if (that.getValue() == null) {
            context.addProblem(Messages.format("NOCK-002102.validation.required.attribute", "value"));
        }
    }
}
