package io.machinecode.chainlink.spi.loader;

import io.machinecode.chainlink.spi.exception.ParentNotFoundException;
import io.machinecode.chainlink.spi.jsl.inherit.InheritableElement;

/**
 * <p>A job loader that allows JSL inheritance.</p>
 *
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public interface InheritableJobLoader extends JobLoader {

    /**
     *
     * @param clazz The type of element to find.
     * @param that The element to find the parent of.
     * @param defaultJobXml The default job XML to search for the parent in.
     * @param <T> The type of element to load.
     * @return The parent element.
     * @throws io.machinecode.chainlink.spi.exception.ParentNotFoundException If the parent element is not in this repository.
     */
    <T extends InheritableElement<T>> T findParent(final Class<T> clazz, final T that, final String defaultJobXml) throws ParentNotFoundException;

    /**
     *
     * @param clazz The type of element to find.
     * @param id The id of the element to find.
     * @param jslName The jsl-name of the element to find.
     * @param <T> The type of element to load.
     * @return The parent element.
     * @throws ParentNotFoundException If the parent element is not in this repository.
     */
    <T extends InheritableElement<T>> T findParent(final Class<T> clazz, final String id, String jslName) throws ParentNotFoundException;
}
