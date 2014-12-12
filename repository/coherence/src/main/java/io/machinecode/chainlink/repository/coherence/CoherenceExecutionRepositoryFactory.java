package io.machinecode.chainlink.repository.coherence;

import io.machinecode.chainlink.spi.configuration.ExecutionRepositoryConfiguration;
import io.machinecode.chainlink.spi.configuration.factory.ExecutionRepositoryFactory;
import io.machinecode.chainlink.spi.repository.ExecutionRepository;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
public class CoherenceExecutionRepositoryFactory implements ExecutionRepositoryFactory {
    @Override
    public ExecutionRepository produce(final ExecutionRepositoryConfiguration configuration) throws Exception {
        return new CoherenceExecutonRepository(
                configuration.getMarshallingProviderFactory().produce(configuration)
        );
    }
}