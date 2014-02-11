package io.machinecode.nock.spi.context;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.Metric.MetricType;
import javax.batch.runtime.context.StepContext;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public interface MutableStepContext extends StepContext {

    void setException(Exception exception);

    void setBatchStatus(BatchStatus batchStatus);

    String getBatchletStatus();

    void setBatchletStatus(String batchletStatus);

    MutableMetric getMetric(MetricType type);
}