package io.machinecode.chainlink.spi.work;

import io.machinecode.chainlink.spi.context.ExecutionContext;
import io.machinecode.chainlink.spi.deferred.Deferred;
import io.machinecode.chainlink.spi.element.task.Task;
import io.machinecode.chainlink.spi.execution.Executor;
import io.machinecode.chainlink.spi.expression.PropertyContext;

import java.io.Serializable;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public interface TaskWork extends Task, Work, Deferred<ExecutionContext>, Serializable {

    TaskWork partition(PropertyContext context);

    void run(final Executor executor, final ExecutionContext context, final int timeout) throws Exception;
}
