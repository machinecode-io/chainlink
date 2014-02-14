package io.machinecode.nock.test.core.execution;

import io.machinecode.nock.core.JobOperatorImpl;
import io.machinecode.nock.core.JobOperationImpl;
import io.machinecode.nock.core.factory.JobFactory;
import io.machinecode.nock.core.model.JobImpl;
import io.machinecode.nock.jsl.fluent.Jsl;
import io.machinecode.nock.test.core.execution.artifact.batchlet.FailBatchlet;
import io.machinecode.nock.test.core.execution.artifact.batchlet.InjectedBatchlet;
import io.machinecode.nock.test.core.execution.artifact.batchlet.RunBatchlet;
import io.machinecode.nock.test.core.execution.artifact.batchlet.StopBatchlet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import java.util.concurrent.CancellationException;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public abstract class ExecutorTest extends BaseTest {

    protected JobOperatorImpl operator;

    @Before
    public void before() {
        if (operator == null) {
            operator = new JobOperatorImpl(configuration(), executor());
        }
    }

    @Test
    public void runBatchletTest() throws Exception {
        printMethodName();
        final JobImpl job = JobFactory.INSTANCE.produceExecution(Jsl.job()
                .setId("run-job")
                .addExecution(
                        Jsl.stepWithBatchletAndPlan()
                                .setId("step")
                                .setTask(
                                        Jsl.batchlet()
                                                .setRef("run-batchlet")
                                )
                ), PARAMETERS);
        final JobOperationImpl operation = operator.startJob(job, "run-job", PARAMETERS);
        final JobExecution execution = repository().getJobExecution(operation.getJobExecutionId());
        operation.get();
        Assert.assertTrue("RunBatchlet hasn't run yet", RunBatchlet.hasRun.get());
        Assert.assertEquals("Batch Status", BatchStatus.COMPLETED, repository().getJobExecution(execution.getExecutionId()).getBatchStatus());
        Assert.assertEquals("Exit  Status", BatchStatus.COMPLETED.name(), repository().getJobExecution(execution.getExecutionId()).getExitStatus());
    }

    @Test
    public void stopBatchletTest() throws Exception {
        printMethodName();
        final JobImpl job = JobFactory.INSTANCE.produceExecution(Jsl.job()
                .setId("stop-job")
                .addExecution(
                        Jsl.stepWithBatchletAndPlan()
                                .setId("step")
                                .setTask(
                                        Jsl.batchlet()
                                                .setRef("stop-batchlet")
                                )
                ), PARAMETERS);
        final JobOperationImpl operation = operator.startJob(job, "stop-job", PARAMETERS);
        final JobExecution execution = repository().getJobExecution(operation.getJobExecutionId());
        Thread.sleep(100);
        Assert.assertTrue("StopBatchlet hasn't run yet", StopBatchlet.hasRun.get());
        operator.stop(operation.getJobExecutionId());
        //Assert.assertTrue("Operation hasn't been cancelled", operation.isDone()); //Some wierdness in AllDeferred's done/Cancelled
        try {
            operation.get();
        } catch (final CancellationException e) {
            //
        }
        Assert.assertTrue("StopBatchlet hasn't stopped yet", StopBatchlet.hasStopped.get());
        Assert.assertEquals("Batch Status", BatchStatus.STOPPED, repository().getJobExecution(execution.getExecutionId()).getBatchStatus());
        Assert.assertEquals("Exit  Status", BatchStatus.STOPPED.name(), repository().getJobExecution(execution.getExecutionId()).getExitStatus());
    }

    @Test
    public void failBatchletTest() throws Exception {
        printMethodName();
        final JobImpl job = JobFactory.INSTANCE.produceExecution(Jsl.job()
                .setId("fail-job")
                .addExecution(
                        Jsl.stepWithBatchletAndPlan()
                                .setId("step")
                                .setTask(
                                        Jsl.batchlet()
                                                .setRef("fail-batchlet")
                                )
                ), PARAMETERS);
        final JobOperationImpl operation = operator.startJob(job, "fail-job", PARAMETERS);
        final JobExecution execution = repository().getJobExecution(operation.getJobExecutionId());
        operation.get();
        Assert.assertTrue("FailBatchlet hasn't run yet", FailBatchlet.hasRun.get());
        Assert.assertEquals("Batch Status", BatchStatus.FAILED, repository().getJobExecution(execution.getExecutionId()).getBatchStatus());
        Assert.assertEquals("Exit  Status", BatchStatus.FAILED.name(), repository().getJobExecution(execution.getExecutionId()).getExitStatus());
    }

    @Test
    public void injectedBatchletTest() throws Exception {
        printMethodName();
        final JobImpl job = JobFactory.INSTANCE.produceExecution(Jsl.job()
                .setId("injected-job")
                .addExecution(
                        Jsl.stepWithBatchletAndPlan()
                                .setId("step")
                                .setTask(
                                        Jsl.batchlet()
                                                .setRef("injected-batchlet")
                                                .addProperty("property", "value")
                                )
                ), PARAMETERS);
        final JobOperationImpl operation = operator.startJob(job, "injected-job", PARAMETERS);
        final JobExecution execution = repository().getJobExecution(operation.getJobExecutionId());
        operation.get();
        Assert.assertTrue("InjectedBatchlet hasn't run yet", InjectedBatchlet.hasRun.get());
        Assert.assertEquals("Batch Status", BatchStatus.COMPLETED, repository().getJobExecution(execution.getExecutionId()).getBatchStatus());
        Assert.assertEquals("Exit  Status", BatchStatus.COMPLETED.name(), repository().getJobExecution(execution.getExecutionId()).getExitStatus());
    }
}
