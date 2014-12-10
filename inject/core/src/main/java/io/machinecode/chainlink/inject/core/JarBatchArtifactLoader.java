package io.machinecode.chainlink.inject.core;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class JarBatchArtifactLoader extends BatchArtifactLoader {

    public JarBatchArtifactLoader(final ClassLoader loader) {
        super(loader);
    }

    @Override
    public String getPrefix() {
        return "META-INF/";
    }
}
