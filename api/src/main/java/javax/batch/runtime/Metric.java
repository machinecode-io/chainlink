package javax.batch.runtime;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public interface Metric {

    MetricType getType();

    long getValue();

    enum MetricType {
        READ_COUNT,
        WRITE_COUNT,
        COMMIT_COUNT,
        ROLLBACK_COUNT,
        READ_SKIP_COUNT,
        PROCESS_SKIP_COUNT,
        FILTER_COUNT,
        WRITE_SKIP_COUNT
    }
}
