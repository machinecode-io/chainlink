package io.machinecode.chainlink.test.cdi.producer;

import io.machinecode.chainlink.core.execution.artifact.batchlet.ErrorBatchlet;
import io.machinecode.chainlink.core.execution.artifact.batchlet.FailBatchlet;
import io.machinecode.chainlink.core.execution.artifact.batchlet.FailProcessBatchlet;
import io.machinecode.chainlink.core.execution.artifact.batchlet.FailStopBatchlet;
import io.machinecode.chainlink.core.execution.artifact.batchlet.InjectedBatchlet;
import io.machinecode.chainlink.core.execution.artifact.batchlet.OverrideBatchlet;
import io.machinecode.chainlink.core.execution.artifact.batchlet.RunBatchlet;
import io.machinecode.chainlink.core.execution.artifact.batchlet.StopBatchlet;

import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
*/
public class BatchletProducer {

    @Produces
    @Named("injectedBatchlet")
    public javax.batch.api.Batchlet batchlet(@New InjectedBatchlet batchlet) {
        return batchlet;
    }

    @Produces
    @Named("unmanagedInjectedBatchlet")
    public javax.batch.api.Batchlet unmanaged() {
        return new InjectedBatchlet();
    }

    @Produces
    @Named("failBatchlet")
    public javax.batch.api.Batchlet batchlet(@New FailBatchlet batchlet) {
        return batchlet;
    }

    @Produces
    @Named("errorBatchlet")
    public javax.batch.api.Batchlet batchlet(@New ErrorBatchlet batchlet) {
        return batchlet;
    }

    @Produces
    @Named("failStopBatchlet")
    public javax.batch.api.Batchlet batchlet(@New FailStopBatchlet batchlet) {
        return batchlet;
    }

    @Produces
    @Named("failProcessBatchlet")
    public javax.batch.api.Batchlet batchlet(@New FailProcessBatchlet batchlet) {
        return batchlet;
    }

    @Produces
    @Named("stopBatchlet")
    public javax.batch.api.Batchlet batchlet(@New StopBatchlet batchlet) {
        return batchlet;
    }

    @Produces
    @Named("runBatchlet")
    public javax.batch.api.Batchlet batchlet(@New RunBatchlet batchlet) {
        return batchlet;
    }

    @Produces
    @Named("overrideBatchlet")
    public javax.batch.api.Batchlet batchlet(@New OverrideBatchlet batchlet) {
        return batchlet;
    }
}
