package io.machinecode.chainlink.transport.infinispan;

import io.machinecode.chainlink.core.then.ChainImpl;
import io.machinecode.chainlink.spi.registry.ChainId;
import io.machinecode.chainlink.spi.then.Chain;
import io.machinecode.chainlink.transport.infinispan.cmd.InvokeChainCommand;
import io.machinecode.then.core.PromiseImpl;
import org.infinispan.remoting.transport.Address;

import java.io.Serializable;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class RemoteChain extends ChainImpl<Void> {

    protected final InfinispanRegistry registry;
    protected final Address address;
    protected final long jobExecutionId;
    protected final ChainId chainId;

    public RemoteChain(final InfinispanRegistry registry, final Address address, final long jobExecutionId, final ChainId chainId) {
        this.registry = registry;
        this.address = address;
        this.jobExecutionId = jobExecutionId;
        this.chainId = chainId;
    }

    protected InvokeChainCommand command(final String name, final boolean willReturn, final Serializable... params) {
        return new InvokeChainCommand(registry.cacheName, jobExecutionId, chainId, name, willReturn, params);
    }

    @Override
    public void resolve(final Void value) {
        try {
            final PromiseImpl<Void,Throwable> promise = new PromiseImpl<Void,Throwable>();
            registry.invoke(address, command("resolve", false, new Serializable[]{ null }), promise);
            super.resolve(value);
            promise.get();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reject(final Throwable failure) {
        try {
            final PromiseImpl<Void,Throwable> promise = new PromiseImpl<Void,Throwable>();
            registry.invoke(address, command("reject", false, failure), promise);
            super.reject(failure);
            promise.get();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChainImpl<Void> link(final Chain<?> that) {
        try {
            final PromiseImpl<Boolean,Throwable> promise = new PromiseImpl<Boolean,Throwable>();
            registry.invoke(address, command("link", false, new Serializable[]{ null }), promise);
            super.link(that);
            promise.get();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
