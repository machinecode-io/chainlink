package io.machinecode.chainlink.core.marshalling;

import io.machinecode.chainlink.spi.configuration.Dependencies;
import io.machinecode.chainlink.spi.property.PropertyLookup;
import io.machinecode.chainlink.spi.configuration.factory.MarshallingFactory;
import io.machinecode.chainlink.spi.marshalling.Marshalling;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class JdkMarshallingFactory implements MarshallingFactory {
    @Override
    public Marshalling produce(final Dependencies dependencies, final PropertyLookup properties) {
        return new JdkMarshalling();
    }
}
