package io.machinecode.chainlink.core.jsl.fluent;

import io.machinecode.chainlink.spi.jsl.inherit.InheritableBase;

/**
 * An elementName that can inherit from other elements from
 * the JSL Inheritance v1 spec.
 *
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
public abstract class FluentInheritable<T extends FluentInheritable<T>> implements InheritableBase<T> {

    protected Boolean _abstract;
    protected String parent;
    protected String jslName;


    @Override
    public Boolean isAbstract() {
        return _abstract;
    }

    @Override
    public T setAbstract(final Boolean _abstract) {
        this._abstract = _abstract;
        return (T)this;
    }

    @Override
    public String getParent() {
        return parent;
    }

    @Override
    public T setParent(final String parent) {
        this.parent = parent;
        return (T)this;
    }

    @Override
    public String getJslName() {
        return jslName;
    }

    @Override
    public T setJslName(final String jslName) {
        this.jslName = jslName;
        return (T)this;
    }

    @Override
    public T copy(final T that) {
        return BaseTool.copy((T)this, that);
    }

    protected void inheritingElementRule(final T parent) {
        BaseTool.inheritingElementRule(this, parent);
    }
}
