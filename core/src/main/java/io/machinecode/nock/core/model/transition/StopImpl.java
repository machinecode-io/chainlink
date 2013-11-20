package io.machinecode.nock.core.model.transition;

import io.machinecode.nock.spi.element.transition.Stop;
import org.jboss.logging.Logger;

import javax.batch.runtime.BatchStatus;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class StopImpl extends TransitionImpl implements Stop {

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
    public Result runTransition(final String id) throws Exception {
        return Result.status(BatchStatus.STOPPED, this.exitStatus, this.restart == null ? id : this.restart);
    }

    @Override
    public String element() {
        return Stop.ELEMENT;
    }
}
