package io.machinecode.nock.core.expression;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import io.machinecode.nock.spi.factory.PropertyContext;
import io.machinecode.nock.spi.inject.ArtifactReference;

import java.util.Properties;

/**
* @author Brent Douglas <brent.n.douglas@gmail.com>
*/
public class PropertyContextImpl implements PropertyContext {

    private final Properties properties;
    private final TMap<String,ArtifactReference> references;

    public PropertyContextImpl() {
        this(new Properties());
    }

    public PropertyContextImpl(final Properties properties) {
        this.properties = properties;
        this.references = new THashMap<String, ArtifactReference>();
    }

    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public <T extends ArtifactReference> T getReference(final T that) {
        final T old = (T) references.putIfAbsent(that.ref(), that);
        return old == null ? that : old;
    }
}
