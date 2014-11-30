package io.machinecode.chainlink.jsl.fluent;

import io.machinecode.chainlink.jsl.fluent.execution.FluentDecision;
import io.machinecode.chainlink.jsl.fluent.execution.FluentFlow;
import io.machinecode.chainlink.jsl.fluent.execution.FluentSplit;
import io.machinecode.chainlink.jsl.fluent.execution.FluentStep;
import io.machinecode.chainlink.jsl.fluent.partition.FluentAnalyser;
import io.machinecode.chainlink.jsl.fluent.partition.FluentCollector;
import io.machinecode.chainlink.jsl.fluent.partition.FluentMapper;
import io.machinecode.chainlink.jsl.fluent.partition.FluentPartition;
import io.machinecode.chainlink.jsl.fluent.partition.FluentPlan;
import io.machinecode.chainlink.jsl.fluent.partition.FluentReducer;
import io.machinecode.chainlink.jsl.fluent.task.FluentBatchlet;
import io.machinecode.chainlink.jsl.fluent.task.FluentCheckpointAlgorithm;
import io.machinecode.chainlink.jsl.fluent.task.FluentChunk;
import io.machinecode.chainlink.jsl.fluent.task.FluentExceptionClass;
import io.machinecode.chainlink.jsl.fluent.task.FluentExceptionClassFilter;
import io.machinecode.chainlink.jsl.fluent.task.FluentItemProcessor;
import io.machinecode.chainlink.jsl.fluent.task.FluentItemReader;
import io.machinecode.chainlink.jsl.fluent.task.FluentItemWriter;
import io.machinecode.chainlink.jsl.fluent.transition.FluentEnd;
import io.machinecode.chainlink.jsl.fluent.transition.FluentFail;
import io.machinecode.chainlink.jsl.fluent.transition.FluentNext;
import io.machinecode.chainlink.jsl.fluent.transition.FluentStop;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
public class Jsl {

    public static FluentJob job(final String id) {
        return new FluentJob().setId(id);
    }

    public static FluentListener listener(final String ref) {
        return new FluentListener().setRef(ref);
    }

    public static FluentListeners listeners() {
        return new FluentListeners();
    }

    public static FluentProperty property() {
        return new FluentProperty();
    }

    public static FluentProperties properties() {
        return new FluentProperties();
    }

    // Execution

    public static FluentDecision decision(final String id) {
        return new FluentDecision().setId(id);
    }

    public static FluentFlow flow(final String id) {
        return new FluentFlow().setId(id);
    }

    public static FluentSplit split(final String id) {
        return new FluentSplit().setId(id);
    }

    public static FluentStep step(final String id) {
        return new FluentStep().setId(id);
    }

    // Transition

    public static FluentEnd end() {
        return new FluentEnd();
    }

    public static FluentFail fail() {
        return new FluentFail();
    }

    public static FluentNext next() {
        return new FluentNext();
    }

    public static FluentStop stop() {
        return new FluentStop();
    }

    // Partition

    public static FluentAnalyser analyser(final String ref) {
        return new FluentAnalyser().setRef(ref);
    }

    public static FluentCollector collector(final String ref) {
        return new FluentCollector().setRef(ref);
    }

    public static FluentPartition partition() {
        return new FluentPartition();
    }

    public static FluentMapper mapper(final String ref) {
        return new FluentMapper().setRef(ref);
    }

    public static FluentPlan plan() {
        return new FluentPlan();
    }

    public static FluentReducer reducer(final String ref) {
        return new FluentReducer().setRef(ref);
    }


    // Task

    public static FluentBatchlet batchlet(final String ref) {
        return new FluentBatchlet().setRef(ref);
    }

    public static FluentCheckpointAlgorithm checkpointAlgorithm(final String ref) {
        return new FluentCheckpointAlgorithm().setRef(ref);
    }

    public static FluentChunk chunk() {
        return new FluentChunk();
    }

    public static FluentExceptionClass classes() {
        return new FluentExceptionClass();
    }

    public static FluentExceptionClassFilter filter() {
        return new FluentExceptionClassFilter();
    }

    public static FluentExceptionClassFilter skippableExceptionClasses() {
        return filter();
    }

    public static FluentExceptionClassFilter retryableExceptionClasses() {
        return filter();
    }

    public static FluentExceptionClassFilter noRollbackExceptionClasses() {
        return filter();
    }

    public static FluentItemProcessor processor(final String ref) {
        return new FluentItemProcessor().setRef(ref);
    }

    public static FluentItemReader reader(final String ref) {
        return new FluentItemReader().setRef(ref);
    }

    public static FluentItemWriter writer(final String ref) {
        return new FluentItemWriter().setRef(ref);
    }
}
