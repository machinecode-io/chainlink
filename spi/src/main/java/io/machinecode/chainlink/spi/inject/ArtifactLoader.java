package io.machinecode.chainlink.spi.inject;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public interface ArtifactLoader {

    /**
     * @param id The identifier of the artifact to load.
     * @param as An interface the artifact is expected to implement.
     * @param loader The configured classloader.
     * @param <T>
     * @return An artifact matching {@param id} or null if no artifact matching is able to be loaded.
     * @throws ArtifactOfWrongTypeException If the artifact was able to be loaded but does not implement {@param as}
     */
    <T> T load(final String id, final Class<T> as, final ClassLoader loader) throws ArtifactOfWrongTypeException;
}