package io.machinecode.chainlink.core.validation.partition;

import io.machinecode.chainlink.core.validation.PropertyReferenceValidator;
import io.machinecode.chainlink.spi.jsl.partition.Mapper;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class MapperValidator extends PropertyReferenceValidator<Mapper> {

    public static final MapperValidator INSTANCE = new MapperValidator();

    protected MapperValidator() {
        super(Mapper.ELEMENT);
    }
}
