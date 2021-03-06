package io.machinecode.chainlink.core.jsl.impl.transition;

import io.machinecode.chainlink.spi.jsl.transition.Stop;
import org.jboss.logging.Logger;

import javax.batch.runtime.BatchStatus;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class StopImpl extends TransitionImpl implements Stop {
    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(StopImpl.class);

    private final String exitStatus;
    private final String restart;

    public StopImpl(final String on, final String exitStatus, final String restart) {
        super(on);
        this.exitStatus = exitStatus;
        this.restart = restart;
    }

    @Override
    public String getExitStatus() {
        return this.exitStatus;
    }

    @Override
    public String getRestart() {
        return this.restart;
    }

    @Override
    public String element() {
        return Stop.ELEMENT;
    }

    @Override
    public BatchStatus getBatchStatus() {
        return BatchStatus.STOPPING;
    }

    @Override
    public String getNext() {
        return null;
    }

    @Override
    public String getRestartId() {
        return this.restart;
    }

    @Override
    public boolean isTerminating() {
        return true;
    }
}
