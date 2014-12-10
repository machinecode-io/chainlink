package io.machinecode.chainlink.spi.configuration;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public interface ScopeModel {

    JobOperatorModel getJobOperator(final String name);
}
