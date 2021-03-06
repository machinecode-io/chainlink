package io.machinecode.chainlink.repository.jpa;

import io.machinecode.chainlink.core.repository.JobExecutionImpl;
import io.machinecode.chainlink.core.repository.JobInstanceImpl;
import io.machinecode.chainlink.core.repository.PartitionExecutionImpl;
import io.machinecode.chainlink.core.repository.StepExecutionImpl;
import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.repository.ExtendedJobExecution;
import io.machinecode.chainlink.spi.repository.ExtendedJobInstance;
import io.machinecode.chainlink.spi.repository.ExtendedStepExecution;
import io.machinecode.chainlink.spi.repository.PartitionExecution;
import io.machinecode.chainlink.spi.repository.Repository;

import javax.batch.operations.JobExecutionAlreadyCompleteException;
import javax.batch.operations.JobExecutionNotMostRecentException;
import javax.batch.operations.JobRestartException;
import javax.batch.operations.NoSuchJobException;
import javax.batch.operations.NoSuchJobExecutionException;
import javax.batch.operations.NoSuchJobInstanceException;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobInstance;
import javax.batch.runtime.Metric;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class JpaRepository implements Repository {

    protected final EntityManagerFactory factory;
    protected final TransactionManagerLookup lookup;

    public JpaRepository(final EntityManagerLookup entityManagerLookup, final TransactionManagerLookup transactionManagerLookup) {
        this.factory = entityManagerLookup.getEntityManagerFactory();
        this.lookup = transactionManagerLookup;
    }

    private EntityManager em() {
        return factory.createEntityManager();
    }

    @Override
    public ExtendedJobInstance createJobInstance(final String jobId, final String jslName, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobInstance instance = new JpaJobInstance()
                    .setJobName(jobId)
                    .setJslName(jslName)
                    .setCreateTime(timestamp);
            em.persist(instance);
            em.flush();
            final JobInstanceImpl copy = new JobInstanceImpl(instance);
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public ExtendedJobExecution createJobExecution(final long jobInstanceId, final String jobName, final Properties parameters, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final ExtendedJobExecution that = _createJobExecution(em, jobInstanceId, jobName, parameters, timestamp);
            transaction.commit();
            return that;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    public ExtendedJobExecution _createJobExecution(final EntityManager em, final long jobInstanceId, final String jobName, final Properties parameters, final Date timestamp) throws Exception {
            final JpaJobInstance instance = em.find(JpaJobInstance.class, jobInstanceId);
            if (instance == null) {
                throw new NoSuchJobInstanceException(Messages.format("CHAINLINK-006001.repository.no.such.job.instance", jobInstanceId));
            }
            final JpaJobExecution execution = new JpaJobExecution()
                    .setBatchStatus(BatchStatus.STARTING)
                    .setCreateTime(timestamp)
                    .setLastUpdatedTime(timestamp)
                    .setJobParameters(parameters)
                    .setJobName(jobName)
                    .setJobInstance(instance);
            em.persist(execution);
            em.flush();
            return new JobExecutionImpl(execution);
    }

    @Override
    public ExtendedStepExecution createStepExecution(final long jobExecutionId, final String stepName, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobExecution jpaJobExecution = em.find(JpaJobExecution.class, jobExecutionId);
            if (jpaJobExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", jobExecutionId));
            }
            final JpaStepExecution execution = new JpaStepExecution()
                    .setBatchStatus(BatchStatus.STARTING)
                    .setCreateTime(timestamp)
                    .setUpdatedTime(timestamp)
                    .setStepName(stepName)
                    .setMetricsMap(JpaMetric.empty())
                    .setJobExecution(jpaJobExecution);
            em.persist(execution);
            em.flush();
            final StepExecutionImpl copy = new StepExecutionImpl(execution);
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public PartitionExecution createPartitionExecution(final long stepExecutionId, final int partitionId, final Properties properties, final Serializable persistentUserData, final Serializable readerCheckpoint, final Serializable writerCheckpoint, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaStepExecution stepExecution = em.find(JpaStepExecution.class, stepExecutionId);
            if (stepExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006003.repository.no.such.step.execution", stepExecutionId));
            }
            final JpaPartitionExecution execution = new JpaPartitionExecution()
                    .setStepExecution(stepExecution)
                    .setPartitionId(partitionId)
                    .setPartitionParameters(properties)
                    .setCreateTime(timestamp)
                    .setUpdatedTime(timestamp)
                    .setMetricsMap(JpaMetric.empty())
                    .setPersistentUserData(persistentUserData)
                    .setReaderCheckpoint(readerCheckpoint)
                    .setWriterCheckpoint(writerCheckpoint)
                    .setBatchStatus(BatchStatus.STARTING);
            em.persist(execution);
            em.flush();
            final PartitionExecutionImpl copy = new PartitionExecutionImpl(execution);
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public void startJobExecution(final long jobExecutionId, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobExecution jobExecution = em.find(JpaJobExecution.class, jobExecutionId);
            if (jobExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", jobExecutionId));
            }
            jobExecution.setBatchStatus(BatchStatus.STARTED)
                    .setStartTime(timestamp)
                    .setLastUpdatedTime(timestamp);
            em.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public void updateJobExecution(final long jobExecutionId, final BatchStatus batchStatus, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobExecution jobExecution = em.find(JpaJobExecution.class, jobExecutionId);
            if (jobExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", jobExecutionId));
            }
            jobExecution.setBatchStatus(batchStatus)
                    .setLastUpdatedTime(timestamp);
            em.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public void finishJobExecution(final long jobExecutionId, final BatchStatus batchStatus, final String exitStatus, final String restartElementId, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobExecution jobExecution = em.find(JpaJobExecution.class, jobExecutionId);
            if (jobExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", jobExecutionId));
            }
            jobExecution.setBatchStatus(batchStatus)
                    .setExitStatus(exitStatus)
                    .setRestartElementId(restartElementId)
                    .setEndTime(timestamp)
                    .setLastUpdatedTime(timestamp);
            em.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public void linkJobExecutions(final long jobExecutionId, final long restartJobExecutionId) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobExecution jobExecution = em.find(JpaJobExecution.class, jobExecutionId);
            if (jobExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", jobExecutionId));
            }
            final JpaJobExecution oldJobExecution = em.find(JpaJobExecution.class, restartJobExecutionId);
            if (oldJobExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", restartJobExecutionId));
            }
            final List<JpaJobExecution> previous = em.createNamedQuery("JpaJobExecution.previous", JpaJobExecution.class)
                    .setParameter("jobExecutionId", restartJobExecutionId)
                    .getResultList();
            em.persist(new JpaJobExecutionHistory()
                    .setJobExecution(jobExecution)
                    .setPreviousJobExecution(oldJobExecution)
            );
            for (final JpaJobExecution execution : previous) {
                em.persist(new JpaJobExecutionHistory()
                        .setJobExecution(jobExecution)
                        .setPreviousJobExecution(execution)
                );
            }
            em.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public void startStepExecution(final long stepExecutionId, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaStepExecution stepExecution = em.find(JpaStepExecution.class, stepExecutionId);
            if (stepExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006003.repository.no.such.step.execution", stepExecutionId));
            }
            stepExecution.setBatchStatus(BatchStatus.STARTED)
                    .setStartTime(timestamp)
                    .setUpdatedTime(timestamp);
            em.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public void updateStepExecution(final long stepExecutionId, final Metric[] metrics, final Serializable persistentUserData, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaStepExecution stepExecution = em.find(JpaStepExecution.class, stepExecutionId);
            if (stepExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006003.repository.no.such.step.execution", stepExecutionId));
            }
            stepExecution.setPersistentUserData(persistentUserData)
                    .setUpdatedTime(timestamp);
            _updateMetrics(metrics, stepExecution.getMetricsMap());
            em.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public void updateStepExecution(final long stepExecutionId, final Metric[] metrics, final Serializable persistentUserData, final Serializable readerCheckpoint, final Serializable writerCheckpoint, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaStepExecution stepExecution = em.find(JpaStepExecution.class, stepExecutionId);
            if (stepExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006003.repository.no.such.step.execution", stepExecutionId));
            }
            stepExecution.setPersistentUserData(persistentUserData)
                    .setReaderCheckpoint(readerCheckpoint)
                    .setWriterCheckpoint(writerCheckpoint)
                    .setUpdatedTime(timestamp);
            _updateMetrics(metrics, stepExecution.getMetricsMap());
            em.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public void finishStepExecution(final long stepExecutionId, final Metric[] metrics, final BatchStatus batchStatus, final String exitStatus, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaStepExecution stepExecution = em.find(JpaStepExecution.class, stepExecutionId);
            if (stepExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006003.repository.no.such.step.execution", stepExecutionId));
            }
            stepExecution.setBatchStatus(batchStatus)
                    .setExitStatus(exitStatus)
                    .setEndTime(timestamp)
                    .setUpdatedTime(timestamp);
            _updateMetrics(metrics, stepExecution.getMetricsMap());
            em.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public void startPartitionExecution(final long partitionExecutionId, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaPartitionExecution execution = em.find(JpaPartitionExecution.class, partitionExecutionId);
            if (execution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006008.repository.no.such.partition.execution", partitionExecutionId));
            }
            execution.setBatchStatus(BatchStatus.STARTED)
                    .setStartTime(timestamp)
                    .setUpdatedTime(timestamp);
            em.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public void updatePartitionExecution(final long partitionExecutionId, final Metric[] metrics, final Serializable persistentUserData, final Serializable readerCheckpoint, final Serializable writerCheckpoint, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaPartitionExecution execution = em.find(JpaPartitionExecution.class, partitionExecutionId);
            if (execution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006008.repository.no.such.partition.execution", partitionExecutionId));
            }
            execution.setPersistentUserData(persistentUserData)
                    .setReaderCheckpoint(readerCheckpoint)
                    .setWriterCheckpoint(writerCheckpoint)
                    .setUpdatedTime(timestamp);
            _updateMetrics(metrics, execution.getMetricsMap());
            em.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public void finishPartitionExecution(final long partitionExecutionId, final Metric[] metrics, final Serializable persistentUserData, final BatchStatus batchStatus, final String exitStatus, final Date timestamp) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaPartitionExecution execution = em.find(JpaPartitionExecution.class, partitionExecutionId);
            if (execution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006008.repository.no.such.partition.execution", partitionExecutionId));
            }
            execution.setPersistentUserData(persistentUserData)
                    .setBatchStatus(batchStatus)
                    .setExitStatus(exitStatus)
                    .setEndTime(timestamp)
                    .setUpdatedTime(timestamp);
            _updateMetrics(metrics, execution.getMetricsMap());
            em.flush();
            transaction.commit();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public Set<String> getJobNames() throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final List<String> names = em.createNamedQuery("JpaJobInstance.jobNames", String.class)
                    .getResultList();
            transaction.commit();
            return new HashSet<>(names);
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public int getJobInstanceCount(final String jobName) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final long count = em.createNamedQuery("JpaJobInstance.countWithJobName", Long.class)
                    .setParameter("jobName", jobName)
                    .getSingleResult();
            if (count == 0) {
                throw new NoSuchJobException(Messages.format("CHAINLINK-006000.repository.no.such.job", jobName));
            }
            transaction.commit();
            return (int)count;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public List<JobInstance> getJobInstances(final String jobName, final int start, final int count) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final List<JpaJobInstance> instances = em.createNamedQuery("JpaJobExecution.withJobName", JpaJobInstance.class)
                    .setFirstResult(start)
                    .setMaxResults(count)
                    .setParameter("jobName", jobName)
                    .getResultList();
            final int size = instances.size();
            if (size == 0) {
                if (em.createNamedQuery("JpaJobInstance.countWithJobName", Long.class)
                        .setParameter("jobName", jobName)
                        .getSingleResult() == 0) {
                    throw new NoSuchJobException(Messages.format("CHAINLINK-006000.repository.no.such.job", jobName));
                }
                transaction.commit();
                return Collections.emptyList();
            }
            final List<JobInstance> copy = new ArrayList<>(size);
            for (final JpaJobInstance instance : instances) {
                copy.add(new JobInstanceImpl(instance));
            }
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public List<Long> getRunningExecutions(final String jobName) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final List<Long> ids = em.createNamedQuery("JpaJobExecution.runningJobExecutionIds", Long.class)
                    .setParameter("jobName", jobName)
                    .getResultList();
            if (ids.isEmpty()) {
                if (em.createNamedQuery("JpaJobExecution.countWithJobName", Long.class)
                    .setParameter("jobName", jobName)
                    .getSingleResult() == 0) {
                    throw new NoSuchJobException(Messages.format("CHAINLINK-006000.repository.no.such.job", jobName));
                }
            }
            transaction.commit();
            return ids;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public Properties getParameters(final long jobExecutionId) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobExecution jobExecution = em.find(JpaJobExecution.class, jobExecutionId);
            if (jobExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", jobExecutionId));
            }
            transaction.commit();
            return jobExecution.getJobParameters();
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public ExtendedJobInstance getJobInstance(final long jobInstanceId) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobInstance jobInstance = em.find(JpaJobInstance.class, jobInstanceId);
            if (jobInstance == null) {
                throw new NoSuchJobInstanceException(Messages.format("CHAINLINK-006001.repository.no.such.job.instance", jobInstanceId));
            }
            final JobInstanceImpl copy = new JobInstanceImpl(jobInstance);
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public ExtendedJobInstance getJobInstanceForExecution(final long jobExecutionId) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobExecution jobExecution = em.find(JpaJobExecution.class, jobExecutionId);
            if (jobExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", jobExecutionId));
            }
            final JobInstanceImpl copy = new JobInstanceImpl(jobExecution.getJobInstance());
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public List<JobExecutionImpl> getJobExecutions(final long jobInstanceId) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobInstance jobInstance = em.find(JpaJobInstance.class, jobInstanceId);
            if (jobInstance == null) {
                throw new NoSuchJobInstanceException(Messages.format("CHAINLINK-006001.repository.no.such.job.instance", jobInstanceId));
            }
            final List<JobExecutionImpl> copy = JobExecutionImpl.copy(jobInstance.getJobExecutions());
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public JobExecutionImpl getJobExecution(final long jobExecutionId) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobExecution jobExecution = em.find(JpaJobExecution.class, jobExecutionId);
            if (jobExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", jobExecutionId));
            }
            final JobExecutionImpl copy = new JobExecutionImpl(jobExecution);
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public ExtendedJobExecution restartJobExecution(final long jobExecutionId, final Properties parameters) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobExecution jobExecution = em.find(JpaJobExecution.class, jobExecutionId);
            if (jobExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", jobExecutionId));
            }
            final long jobInstanceId = jobExecution.getJobInstance().getInstanceId();
            final List<JpaJobExecution> jobExecutions = em.createNamedQuery("JpaJobExecution.byCreateDate", JpaJobExecution.class)
                    .setParameter("jobInstanceId", jobInstanceId)
                    .setMaxResults(1)
                    .getResultList();
            if (jobExecutions.isEmpty()) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", jobExecutionId));
            }
            final JpaJobExecution latest = jobExecutions.get(0);
            if (jobExecutionId != latest.getExecutionId()) {
                throw new JobExecutionNotMostRecentException(Messages.format("CHAINLINK-006004.repository.not.most.recent.execution", jobExecutionId, jobInstanceId));
            }
            switch (jobExecution.getBatchStatus()) {
                case STOPPED:
                case FAILED:
                    break;
                case COMPLETED:
                    throw new JobExecutionAlreadyCompleteException(Messages.format("CHAINLINK-006006.repository.execution.already.complete", jobExecutionId));
                default:
                    throw new JobRestartException(Messages.format("CHAINLINK-006007.repository.execution.not.eligible.for.restart", jobExecution.getExecutionId(), BatchStatus.STOPPED, BatchStatus.FAILED, jobExecution.getBatchStatus()));
            }
            final ExtendedJobExecution that = _createJobExecution(
                    em,
                    jobInstanceId,
                    jobExecution.getJobName(),
                    parameters,
                    new Date()
            );
            transaction.commit();
            return that;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public List<StepExecutionImpl> getStepExecutionsForJobExecution(final long jobExecutionId) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaJobExecution jobExecution = em.find(JpaJobExecution.class, jobExecutionId);
            if (jobExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006002.repository.no.such.job.execution", jobExecutionId));
            }
            final List<StepExecutionImpl> copy = StepExecutionImpl.copy(jobExecution.getStepExecutions());
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public StepExecutionImpl getStepExecution(final long stepExecutionId) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaStepExecution stepExecution = em.find(JpaStepExecution.class, stepExecutionId);
            if (stepExecution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006003.repository.no.such.step.execution", stepExecutionId));
            }
            final StepExecutionImpl copy = new StepExecutionImpl(stepExecution);
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public StepExecutionImpl getPreviousStepExecution(final long jobExecutionId, final long stepExecutionId, final String stepName) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final List<JpaStepExecution> stepExecutions = em.createNamedQuery("JpaStepExecution.previous", JpaStepExecution.class)
                    .setMaxResults(1)
                    .setParameter("jobExecutionId", jobExecutionId)
                    .setParameter("stepExecutionId", stepExecutionId)
                    .setParameter("stepName", stepName)
                    .getResultList();
            if (stepExecutions.isEmpty()) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006005.repository.no.step.named", jobExecutionId, stepName));
            }
            final StepExecutionImpl copy = new StepExecutionImpl(stepExecutions.get(0));
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public StepExecutionImpl getLatestStepExecution(final long jobExecutionId, final String stepName) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final List<JpaStepExecution> stepExecutions = em.createNamedQuery("JpaStepExecution.latest", JpaStepExecution.class)
                    .setMaxResults(1)
                    .setParameter("jobExecutionId", jobExecutionId)
                    .setParameter("stepName", stepName)
                    .getResultList();
            if (stepExecutions.isEmpty()) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006005.repository.no.step.named", jobExecutionId, stepName));
            }
            final StepExecutionImpl copy = new StepExecutionImpl(stepExecutions.get(0));
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public int getStepExecutionCount(final long jobExecutionId, final String stepName) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final long count = em.createNamedQuery("JpaStepExecution.countWithJobExecutionIdAndStepName", Long.class)
                    .setParameter("jobExecutionId", jobExecutionId)
                    .setParameter("stepName", stepName)
                    .getSingleResult();
            transaction.commit();
            return (int)count;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public StepExecutionImpl[] getStepExecutions(final long[] stepExecutionIds) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final StepExecutionImpl[] stepExecutions = new StepExecutionImpl[stepExecutionIds.length];
            for (int i = 0; i < stepExecutionIds.length; ++i) {
                final long stepExecutionId = stepExecutionIds[i];
                final JpaStepExecution stepExecution = em.find(JpaStepExecution.class, stepExecutionId);
                if (stepExecution == null) {
                    throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006003.repository.no.such.step.execution", stepExecutionId));
                }
                stepExecutions[i] = new StepExecutionImpl(stepExecution);
            }
            transaction.commit();
            return stepExecutions;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public PartitionExecutionImpl[] getUnfinishedPartitionExecutions(final long stepExecutionId) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final List<JpaPartitionExecution> partitionExecutions = em.createNamedQuery("JpaPartitionExecution.unfinishedForStepExecutionId", JpaPartitionExecution.class)
                    .setParameter("stepExecutionId", stepExecutionId)
                    .getResultList();
            if (partitionExecutions.isEmpty()) { //TODO Really?
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006003.repository.no.such.step.execution", stepExecutionId));
            }
            final PartitionExecutionImpl[] copy = PartitionExecutionImpl.copy(partitionExecutions);
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    @Override
    public PartitionExecutionImpl getPartitionExecution(final long partitionExecutionId) throws Exception {
        final EntityManager em = em();
        final ExtendedTransactionManager transaction = lookup.getTransactionManager(em);
        try {
            transaction.begin();
            if (!transaction.isResourceLocal()) {
                em.joinTransaction();
            }
            final JpaPartitionExecution execution = em.find(JpaPartitionExecution.class, partitionExecutionId);
            if (execution == null) {
                throw new NoSuchJobExecutionException(Messages.format("CHAINLINK-006008.repository.no.such.partition.execution", partitionExecutionId));
            }
            final PartitionExecutionImpl copy = new PartitionExecutionImpl(em.find(JpaPartitionExecution.class, partitionExecutionId));
            transaction.commit();
            return copy;
        } catch (final Exception e) {
            transaction.rollback();
            throw e;
        } finally {
            if (transaction.isResourceLocal()) {
                em.close();
            }
        }
    }

    public void _updateMetrics(final Metric[] source, final Map<Metric.MetricType, JpaMetric> target) {
        for (final Metric metric : source) {
            target.get(metric.getType()).setValue(metric.getValue());
        }
    }
}
