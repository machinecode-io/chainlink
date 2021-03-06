package io.machinecode.chainlink.core.configuration;

import io.machinecode.chainlink.spi.configuration.PropertyModel;

import java.util.Properties;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class PropertyModelImpl implements PropertyModel {

    protected final Properties properties;

    public PropertyModelImpl(final Properties properties) {
        this.properties = properties;
    }

    public PropertyModelImpl() {
        this(new Properties());
    }

    @Override
    public void setProperty(final String name, final String value) {
        properties.setProperty(name, value);
    }
}
