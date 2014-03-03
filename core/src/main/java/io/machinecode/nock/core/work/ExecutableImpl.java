package io.machinecode.nock.core.work;

import io.machinecode.nock.core.deferred.DeferredImpl;
import io.machinecode.nock.spi.context.ExecutionContext;
import io.machinecode.nock.spi.context.ThreadId;
import io.machinecode.nock.spi.deferred.Deferred;
import io.machinecode.nock.spi.execution.Executable;
import io.machinecode.nock.spi.execution.Executor;
import io.machinecode.nock.spi.util.Messages;
import io.machinecode.nock.spi.work.Work;

/**
* @author Brent Douglas <brent.n.douglas@gmail.com>
*/
public abstract class ExecutableImpl<T extends Work> extends DeferredImpl<Deferred<?>> implements Executable {

    protected final T work;
    protected final ThreadId threadId;
    protected final Executable parent;
    protected final ExecutionContext context;

    public ExecutableImpl(final Executable parent, final ExecutionContext context, final T work, final ThreadId threadId) {
        super(new Deferred[1]);
        this.parent = parent;
        this.context = context;
        this.work = work;
        this.threadId = threadId;
    }

    public ExecutableImpl(final ExecutableImpl<T> executable, final ThreadId threadId) {
        this(executable.getParent(), executable.getContext(), executable.work, threadId);
    }

    @Override
    public boolean isCancelled() {
        synchronized (lock) {
            return super.isCancelled() || (this.parent != null && this.parent.isCancelled());
        }
    }

    @Override
    public Executable getParent() {
        return parent;
    }

    @Override
    public ExecutionContext getContext() {
        return context;
    }

    @Override
    public ThreadId getThreadId() {
        return threadId;
    }

    @Override
    public void execute(final Executor executor, final ThreadId threadId, final Executable callback,
                        final ExecutionContext context) {
        try {
            log().tracef(Messages.get("NOCK-015703.executable.execute"), this.context, this);
            final Deferred<?> next = doExecute(executor, threadId, callback, context);
            // null means that it was an incomplete partition
            if (next == null) {
                resolve(null); //Need to call notify listener.
                return;
            }
            resolve(setChild(0, next));
        } catch (final Throwable e) {
            log().errorf(e, Messages.get("NOCK-015704.executable.exception"), this.context, this);
            reject(e);
        }
    }

    @Override
    protected String getResolveLogMessage() {
        return Messages.format("NOCK-015100.executable.resolve", this.context, this);
    }

    @Override
    protected String getRejectLogMessage() {
        return Messages.format("NOCK-015101.executable.reject", this.context, this);
    }

    @Override
    protected String getCancelLogMessage() {
        return Messages.format("NOCK-015102.executable.cancel", this.context, this);
    }

    @Override
    protected String getTimeoutExceptionMessage() {
        return Messages.format("NOCK-015000.executable.timeout", this.context, this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[threadId=" + threadId + ",work=" + work + "]";
    }

    protected abstract Deferred<?> doExecute(final Executor executor, final ThreadId threadId, final Executable callback,
                                             final ExecutionContext context) throws Throwable;
}
