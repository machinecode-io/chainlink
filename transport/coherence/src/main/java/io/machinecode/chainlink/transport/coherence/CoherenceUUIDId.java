package io.machinecode.chainlink.transport.coherence;

import com.tangosol.net.Member;
import io.machinecode.chainlink.core.transport.DistributedUUIDId;

import java.util.UUID;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class CoherenceUUIDId extends DistributedUUIDId<Member> {
    private static final long serialVersionUID = 1L;

    public CoherenceUUIDId(final UUID uuid, final Member address) {
        super(uuid, address);
    }

    public CoherenceUUIDId(final Member address) {
        super(address);
    }
}
