package io.machinecode.chainlink.inject.seam;

import io.machinecode.chainlink.spi.inject.ArtifactLoader;
import io.machinecode.chainlink.spi.inject.ArtifactOfWrongTypeException;
import io.machinecode.chainlink.spi.Messages;
import org.jboss.logging.Logger;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.contexts.Lifecycle;

import javax.batch.operations.BatchRuntimeException;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
@Name("seamArtifactLoader")
@Scope(ScopeType.APPLICATION)
@Startup
@AutoCreate
public class SeamArtifactLoader implements ArtifactLoader {

    private static final Logger log = Logger.getLogger(SeamArtifactLoader.class);

    @Override
    public <T> T load(final String id, final Class<T> as, final ClassLoader loader) throws BatchRuntimeException {
        return inject(id, as);
    }

    public static <T> T inject(final String id, final Class<T> as) {
        if (Contexts.isApplicationContextActive()) {
            final Object that = Component.getInstance(id);
            if (that == null) {
                log.tracef(Messages.get("CHAINLINK-025001.artifact.loader.not.found"), id, as.getSimpleName());
                return null;
            }
            if (as.isAssignableFrom(that.getClass())) {
                return as.cast(that);
            }
            throw new ArtifactOfWrongTypeException(Messages.format("CHAINLINK-025000.artifact.loader.assignability", id, as.getSimpleName()));
        } else {
            try {
                Lifecycle.beginCall();
                final Object that = Component.getInstance(id);
                if (that == null) {
                    log.tracef(Messages.get("CHAINLINK-025001.artifact.loader.not.found"), id, as.getSimpleName());
                    return null;
                }
                if (as.isAssignableFrom(that.getClass())) {
                    return as.cast(that);
                }
                throw new ArtifactOfWrongTypeException(Messages.format("CHAINLINK-025000.artifact.loader.assignability", id, as.getSimpleName()));
            } finally {
                Lifecycle.endCall();
            }
        }
    }
}
