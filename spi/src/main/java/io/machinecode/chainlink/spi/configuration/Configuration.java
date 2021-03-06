package io.machinecode.chainlink.spi.configuration;

import io.machinecode.chainlink.spi.execution.Executor;
import io.machinecode.chainlink.spi.property.PropertyLookup;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public interface Configuration extends Dependencies, PropertyLookup {

    Executor getExecutor();
}
