package io.machinecode.chainlink.core.jsl.impl.task;

import io.machinecode.chainlink.core.jsl.impl.PropertiesImpl;
import io.machinecode.chainlink.core.jsl.impl.PropertyReferenceImpl;
import io.machinecode.chainlink.spi.configuration.Configuration;
import io.machinecode.chainlink.spi.context.ExecutionContext;
import io.machinecode.chainlink.spi.inject.ArtifactReference;
import io.machinecode.chainlink.spi.inject.InjectablesProvider;
import io.machinecode.chainlink.spi.inject.InjectionContext;
import io.machinecode.chainlink.spi.jsl.task.ItemWriter;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class ItemWriterImpl extends PropertyReferenceImpl<javax.batch.api.chunk.ItemWriter> implements ItemWriter {
    private static final long serialVersionUID = 1L;

    public ItemWriterImpl(final ArtifactReference ref, final PropertiesImpl properties) {
        super(ref, properties);
    }

    public void open(final Configuration configuration, final ExecutionContext context, final Serializable checkpoint) throws Exception {
        final InjectionContext injectionContext = configuration.getInjectionContext();
        final InjectablesProvider provider = injectionContext.getProvider();
        try {
            provider.setInjectables(_injectables(context));
            load(javax.batch.api.chunk.ItemWriter.class, configuration, context).open(checkpoint);
        } finally {
            provider.setInjectables(null);
        }
    }

    public void close(final Configuration configuration, final ExecutionContext context) throws Exception {
        final InjectionContext injectionContext = configuration.getInjectionContext();
        final InjectablesProvider provider = injectionContext.getProvider();
        try {
            provider.setInjectables(_injectables(context));
            load(javax.batch.api.chunk.ItemWriter.class, configuration, context).close();
        } finally {
            provider.setInjectables(null);
        }
    }

    public void writeItems(final Configuration configuration, final ExecutionContext context, final List<Object> items) throws Exception {
        final InjectionContext injectionContext = configuration.getInjectionContext();
        final InjectablesProvider provider = injectionContext.getProvider();
        try {
            provider.setInjectables(_injectables(context));
            load(javax.batch.api.chunk.ItemWriter.class, configuration, context).writeItems(items);
        } finally {
            provider.setInjectables(null);
        }
    }

    public Serializable checkpointInfo(final Configuration configuration, final ExecutionContext context) throws Exception {
        final InjectionContext injectionContext = configuration.getInjectionContext();
        final InjectablesProvider provider = injectionContext.getProvider();
        try {
            provider.setInjectables(_injectables(context));
            return load(javax.batch.api.chunk.ItemWriter.class, configuration, context).checkpointInfo();
        } finally {
            provider.setInjectables(null);
        }
    }
}
