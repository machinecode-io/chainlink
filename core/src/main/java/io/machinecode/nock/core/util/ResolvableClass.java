package io.machinecode.nock.core.util;

import io.machinecode.nock.spi.util.Resolvable;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class ResolvableClass<T> implements Resolvable<Class<T>> {

    private final String fqcn;
    private transient Class<T> clazz;

    public ResolvableClass(final String fqcn) {
        this.fqcn = fqcn;
    }

    public ResolvableClass(final Class<T> clazz) {
        this.fqcn = clazz.getCanonicalName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> resolve(final ClassLoader loader) throws ClassNotFoundException {
        if (clazz == null) {
            clazz = (Class<T>) loader.loadClass(this.fqcn);
        }
        return clazz;
    }

    public String fqcn() {
        return this.fqcn;
    }
}
