package io.machinecode.nock.core.work.execution;

import io.machinecode.nock.core.work.DeferredImpl;
import io.machinecode.nock.core.work.ExecutableImpl;
import io.machinecode.nock.spi.context.Context;
import io.machinecode.nock.spi.transport.Transport;
import io.machinecode.nock.spi.work.Deferred;
import io.machinecode.nock.spi.work.ExecutionWork;

/**
* Brent Douglas <brent.n.douglas@gmail.com>
*/
public class RunExecution extends ExecutableImpl {
    final ExecutionWork work;
    final Context context;

    public RunExecution(final ExecutionWork work, final Context context) {
        this.work = work;
        this.context = context;
    }

    @Override
    public Deferred run(final Transport transport) throws Exception {
        return new DeferredImpl(
                work.before(transport, context),
                work.run(transport, context)
        );
    }
}