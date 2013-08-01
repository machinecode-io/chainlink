package io.machinecode.nock.jsl.impl.type;

import io.machinecode.nock.jsl.api.Listeners;
import io.machinecode.nock.jsl.api.Part;
import io.machinecode.nock.jsl.api.Properties;
import io.machinecode.nock.jsl.api.partition.Mapper;
import io.machinecode.nock.jsl.api.transition.Transition;
import io.machinecode.nock.jsl.api.execution.Step;
import io.machinecode.nock.jsl.impl.ListenersImpl;
import io.machinecode.nock.jsl.impl.PropertiesImpl;
import io.machinecode.nock.jsl.impl.transition.TransitionImpl;

import java.util.List;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public abstract class StepImpl<T extends Part, U extends Mapper> extends ExecutionImpl implements Step<T, U> {

    private final String next;
    private final int startLimit;
    private final boolean allowStartIfComplete;
    private final Listeners listeners;
    private final Properties properties;
    private final List<Transition> transitions;

    public StepImpl(final Step<T, U> that) {
        super(that);
        this.next = that.getNext();
        this.startLimit = that.getStartLimit();
        this.allowStartIfComplete = that.isAllowStartIfComplete();
        this.listeners = new ListenersImpl(that.getListeners());
        this.properties = new PropertiesImpl(that.getProperties());
        this.transitions = TransitionImpl.immutableCopyTransitions(that.getTransitions());
    }

    @Override
    public String getNext() {
        return this.next;
    }

    @Override
    public int getStartLimit() {
        return this.startLimit;
    }

    @Override
    public boolean isAllowStartIfComplete() {
        return this.allowStartIfComplete;
    }

    @Override
    public Listeners getListeners() {
        return this.listeners;
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    @Override
    public List<Transition> getTransitions() {
        return this.transitions;
    }
}
