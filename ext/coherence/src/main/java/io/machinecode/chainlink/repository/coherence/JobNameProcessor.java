package io.machinecode.chainlink.repository.coherence;

import com.tangosol.util.InvocableMap;
import io.machinecode.chainlink.spi.repository.ExtendedJobInstance;

/**
* @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
*/
public class JobNameProcessor extends BaseProcessor {
    private static final long serialVersionUID = 1L;

    @Override
    public Object process(final InvocableMap.Entry entry) {
        return ((ExtendedJobInstance) entry.getValue()).getJobName();
    }
}
