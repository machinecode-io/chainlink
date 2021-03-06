package io.machinecode.chainlink.core;

import io.machinecode.chainlink.core.schema.Configure;
import io.machinecode.chainlink.spi.exception.NoConfigurationWithIdException;
import io.machinecode.chainlink.spi.management.ExtendedJobOperator;
import io.machinecode.chainlink.core.schema.SubSystemSchema;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public interface Environment {

    /**
     * @param name The name of the operator to find.
     * @return A deployment scoped scoped {@link ExtendedJobOperator} if one can be found, if not
     * a subsystem scoped {@link ExtendedJobOperator}.
     * @throws NoConfigurationWithIdException If there is no deployment or subsystem scoped
     * operator configured with the name {@param name}.
     */
    ExtendedJobOperator getJobOperator(final String name) throws NoConfigurationWithIdException;

    /**
     * @return An immutable view of current Chainlink configuration.
     */
    SubSystemSchema<?,?,?> getConfiguration();

    /**
     * @param configure Some code to run with a mutable view of the current configuration. Changes
     *                  applied here will are stored in an implementation specific manner and are
     *                  immediately available to {@link #getConfiguration()} and subsequent calls
     *                  to this method though are not visible to {@link #getJobOperator(String)}
     *                  until {@link #reload()} is called.
     *
     *                  If an Exception is thrown while running {@link Configure#configure(io.machinecode.chainlink.core.schema.MutableSubSystemSchema)}
     *                  no changes will be visible to any methods.
     * @return An immutable view of the Chainlink configuration before {@param configure} was applied.
     */
    SubSystemSchema<?,?,?> setConfiguration(final Configure configure);

    /**
     * Make the currently stored configuration available to
     * {@link #getJobOperator(String)}.
     * @throws Exception On an implementation specific error.
     */
    void reload() throws Exception;
}
