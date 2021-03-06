package io.machinecode.chainlink.repository.infinispan;

import io.machinecode.chainlink.spi.repository.ExtendedJobInstance;

import javax.batch.runtime.JobInstance;

/**
* @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
*/
public class JobInstanceCountCallable extends BaseCallable<Long, ExtendedJobInstance, Integer> {
    private static final long serialVersionUID = 1L;

    private final String jobName;

    public JobInstanceCountCallable(final String jobName) {
        this.jobName = jobName;
    }

    @Override
    public Integer call() throws Exception {
        int i = 0;
        for (final JobInstance jobInstance : cache.values()) {
            if (jobName.equals(jobInstance.getJobName())) {
                ++i;
            }
        }
        return i;
    }
}
