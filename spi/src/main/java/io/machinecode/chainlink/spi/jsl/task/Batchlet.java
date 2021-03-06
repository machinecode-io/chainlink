package io.machinecode.chainlink.spi.jsl.task;

import io.machinecode.chainlink.spi.jsl.PropertyReference;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public interface Batchlet extends Task, PropertyReference {

    String ELEMENT = "batchlet";

}
