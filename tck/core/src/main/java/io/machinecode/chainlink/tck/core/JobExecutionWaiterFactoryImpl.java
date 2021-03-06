package io.machinecode.chainlink.tck.core;

import com.ibm.jbatch.tck.spi.JobExecutionWaiter;
import com.ibm.jbatch.tck.spi.JobExecutionWaiterFactory;

import javax.batch.operations.JobOperator;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class JobExecutionWaiterFactoryImpl implements JobExecutionWaiterFactory {

    @Override
    public JobExecutionWaiter createWaiter(final long executionId, final JobOperator operator, final long timeout) {
        return new JobExecutionWaiterImpl(executionId, operator, timeout);
    }
}
