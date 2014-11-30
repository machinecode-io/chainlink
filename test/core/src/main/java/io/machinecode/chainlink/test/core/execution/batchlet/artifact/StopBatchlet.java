package io.machinecode.chainlink.test.core.execution.batchlet.artifact;

import io.machinecode.chainlink.test.core.execution.Reference;

import javax.batch.runtime.BatchStatus;

/**
* @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
*/
public class StopBatchlet extends javax.batch.api.AbstractBatchlet {
    public static Reference<Boolean> hasStopped = new Reference<Boolean>(false);
    @Override
    public String process() throws Exception {
        synchronized (this) {
            wait();
        }
        return BatchStatus.COMPLETED.toString();
    }

    @Override
    public void stop() throws Exception {
        hasStopped.set(true);
        synchronized (this) {
            notifyAll();
        }
    }
}
