package io.machinecode.nock.core.work;

import io.machinecode.nock.spi.execution.Item;

import javax.batch.runtime.BatchStatus;
import java.io.Serializable;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class ItemImpl implements Item {
    private final Serializable data;
    private final BatchStatus batchStatus;
    private final String exitStatus;

    public ItemImpl(final Serializable data, final BatchStatus batchStatus, final String exitStatus) {
        this.data = data;
        this.batchStatus = batchStatus;
        this.exitStatus = exitStatus;
    }

    @Override
    public Serializable getData() {
        return data;
    }

    @Override
    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    @Override
    public String getExitStatus() {
        return exitStatus;
    }
}