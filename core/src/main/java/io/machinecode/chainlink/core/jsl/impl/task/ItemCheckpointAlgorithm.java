package io.machinecode.chainlink.core.jsl.impl.task;

import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.configuration.Configuration;
import io.machinecode.chainlink.spi.context.ExecutionContext;

/**
* @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
*/
final class ItemCheckpointAlgorithm extends CheckpointAlgorithmImpl {
    private static final long serialVersionUID = 1L;

    final int timeout;
    final int target;
    int current;

    public ItemCheckpointAlgorithm(final int timeout, final int target) {
        super(null, null);
        this.timeout = timeout;
        this.target = target;
    }

    @Override
    public int checkpointTimeout(final Configuration configuration, final ExecutionContext context) throws Exception {
        return timeout;
    }

    @Override
    public void beginCheckpoint(final Configuration configuration, final ExecutionContext context) throws Exception {
        //
    }

    @Override
    public boolean isReadyToCheckpoint(final Configuration configuration, final ExecutionContext context) throws Exception {
        if (current > target) {
            throw new IllegalStateException(Messages.format("CHAINLINK-030000.item.checkpoint", current, target));
        }
        return target == ++current;
    }

    @Override
    public void endCheckpoint(final Configuration configuration, final ExecutionContext context) throws Exception {
        current = 0;
    }
}
