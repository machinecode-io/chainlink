package io.machinecode.chainlink.core.jsl.impl.transition;

import io.machinecode.chainlink.spi.jsl.transition.End;
import org.jboss.logging.Logger;

import javax.batch.runtime.BatchStatus;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class EndImpl extends TransitionImpl implements End {
    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(EndImpl.class);

    private final String exitStatus;

    public EndImpl(final String on, final String exitStatus) {
        super(on);
        this.exitStatus = exitStatus;
    }

    @Override
    public String getExitStatus() {
        return this.exitStatus;
    }

    @Override
    public String element() {
        return End.ELEMENT;
    }

    @Override
    public BatchStatus getBatchStatus() {
        return BatchStatus.COMPLETED;
    }

    @Override
    public String getNext() {
        return null;
    }

    @Override
    public String getRestartId() {
        return null;
    }

    @Override
    public boolean isTerminating() {
        return true;
    }
}
