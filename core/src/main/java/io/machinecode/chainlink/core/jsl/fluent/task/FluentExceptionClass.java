package io.machinecode.chainlink.core.jsl.fluent.task;

import io.machinecode.chainlink.spi.jsl.inherit.task.InheritableExceptionClass;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class FluentExceptionClass implements InheritableExceptionClass<FluentExceptionClass> {

    private String className;

    @Override
    public String getClassName() {
        return this.className;
    }

    public FluentExceptionClass setClassName(final String className) {
        this.className = className;
        return this;
    }

    @Override
    public FluentExceptionClass copy() {
        return copy(new FluentExceptionClass());
    }

    @Override
    public FluentExceptionClass copy(final FluentExceptionClass that) {
        return ExceptionClassTool.copy(this, that);
    }

    @Override
    public FluentExceptionClass merge(final FluentExceptionClass that) {
        return ExceptionClassTool.merge(this, that);
    }
}
