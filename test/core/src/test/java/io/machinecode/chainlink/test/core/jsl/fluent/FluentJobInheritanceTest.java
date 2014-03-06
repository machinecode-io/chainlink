package io.machinecode.chainlink.test.core.jsl.fluent;

import io.machinecode.chainlink.jsl.fluent.Jsl;
import io.machinecode.chainlink.jsl.fluent.loader.FluentJobLoader;
import io.machinecode.chainlink.test.core.jsl.InheritanceJobTest;

import javax.xml.bind.JAXBException;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class FluentJobInheritanceTest extends InheritanceJobTest {

    @Override
    protected FluentJobLoader createRepo() {
        return new FluentJobLoader()
                .add(
                        "job-config-1",
                        Jsl.job()
                                .setId("i1")
                                .setVersion("1.0")
                                .setRestartable("true")
                                .addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step1")
                                        .setAbstract(true)
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit")
                                        )
                                )
                ).add(
                        "job-default-1",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addProperty("job-prop", "job-value")
                                .addListener(Jsl.listener()
                                        .setRef("something")
                                )
                                .addExecution(Jsl.stepWithChunkAndPlan()
                                        .setId("step1")
                                        .setTask(Jsl.chunk()
                                                .setReader(Jsl.reader()
                                                        .setRef("Reader2")
                                                        .addProperty("", "")
                                                )
                                                .setProcessor(Jsl.processor()
                                                        .setRef("Processor2")
                                                )
                                                .setWriter(Jsl.writer()
                                                        .setRef("PostingWriter")
                                                        .addProperty("Writer2","")
                                                )
                                                .setCheckpointAlgorithm(Jsl.checkpointAlgorithm()
                                                        .setRef("other-checkpoint-algorithm")
                                                )
                                                .setSkippableExceptionClasses(Jsl.skippableExceptionClasses()
                                                        .addInclude("java.lang.Exception")
                                                        .addExclude(Throwable.class)
                                                        .addExclude("javax.xml.bind.JAXBException")
                                                ).setRetryableExceptionClasses(Jsl.retryableExceptionClasses()
                                                        .addInclude("java.lang.Exception")
                                                        .addExclude("java.lang.Throwable")
                                                        .addExclude(JAXBException.class)
                                                ).setNoRollbackExceptionClasses(Jsl.noRollbackExceptionClasses()
                                                        .addInclude(Exception.class)
                                                        .addExclude("java.lang.Throwable")
                                                        .addExclude("javax.xml.bind.JAXBException")
                                                )
                                        ).setPartition(Jsl.partitionWithPlan()
                                                .setStrategy(Jsl.plan()
                                                )
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step2")
                                )
                ).add(
                        "job-merge-1",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step1")
                                        .setAbstract(true)
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit")
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndMapper()
                                        .setId("step2")
                                        .setAbstract(true)
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit2")
                                        )
                                ).addExecution(Jsl.stepWithChunkAndPlan()
                                        .setId("s1")
                                        .setParent("step1")
                                        .setNext("s2")
                                ).addExecution(Jsl.stepWithChunkAndMapper()
                                        .setId("s2")
                                        .setParent("step2")
                                )
                ).add(
                        "job-merge-1-result",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s1")
                                        .setNext("s2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit")
                                        )
                                )
                                .addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit2")
                                        )
                                )
                ).add(
                        "job-merge-2",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step1")
                                        .setAbstract(true)
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit")
                                        )
                                )
                                .addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step2")
                                        .setAbstract(true)
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit2")
                                        )
                                )
                                .addExecution(Jsl.flow()
                                        .setId("flow1")
                                        .setAbstract(true)
                                        .addExecution(Jsl.stepWithBatchletAndPlan()
                                                .setId("s1")
                                                .setParent("step1")
                                                .setAllowStartIfComplete("true")
                                                .setNext("s2")
                                        )
                                        .addExecution(Jsl.stepWithBatchletAndPlan()
                                                .setId("s2")
                                                .setParent("step2")
                                        )
                                ).addExecution(Jsl.flow()
                                        .setId("f1")
                                        .setParent("flow1")
                                        .addTransition(Jsl.stop()
                                                .setOn("ERROR")
                                                .setRestart("s1")
                                        )
                                )
                ).add(
                        "job-merge-2-result",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addExecution(Jsl.flow()
                                        .setId("f1")
                                        .addExecution(Jsl.stepWithBatchletAndPlan()
                                                .setId("s1")
                                                .setAllowStartIfComplete("true")
                                                .setNext("s2")
                                                .setTask(Jsl.batchlet()
                                                        .setRef("Doit")
                                                )
                                        )
                                        .addExecution(Jsl.stepWithBatchletAndPlan()
                                                .setId("s2")
                                                .setTask(Jsl.batchlet()
                                                        .setRef("Doit2")
                                                )
                                        ).addTransition(Jsl.stop()
                                                .setOn("ERROR")
                                                .setRestart("s1")
                                        )
                                )

                ).add(
                        "job-merge-3-1",
                        Jsl.job()
                                .setId("parent1")
                                .setVersion("1.0")
                                .setAbstract(true)
                                .addListener(Jsl.listener()
                                        .setRef("StepAuditor")
                                )
                ).add(
                        "job-merge-3-2",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .setParent("parent1")
                                .setJslName("job-merge-3-1")
                                .addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s1")
                                        .setNext("s2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit")
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit2")
                                        )

                                )
                ).add(
                        "job-merge-3-result",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addListener(Jsl.listener()
                                        .setRef("StepAuditor")
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s1")
                                        .setNext("s2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit")
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit2")
                                        )

                                )
                ).add(
                        "job-merge-4-1",
                        Jsl.job()
                                .setId("parent1")
                                .setVersion("1.0")
                                .addListener(Jsl.listener()
                                        .setRef("StepAuditor")
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step1")
                                        .setNext("step2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit")
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit2")
                                        )

                                )
                ).add(
                        "job-merge-4-2",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s1")
                                        .setParent("step1")
                                        .setJslName("job-merge-4-1")
                                        .setNext("s2")
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s2")
                                        .setParent("step2")
                                        .setJslName("job-merge-4-1")
                                )
                ).add(
                        "job-merge-4-result",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s1")
                                        .setNext("s2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit")
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit2")
                                        )
                                )
                ).add(
                        "job-merge-5-1",
                        Jsl.job()
                                .setId("parent1")
                                .setVersion("1.0")
                                .addListener(Jsl.listener()
                                        .setRef("StepAuditor")
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step1")
                                        .setNext("step2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit")
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit2")
                                        )
                                )
                ).add(
                        "job-merge-5-2",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .setParent("parent1")
                                .setJslName("job-merge-5-1")
                                .addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s1")
                                        .setParent("step1")
                                        .setJslName("job-merge-5-1")
                                        .setNext("s2")
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s2")
                                        .setParent("step2")
                                        .setJslName("job-merge-5-1")
                                )
                ).add(
                        "job-merge-5-result",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .setParent("parent1")
                                .setJslName("job-merge-5-1")
                                .addListener(Jsl.listener()
                                        .setRef("StepAuditor")
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s1")
                                        .setNext("s2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit")
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s2")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit2")
                                        )
                                )
                ).add(
                        "job-merge-6-1",
                        Jsl.job()
                                .setId("parent1")
                                .setVersion("1.0")
                                .addListener(Jsl.listener()
                                        .setRef("StepAuditor")
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step1")
                                        .setAbstract(true)
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit")
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step2")
                                        .setAbstract(true)
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit2")
                                        )
                                ).addExecution(Jsl.flow()
                                        .setId("flow1")
                                        .setNext("step3")
                                        .addExecution(Jsl.stepWithBatchletAndPlan()
                                                .setId("s1")
                                                .setParent("step1")
                                                .setAllowStartIfComplete("true")
                                                .setNext("s2")
                                        ).addExecution(Jsl.stepWithBatchletAndPlan()
                                                .setId("s2")
                                                .setParent("step2")
                                        ).addTransition(Jsl.stop()
                                                .setOn("ERROR")
                                                .setRestart("step3")
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("step3")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit3")
                                        )
                                )
                ).add(
                        "job-merge-6-2",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addExecution(Jsl.flow()
                                        .setId("f1")
                                        .setParent("flow1")
                                        .setJslName("job-merge-6-1")
                                        .setNext("s3")
                                        .addTransition(Jsl.fail()
                                                .setOn("ERROR")
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s3")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit3")
                                        )
                                )
                ).add(
                        "job-merge-6-result",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addExecution(Jsl.flow()
                                        .setId("f1")
                                        .setNext("s3")
                                        .addExecution(Jsl.stepWithBatchletAndPlan()
                                                .setId("s1")
                                                .setAllowStartIfComplete("true")
                                                .setNext("s2")
                                                .setTask(Jsl.batchlet()
                                                        .setRef("Doit")
                                                )
                                        )
                                        .addExecution(Jsl.stepWithBatchletAndPlan()
                                                .setId("s2")
                                                .setTask(Jsl.batchlet()
                                                        .setRef("Doit2")
                                                )
                                        )
                                        .addTransition(Jsl.fail()
                                                .setOn("ERROR")
                                        )
                                ).addExecution(Jsl.stepWithBatchletAndPlan()
                                        .setId("s3")
                                        .setTask(Jsl.batchlet()
                                                .setRef("Doit3")
                                        )
                                )
                ).add(
                        "job-merge-7",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addExecution(Jsl.stepWithChunkAndPlan()
                                        .setId("step1")
                                        .setAbstract(true)
                                        .addProperty("debug","false")
                                        .setTask(Jsl.chunk()
                                                .setItemCount("50")
                                                .setReader(Jsl.reader()
                                                        .setRef("PostingReader")
                                                        .addProperty("infile","#{jobProperties['step1.infile']}?:in.txt")
                                                ).setProcessor(Jsl.processor()
                                                        .setRef("PostingProcessing")
                                                ).setWriter(Jsl.writer()
                                                        .setRef("PostingWriter")
                                                        .addProperty("outfile","#{jobProperties['step1.outfile']}?:out.txt")
                                                )
                                        )
                                ).addExecution(Jsl.stepWithChunkAndPlan()
                                        .setId("s1")
                                        .setParent("step1")
                                        .setProperties(Jsl.properties()
                                                .setMerge(true)
                                                .addProperty("debug","true")
                                                .addProperty("step1.infile","postings.out.txt")
                                                .addProperty("step1.outfile","postings.out.txt")
                                        )
                                        .setTask(Jsl.chunk()
                                                .setItemCount("100")
                                        )
                                )
                ).add(
                        "job-merge-7-result",
                        Jsl.job()
                                .setId("job1")
                                .setVersion("1.0")
                                .addExecution(Jsl.stepWithChunkAndPlan()
                                        .setId("s1")
                                        .addProperty("debug","false")
                                        .addProperty("debug","true")
                                        .addProperty("step1.infile","postings.out.txt")
                                        .addProperty("step1.outfile","postings.out.txt")
                                        .setTask(Jsl.chunk()
                                                .setItemCount("100")
                                                .setReader(Jsl.reader()
                                                        .setRef("PostingReader")
                                                        .addProperty("infile","#{jobProperties['step1.infile']}?:in.txt")
                                                ).setProcessor(Jsl.processor()
                                                        .setRef("PostingProcessing")
                                                ).setWriter(Jsl.writer()
                                                        .setRef("PostingWriter")
                                                        .addProperty("outfile","#{jobProperties['step1.outfile']}?:out.txt")
                                                )
                                        )
                                )
                );
    }
}
