package io.machinecode.chainlink.core.jsl.xml.transition;

import io.machinecode.chainlink.spi.jsl.inherit.transition.InheritableTerminatingTransition;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import static javax.xml.bind.annotation.XmlAccessType.NONE;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
@XmlAccessorType(NONE)
public abstract class XmlTerminatingTransition<T extends XmlTerminatingTransition<T>> implements InheritableTerminatingTransition<T> {

    @XmlAttribute(name = "on", required = true)
    private String on;

    @XmlAttribute(name = "exit-status", required = false)
    private String exitStatus;

    public String getOn() {
        return on;
    }

    @SuppressWarnings("unchecked")
    public T setOn(final String on) {
        this.on = on;
        return (T)this;
    }

    public String getExitStatus() {
        return exitStatus;
    }

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
