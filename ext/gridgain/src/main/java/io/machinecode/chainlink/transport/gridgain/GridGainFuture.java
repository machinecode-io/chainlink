package io.machinecode.chainlink.transport.gridgain;

import org.gridgain.grid.GridException;
import org.gridgain.grid.GridFuture;
import org.gridgain.grid.GridFutureTimeoutException;
import org.gridgain.grid.GridInterruptedException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class GridGainFuture<T> implements Future<T> {

    final GridFuture<T> future;

    public GridGainFuture(final GridFuture<T> future) {
        this.future = future;
    }
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        try {
            return future.cancel();
        } catch (final GridException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        try {
            return future.get();
        } catch (final GridInterruptedException e) {
            final InterruptedException ex = new InterruptedException();
            ex.addSuppressed(e);
            throw ex;
        } catch (final GridException e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            return future.get(timeout, unit);
        } catch (final GridInterruptedException e) {
            final InterruptedException ex = new InterruptedException();
            ex.addSuppressed(e);
            throw ex;
        } catch (final GridFutureTimeoutException e) {
            final TimeoutException ex = new TimeoutException();
            ex.addSuppressed(e);
            throw ex;
        } catch (final GridException e) {
            throw new ExecutionException(e);
        }
    }
}