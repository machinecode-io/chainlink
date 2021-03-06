package javax.batch.api.chunk;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public interface CheckpointAlgorithm {

    int checkpointTimeout() throws Exception;

    void beginCheckpoint() throws Exception;

    boolean isReadyToCheckpoint() throws Exception;

    void endCheckpoint() throws Exception;
}
