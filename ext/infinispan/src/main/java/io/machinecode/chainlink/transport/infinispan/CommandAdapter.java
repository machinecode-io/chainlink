package io.machinecode.chainlink.transport.infinispan;

import io.machinecode.chainlink.spi.configuration.Configuration;
import io.machinecode.chainlink.core.transport.cmd.Command;
import io.machinecode.chainlink.transport.infinispan.configuration.ChainlinkCommand;
import org.infinispan.commands.remote.BaseRpcCommand;
import org.infinispan.context.InvocationContext;
import org.infinispan.remoting.transport.Address;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
public class CommandAdapter extends BaseRpcCommand implements ChainlinkCommand {

    public static final byte COMMAND_ID_61 = 61;

    private Address origin;
    private Command<?> command;

    private transient Configuration configuration;

    public CommandAdapter(final String cacheName) {
        super(cacheName);
    }

    public CommandAdapter(final String cacheName, final Command<?> command, final Address origin) {
        super(cacheName);
        this.command = command;
        this.origin = origin;
    }

    @Override
    public void init(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Object perform(final InvocationContext ctx) throws Throwable {
        return command.perform(configuration, origin);
    }

    @Override
    public byte getCommandId() {
        return COMMAND_ID_61;
    }

    @Override
    public Object[] getParameters() {
        return new Object[] { command, origin };
    }

    @Override
    public void setParameters(final int commandId, final Object[] parameters) {
        if (commandId != getCommandId()) throw new IllegalStateException(); //TODO Message
        this.command = (Command<?>)parameters[0];
        this.origin = (Address)parameters[1];
    }

    @Override
    public boolean isReturnValueExpected() {
        return true;
    }

    @Override
    public boolean canBlock() {
        return false;
    }
}
