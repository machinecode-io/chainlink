package io.machinecode.chainlink.repository.ehcache;

import gnu.trove.set.hash.THashSet;
import io.machinecode.chainlink.core.repository.BaseMapRepository;
import io.machinecode.chainlink.spi.marshalling.Marshalling;
import io.machinecode.chainlink.spi.repository.ExtendedJobExecution;
import io.machinecode.chainlink.spi.repository.ExtendedJobInstance;
import io.machinecode.chainlink.spi.repository.ExtendedStepExecution;
import io.machinecode.chainlink.spi.repository.PartitionExecution;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;
import net.sf.ehcache.search.aggregator.Aggregators;
import net.sf.ehcache.search.expression.AlwaysMatch;
import net.sf.ehcache.search.expression.EqualTo;
import net.sf.ehcache.search.expression.InCollection;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.batch.runtime.JobInstance;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class EhCacheRepository extends BaseMapRepository {

    protected static final String IDS = EhCacheRepository.class.getCanonicalName() + ".ids";
    protected static final String JOB_INSTANCES = EhCacheRepository.class.getCanonicalName() + ".jobInstances";
    protected static final String JOB_EXECUTIONS = EhCacheRepository.class.getCanonicalName() + ".jobExecutions";
    protected static final String STEP_EXECUTIONS = EhCacheRepository.class.getCanonicalName() + ".stepExecutions";
    protected static final String PARTITION_EXECUTIONS = EhCacheRepository.class.getCanonicalName() + ".partitionExecutions";
    protected static final String JOB_INSTANCE_EXECUTIONS = EhCacheRepository.class.getCanonicalName() + ".jobInstanceExecutions";
    protected static final String JOB_EXECUTION_INSTANCES = EhCacheRepository.class.getCanonicalName() + ".jobExecutionInstances";
    protected static final String JOB_EXECUTION_STEP_EXECUTIONS = EhCacheRepository.class.getCanonicalName() + ".jobExecutionStepExecutions";
    protected static final String LATEST_JOB_EXECUTION_FOR_INSTANCE = EhCacheRepository.class.getCanonicalName() + ".latestJobExecutionForInstance";
    protected static final String STEP_EXECUTION_PARTITION_EXECUTIONS = EhCacheRepository.class.getCanonicalName() + ".stepExecutionPartitionExecutions";
    protected static final String JOB_EXECUTION_HISTORY = EhCacheRepository.class.getCanonicalName() + ".jobExecutionHistory";

    protected final EhCacheMap<String, Long> ids;
    protected final EhCacheMap<Long, ExtendedJobInstance> jobInstances;
    protected final EhCacheMap<Long, ExtendedJobExecution> jobExecutions;
    protected final EhCacheMap<Long, ExtendedStepExecution> stepExecutions;
    protected final EhCacheMap<Long, PartitionExecution> partitionExecutions;
    protected final EhCacheMap<Long, List<Long>> jobInstanceExecutions;
    protected final EhCacheMap<Long, Long> jobExecutionInstances;
    protected final EhCacheMap<Long, Set<Long>> jobExecutionStepExecutions;
    protected final EhCacheMap<Long, Long> latestJobExecutionForInstance;
    protected final EhCacheMap<Long, List<Long>> stepExecutionPartitionExecutions;
    protected final EhCacheMap<Long, Set<Long>> jobExecutionHistory;

    public EhCacheRepository(final Marshalling marshalling, final CacheManager manager) {
        super(marshalling);

        this.ids = EhCacheMap.with(manager.addCacheIfAbsent(IDS));
        this.jobInstances = EhCacheMap.with(manager.addCacheIfAbsent(JOB_INSTANCES));
        this.jobExecutions = EhCacheMap.with(manager.addCacheIfAbsent(JOB_EXECUTIONS));
        this.stepExecutions = EhCacheMap.with(manager.addCacheIfAbsent(STEP_EXECUTIONS));
        this.partitionExecutions = EhCacheMap.with(manager.addCacheIfAbsent(PARTITION_EXECUTIONS));
        this.jobInstanceExecutions = EhCacheMap.with(manager.addCacheIfAbsent(JOB_INSTANCE_EXECUTIONS));
        this.jobExecutionInstances = EhCacheMap.with(manager.addCacheIfAbsent(JOB_EXECUTION_INSTANCES));
        this.jobExecutionStepExecutions = EhCacheMap.with(manager.addCacheIfAbsent(JOB_EXECUTION_STEP_EXECUTIONS));
        this.latestJobExecutionForInstance = EhCacheMap.with(manager.addCacheIfAbsent(LATEST_JOB_EXECUTION_FOR_INSTANCE));
        this.stepExecutionPartitionExecutions = EhCacheMap.with(manager.addCacheIfAbsent(STEP_EXECUTION_PARTITION_EXECUTIONS));
        this.jobExecutionHistory = EhCacheMap.with(manager.addCacheIfAbsent(JOB_EXECUTION_HISTORY));
    }

    @Override
    protected Map<String, Long> ids() {
        return this.ids;
    }

    @Override
    protected Map<Long, ExtendedJobInstance> jobInstances() {
        return this.jobInstances;
    }

    @Override
    protected Map<Long, ExtendedJobExecution> jobExecutions() {
        return this.jobExecutions;
    }

    @Override
    protected Map<Long, ExtendedStepExecution> stepExecutions() {
        return this.stepExecutions;
    }

    @Override
    protected Map<Long, PartitionExecution> partitionExecutions() {
        return this.partitionExecutions;
    }

    @Override
    protected Map<Long, List<Long>> jobInstanceExecutions() {
        return this.jobInstanceExecutions;
    }

    @Override
    protected Map<Long, Long> jobExecutionInstances() {
        return this.jobExecutionInstances;
    }

    @Override
    protected Map<Long, Set<Long>> jobExecutionStepExecutions() {
        return this.jobExecutionStepExecutions;
    }

    @Override
    protected Map<Long, Long> latestJobExecutionForInstance() {
        return this.latestJobExecutionForInstance;
    }

    @Override
    protected Map<Long, List<Long>> stepExecutionPartitionExecutions() {
        return this.stepExecutionPartitionExecutions;
    }

    @Override
    protected Map<Long, Set<Long>> jobExecutionHistory() {
        return this.jobExecutionHistory;
    }

    @Override
    protected long _id(final String key) throws Exception {
        try {
            final long id;
            ids.cache.acquireWriteLockOnKey(key);
            final Long that = ids.get(key);
            id = that == null ? 1 : that + 1;
            ids.put(key, id);
            return id;
        } finally {
            this.ids.cache.releaseWriteLockOnKey(key);
        }
    }

    @Override
    protected Set<String> fetchJobNames() throws Exception {
        final Results result = this.jobInstances.cache.createQuery()
                .addCriteria(new AlwaysMatch())
                .includeValues()
                .execute();
        final Set<String> ret = new THashSet<>();
        for (final Result x : result.all()) {
            ret.add(((ExtendedJobInstance) x.getValue()).getJobName());
        }
        return ret;
    }

    @Override
    protected int fetchJobInstanceCount(final String jobName) throws Exception {
        final Results results = this.jobInstances.cache.createQuery()
                .addCriteria(new EqualTo("job_name", jobName))
                .includeAggregator(Aggregators.count())
                .execute();
        int count = 0;
        for (final Result result : results.all()) {
            for (final Object value : result.getAggregatorResults()) {
               count += (Integer)value;
            }
        }
        return count;
    }

    @Override
    protected List<JobInstance> fetchJobInstances(final String jobName) throws Exception {
        final Results results = this.jobInstances.cache.createQuery()
                .addCriteria(new EqualTo("job_name", jobName))
                .includeValues()
                .execute();
        final List<JobInstance> ret = new ArrayList<>(results.size());
        for (final Result x : results.all()) {
            ret.add(((ExtendedJobInstance) x.getValue()));
        }
        return ret;
    }

    @Override
    protected List<Long> fetchRunningJobExecutionIds(final String jobName) throws Exception {
        final Results results = this.jobExecutions.cache.createQuery()
                .addCriteria(new EqualTo("job_name", jobName))
                .addCriteria(new InCollection("batch_status", Arrays.asList(BatchStatus.STARTING, BatchStatus.STARTED, BatchStatus.STOPPING)))
                .includeValues()
                .execute();
        final List<Long> ret = new ArrayList<>(results.size());
        for (final Result x : results.all()) {
            ret.add(((ExtendedJobExecution) x.getValue()).getExecutionId());
        }
        return ret;
    }

    @Override
    protected List<JobExecution> fetchJobExecutionsForJobInstance(final long jobInstanceId) throws Exception {
        final Results results = this.jobExecutions.cache.createQuery()
                .addCriteria(new EqualTo("job_instance_id", jobInstanceId))
                .includeValues()
                .execute();
        final List<JobExecution> ret = new ArrayList<>(results.size());
        for (final Result x : results.all()) {
            ret.add(((ExtendedJobExecution) x.getValue()));
        }
        return ret;
    }
}
