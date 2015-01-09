package io.machinecode.chainlink.core.execution.chunk.artifact.writer;

import io.machinecode.chainlink.core.execution.chunk.artifact.ChunkEvent;
import io.machinecode.chainlink.core.execution.chunk.artifact.EventOrderAccumulator;
import io.machinecode.chainlink.core.execution.chunk.artifact.exception.FailWriteCloseException;

import javax.batch.api.chunk.ItemWriter;
import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class FailCloseEventOrderWriter implements ItemWriter {

    @Override
    public void open(final Serializable checkpoint) throws Exception {
        EventOrderAccumulator._order.add(ChunkEvent.WRITER_OPEN);
    }

    @Override
    public void close() throws Exception {
        EventOrderAccumulator._order.add(ChunkEvent.WRITER_CLOSE);
        throw new FailWriteCloseException();
    }

    @Override
    public void writeItems(final List<Object> items) throws Exception {
        EventOrderAccumulator._order.add(ChunkEvent.WRITE);
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        EventOrderAccumulator._order.add(ChunkEvent.WRITER_CHECKPOINT);
        return null;
    }
}