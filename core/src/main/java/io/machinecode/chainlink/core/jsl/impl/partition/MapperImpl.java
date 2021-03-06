package io.machinecode.chainlink.core.jsl.impl.partition;

import io.machinecode.chainlink.core.context.ExecutionContextImpl;
import io.machinecode.chainlink.core.jsl.impl.PropertiesImpl;
import io.machinecode.chainlink.core.jsl.impl.PropertyReferenceImpl;
import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.configuration.Configuration;
import io.machinecode.chainlink.spi.inject.ArtifactReference;
import io.machinecode.chainlink.spi.inject.InjectablesProvider;
import io.machinecode.chainlink.spi.inject.InjectionContext;
import io.machinecode.chainlink.spi.jsl.partition.Mapper;
import org.jboss.logging.Logger;

import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class MapperImpl extends PropertyReferenceImpl<PartitionMapper> implements Mapper, StrategyWork {
    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(MapperImpl.class);

    public MapperImpl(final ArtifactReference ref, final PropertiesImpl properties) {
        super(ref, properties);
    }

    @Override
    public PartitionPlan getPartitionPlan(final Configuration configuration, final ExecutionContextImpl context) throws Exception {
        log.debugf(Messages.get("CHAINLINK-022000.mapper.map.partitions"), context.getJobExecutionId(), ref.ref());
        final InjectionContext injectionContext = configuration.getInjectionContext();
        final InjectablesProvider provider = injectionContext.getProvider();
        try {
            provider.setInjectables(_injectables(context));
            return load(PartitionMapper.class, configuration, context).mapPartitions();
        } finally {
            provider.setInjectables(null);
        }
    }
}
