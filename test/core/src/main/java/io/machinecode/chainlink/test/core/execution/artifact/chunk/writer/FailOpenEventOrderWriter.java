package io.machinecode.chainlink.test.core.execution.artifact.chunk.writer;

import io.machinecode.chainlink.test.core.execution.artifact.chunk.ChunkEvent;
import io.machinecode.chainlink.test.core.execution.artifact.chunk.EventOrderAccumulator;
import io.machinecode.chainlink.test.core.execution.artifact.chunk.exception.FailWriteOpenException;
import junit.framework.Assert;

import javax.batch.api.chunk.ItemWriter;
import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com>Brent Douglas</a>
 * @since 1.0
 */
public class FailOpenEventOrderWriter implements ItemWriter {

    @Override
    public void open(final Serializable checkpoint) throws Exception {
        EventOrderAccumulator._order.add(ChunkEvent.WRITER_OPEN);
        throw new FailWriteOpenException();
    }

    @Override
    public void close() throws Exception {
        EventOrderAccumulator._order.add(ChunkEvent.WRITER_CLOSE);
    }

    @Override
    public void writeItems(final List<Object> items) throws Exception {
        Assert.fail();
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        Assert.fail();
        return null;
    }
}