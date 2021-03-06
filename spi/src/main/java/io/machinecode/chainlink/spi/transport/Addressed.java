package io.machinecode.chainlink.spi.transport;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public interface Addressed {

    /**
     * @return A unique identifier for a node.
     */
    Object getAddress();
}
