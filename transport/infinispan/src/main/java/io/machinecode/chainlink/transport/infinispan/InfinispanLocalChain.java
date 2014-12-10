package io.machinecode.chainlink.transport.infinispan;

import io.machinecode.chainlink.spi.registry.ChainId;
import io.machinecode.chainlink.spi.transport.Transport;
import io.machinecode.chainlink.transport.core.DistributedLocalChain;
import io.machinecode.chainlink.transport.core.cmd.InvokeChainCommand;
import org.infinispan.remoting.transport.Address;

import java.io.Serializable;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class InfinispanLocalChain extends DistributedLocalChain<Address> {

    public InfinispanLocalChain(final Transport<Address> transport, final Address address, final long jobExecutionId, final ChainId chainId) {
        super(transport, address, jobExecutionId, chainId);
    }

    @Override
    protected <T> InvokeChainCommand<T, Address> command(final String name, final Serializable... params) {
        return new InvokeChainCommand<>(jobExecutionId, chainId, name, params);
    }
}
