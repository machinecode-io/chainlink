package io.machinecode.chainlink.spi.element.task;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public interface Chunk extends Task {

    String ELEMENT = "chunk";

    String TEN = "10";
    String ZERO = "0";
    String MINUS_ONE = "-1";

    String getCheckpointPolicy();

    String getItemCount();

    String getTimeLimit();

    String getSkipLimit();

    String getRetryLimit();

    ItemReader getReader();

    ItemProcessor getProcessor();

    ItemWriter getWriter();

    CheckpointAlgorithm getCheckpointAlgorithm();

    ExceptionClassFilter getSkippableExceptionClasses();

    ExceptionClassFilter getRetryableExceptionClasses();

    ExceptionClassFilter getNoRollbackExceptionClasses();

    public static final class CheckpointPolicy {
        public static final String ITEM = "item";
        public static final String CUSTOM = "custom";
    }
}