package io.machinecode.chainlink.spi.element.partition;

import io.machinecode.chainlink.spi.element.PropertyReference;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
public interface Mapper extends Strategy, PropertyReference {

    String ELEMENT = "mapper";
}
