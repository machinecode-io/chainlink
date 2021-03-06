package io.machinecode.chainlink.jsl.groovy

import io.machinecode.chainlink.core.jsl.fluent.FluentProperties
import io.machinecode.chainlink.core.jsl.fluent.FluentProperty;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class GroovyProperties {

    final FluentProperties _value = new FluentProperties();

    def partition(final String partition) {
        _value.partition = partition
    }

    def prop(final Closure cl) {
        def that = new GroovyProperty()
        _value.properties.add that._value
        def code = cl.rehydrate(that, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }

    def prop(final String name, final String value) {
        def that = new FluentProperty()
        that.name = name
        that.value = value
        _value.properties.add that
    }
}
