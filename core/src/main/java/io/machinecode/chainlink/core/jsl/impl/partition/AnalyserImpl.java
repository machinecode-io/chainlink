package io.machinecode.chainlink.core.jsl.impl.partition;

import io.machinecode.chainlink.core.jsl.impl.PropertiesImpl;
import io.machinecode.chainlink.core.jsl.impl.PropertyReferenceImpl;
import io.machinecode.chainlink.spi.configuration.Configuration;
import io.machinecode.chainlink.spi.context.ExecutionContext;
import io.machinecode.chainlink.spi.inject.ArtifactReference;
import io.machinecode.chainlink.spi.inject.InjectablesProvider;
import io.machinecode.chainlink.spi.inject.InjectionContext;
import io.machinecode.chainlink.spi.jsl.partition.Analyser;

import javax.batch.api.partition.PartitionAnalyzer;
import javax.batch.runtime.BatchStatus;
import java.io.Serializable;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class AnalyserImpl extends PropertyReferenceImpl<PartitionAnalyzer> implements Analyser {
    private static final long serialVersionUID = 1L;

    public AnalyserImpl(final ArtifactReference ref, final PropertiesImpl properties) {
        super(ref, properties);
    }

    public void analyzeCollectorData(final Configuration configuration, final ExecutionContext context, final Serializable data) throws Exception {
        final InjectionContext injectionContext = configuration.getInjectionContext();
        final InjectablesProvider provider = injectionContext.getProvider();
        try {
            provider.setInjectables(_injectables(context));
            load(PartitionAnalyzer.class, configuration, context).analyzeCollectorData(data);
        } finally {
            provider.setInjectables(null);
        }
    }

    public void analyzeStatus(final Configuration configuration, final ExecutionContext context, final BatchStatus batchStatus, final String exitStatus) throws Exception {
        final InjectionContext injectionContext = configuration.getInjectionContext();
        final InjectablesProvider provider = injectionContext.getProvider();
        try {
            provider.setInjectables(_injectables(context));
            load(PartitionAnalyzer.class, configuration, context).analyzeStatus(batchStatus, exitStatus);
        } finally {
            provider.setInjectables(null);
        }
    }
}
