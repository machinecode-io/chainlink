package io.machinecode.chainlink.jsl.groovy.execution

import io.machinecode.chainlink.core.jsl.fluent.execution.FluentSplit

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class GroovySplit {

    final FluentSplit _value = new FluentSplit();

    def id(final String id) {
        _value.id = id
    }

    def next(final String next) {
        _value.next = next
    }

    def flow(final Closure cl) {
        def that = new GroovyFlow()
        _value.flows.add that._value
        def code = cl.rehydrate(that, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }
}
