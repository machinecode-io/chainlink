package io.machinecode.chainlink.jsl.groovy.task

import io.machinecode.chainlink.core.jsl.fluent.task.FluentExceptionClassFilter

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class GroovyExceptionClassFilter {

    final FluentExceptionClassFilter _value = new FluentExceptionClassFilter();

    def include(final String include) {
        _value.addInclude include
    }

    def include(final Class<? extends Throwable> include) {
        _value.addInclude include
    }

    def exclude(final String exclude) {
        _value.addExclude exclude
    }

    def exclude(final Class<? extends Throwable> exclude) {
        _value.addExclude exclude
    }
}
