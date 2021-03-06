package io.machinecode.chainlink.core.util;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class Tccl {

    /**
     * @return The context class loader of the thread calling this method.
     */
    public static ClassLoader get() {
        return Thread.currentThread().getContextClassLoader();
    }
}
