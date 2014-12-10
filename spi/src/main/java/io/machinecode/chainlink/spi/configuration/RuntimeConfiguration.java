package io.machinecode.chainlink.spi.configuration;

import io.machinecode.chainlink.spi.execution.Executor;
import io.machinecode.chainlink.spi.inject.InjectionContext;
import io.machinecode.chainlink.spi.registry.ExecutionRepositoryId;
import io.machinecode.chainlink.spi.registry.Registry;
import io.machinecode.chainlink.spi.repository.ExecutionRepository;
import io.machinecode.chainlink.spi.transport.Transport;

import javax.transaction.TransactionManager;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public interface RuntimeConfiguration {

    Executor getExecutor();

    Registry getRegistry();

    Transport<?> getTransport();

    TransactionManager getTransactionManager();

    ExecutionRepository getExecutionRepository(final ExecutionRepositoryId id);

    InjectionContext getInjectionContext();
}
