package io.machinecode.chainlink.test.core.execution.chunk.artifact.reader;

import io.machinecode.chainlink.test.core.execution.chunk.artifact.ChunkEvent;
import io.machinecode.chainlink.test.core.execution.chunk.artifact.EventOrderAccumulator;
import io.machinecode.chainlink.test.core.execution.chunk.artifact.exception.FailReadException;

import javax.batch.api.chunk.ItemReader;
import java.io.Serializable;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com>Brent Douglas</a>
 * @since 1.0
 */
public class FailReadEventOrderReader implements ItemReader {

    private int count = 0;

    @Override
    public void open(final Serializable checkpoint) throws Exception {
        EventOrderAccumulator._order.add(ChunkEvent.READER_OPEN);
    }

    @Override
    public void close() throws Exception {
        EventOrderAccumulator._order.add(ChunkEvent.READER_CLOSE);
    }

    @Override
    public Object readItem() throws Exception {
        EventOrderAccumulator._order.add(ChunkEvent.READ);
        if (count == 1) {
            throw new FailReadException();
        } else {
            ++count;
            return new Object();
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        EventOrderAccumulator._order.add(ChunkEvent.READER_CHECKPOINT);
        return null;
    }
}