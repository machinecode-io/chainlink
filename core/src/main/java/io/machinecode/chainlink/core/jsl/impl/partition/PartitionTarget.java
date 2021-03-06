package io.machinecode.chainlink.core.jsl.impl.partition;

import io.machinecode.chainlink.spi.execution.Executable;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class PartitionTarget {
    private final Executable[] executables;
    private final int threads;

    public PartitionTarget(final Executable[] executables, final int threads) {
        this.executables = executables;
        this.threads = threads;
    }

    public Executable[] getExecutables() {
        return executables;
    }

    public int getThreads() {
        return threads;
    }
}
