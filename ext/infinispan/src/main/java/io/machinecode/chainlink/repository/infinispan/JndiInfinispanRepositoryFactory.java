package io.machinecode.chainlink.repository.infinispan;

import io.machinecode.chainlink.core.Constants;
import io.machinecode.chainlink.spi.configuration.Dependencies;
import io.machinecode.chainlink.spi.property.PropertyLookup;
import io.machinecode.chainlink.spi.configuration.factory.RepositoryFactory;
import io.machinecode.chainlink.spi.repository.Repository;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.naming.InitialContext;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class JndiInfinispanRepositoryFactory implements RepositoryFactory {
    @Override
    public Repository produce(final Dependencies dependencies, final PropertyLookup properties) throws Exception {
        return new InfinispanRepository(
                dependencies.getMarshalling(),
                InitialContext.<EmbeddedCacheManager>doLookup(properties.getProperty(Constants.CACHE_MANAGER_JNDI_NAME))
        );
    }
}
