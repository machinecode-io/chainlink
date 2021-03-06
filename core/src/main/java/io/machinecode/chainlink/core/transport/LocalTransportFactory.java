package io.machinecode.chainlink.core.transport;

import io.machinecode.chainlink.spi.configuration.Dependencies;
import io.machinecode.chainlink.spi.property.PropertyLookup;
import io.machinecode.chainlink.spi.configuration.factory.TransportFactory;
import io.machinecode.chainlink.spi.transport.Transport;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
public class LocalTransportFactory implements TransportFactory {
    @Override
    public Transport produce(final Dependencies dependencies, final PropertyLookup properties) throws Exception {
        return new LocalTransport(dependencies, properties);
    }
}
