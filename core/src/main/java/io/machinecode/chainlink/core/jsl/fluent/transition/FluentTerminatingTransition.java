package io.machinecode.chainlink.core.jsl.fluent.transition;

import io.machinecode.chainlink.spi.jsl.inherit.transition.InheritableTerminatingTransition;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public abstract class FluentTerminatingTransition<T extends FluentTerminatingTransition<T>> extends FluentTransition<T> implements InheritableTerminatingTransition<T> {

    private String exitStatus;

    @Override
    public String getExitStatus() {
        return exitStatus;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setExitStatus(final String exitStatus) {
        this.exitStatus = exitStatus;
        return (T)this;
    }

    @Override
    public T copy(final T that) {
        return TerminatingTransitionTool.copy((T)this, that);
    }
}
