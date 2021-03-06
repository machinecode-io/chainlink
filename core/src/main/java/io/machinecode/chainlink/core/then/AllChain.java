package io.machinecode.chainlink.core.then;

import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.then.Chain;
import io.machinecode.chainlink.spi.then.OnLink;
import io.machinecode.then.api.Promise;
import org.jboss.logging.Logger;

import java.util.Collection;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
public class AllChain<T> extends BaseChain<T> {

    private static final Logger log = Logger.getLogger(AllChain.class);

    protected final Chain<?>[] link;

    public AllChain(final Chain<?>... link) {
        this.link = link;
        for (final Chain<?> that : link) {
            that.previous(this);
        }
        resolve(null);
    }

    public AllChain(final Collection<? extends Chain<?>> link) {
        this(link.toArray(new Chain[link.size()]));
    }

    @Override
    public void previous(final Chain<?> that) {
        synchronized (lock) {
            this.previous = that;
            lock.notifyAll();
        }
        that.notifyLinked();
    }

    @Override
    public boolean isDone() {
        boolean done = true;
        for (final Promise<?,Throwable,?> that : link) {
            if (that == null) {
                continue;
            }
            done = that.isDone() && done;
        }
        return done;
    }

    @Override
    public AllChain<T> onLink(final OnLink then) {
        if (then == null) {
            throw new IllegalArgumentException(Messages.format("CHAINLINK-004200.chain.argument.required", "onLink"));
        }
        RuntimeException exception = null;
        for (final Chain<?> that : link) {
            try {
                then.link(that);
            } catch (final Throwable e) {
                if (exception == null) {
                    exception = new RuntimeException(Messages.format("CHAINLINK-004002.chain.get.exception"), e);
                } else {
                    exception.addSuppressed(e);
                }
            }
        }
        if (exception != null) {
            log().warnf(exception, Messages.format("CHAINLINK-004002.chain.get.exception"));
            throw exception;
        }
        return this;
    }

    @Override
    public void awaitLink() throws InterruptedException, ExecutionException, CancellationException {
        CancellationException ce = null;
        ExecutionException ee = null;
        for (final Chain<?> chain : link) {
            try {
                chain.get();
            } catch (final ExecutionException e) {
                if (ee == null) {
                    ee = e;
                } else {
                    ee.addSuppressed(e);
                }
            } catch (final CancellationException e) {
                if (ce == null) {
                    ce = e;
                } else {
                    ce.addSuppressed(e);
                }
            } catch (final Exception e) {
                if (ee == null) {
                    ee = new ExecutionException(e);
                } else {
                    ee.addSuppressed(e);
                }
            }
        }
        if (ee != null) {
            if (ce != null) {
                ee.addSuppressed(ce);
            }
            throw ee;
        } else if (ce != null) {
            throw ce;
        }
    }

    @Override
    public void awaitLink(final long timeout, final TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
        final long end = System.currentTimeMillis() + unit.toMillis(timeout);
        CancellationException ce = null;
        ExecutionException ee = null;
        for (final Chain<?> chain : link) {
            try {
                chain.get(_tryTimeout(end), MILLISECONDS);
            } catch (final ExecutionException e) {
                if (ee == null) {
                    ee = e;
                } else {
                    ee.addSuppressed(e);
                }
            } catch (final CancellationException e) {
                if (ce == null) {
                    ce = e;
                } else {
                    ce.addSuppressed(e);
                }
            } catch (final Exception e) {
                if (ee == null) {
                    ee = new ExecutionException(e);
                } else {
                    ee.addSuppressed(e);
                }
            }
        }
        if (ee != null) {
            if (ce != null) {
                ee.addSuppressed(ce);
            }
            throw ee;
        } else if (ce != null) {
            throw ce;
        }
    }

    @Override
    public void link(final Chain<?> that) {
        // These should always be provided when being constructed
        throw new IllegalStateException(); //TODO Message
    }

    @Override
    protected Logger log() {
        return log;
    }
}
