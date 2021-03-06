package io.machinecode.chainlink.core.configuration;

import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.configuration.ConfigurationLoader;
import io.machinecode.chainlink.spi.inject.ArtifactOfWrongTypeException;
import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class ClassLoaderConfigurationLoader implements ConfigurationLoader {

    private static final Logger log = Logger.getLogger(ClassLoaderConfigurationLoader.class);

    @Override
    public <T> T load(final String id, final Class<T> as, final ClassLoader loader) throws Exception {
        try {
            final Class<?> that = loader.loadClass(id);
            if (as.isAssignableFrom(that)) {
                return as.cast(that.newInstance());
            } else {
                throw new ArtifactOfWrongTypeException(Messages.format("CHAINLINK-025000.artifact.loader.assignability", id, as.getCanonicalName()));
            }
        } catch (final ClassNotFoundException e) {
            log.tracef(Messages.get("CHAINLINK-025001.artifact.loader.not.found"), id, as.getSimpleName());
        } catch (final InstantiationException e) {
            log.warnf(Messages.get("CHAINLINK-025002.artifact.loader.instantiation"), id, as.getSimpleName());
        } catch (final IllegalAccessException e) {
            log.warnf(Messages.get("CHAINLINK-025003.artifact.loader.access"), id, as.getSimpleName());
        }
        return null;
    }
}
