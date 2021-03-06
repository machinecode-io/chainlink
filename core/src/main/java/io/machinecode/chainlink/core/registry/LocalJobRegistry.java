package io.machinecode.chainlink.core.registry;

import io.machinecode.chainlink.spi.execution.Executable;
import io.machinecode.chainlink.spi.registry.ChainId;
import io.machinecode.chainlink.spi.registry.ExecutableId;
import io.machinecode.chainlink.spi.registry.SplitAccumulator;
import io.machinecode.chainlink.spi.registry.StepAccumulator;
import io.machinecode.chainlink.spi.then.Chain;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class LocalJobRegistry {

    protected final ConcurrentMap<ChainId, Chain<?>> chains = new ConcurrentHashMap<>();
    protected final ConcurrentMap<ExecutableId, Executable> executables = new ConcurrentHashMap<>();
    protected final ConcurrentMap<String, SplitAccumulator> splits = new ConcurrentHashMap<>();
    protected final ConcurrentMap<String, StepAccumulator> steps = new ConcurrentHashMap<>();

    public void registerChain(final ChainId id, final Chain<?> chain) {
        this.chains.put(id, chain);
    }

    public Chain<?> getChain(final ChainId id) {
        return this.chains.get(id);
    }

    public void registerExecutable(final ExecutableId id, final Executable executable) {
        this.executables.put(id, executable);
    }

    public Executable getExecutable(final ExecutableId id) {
        return this.executables.get(id);
    }

    public StepAccumulator getStepAccumulator(final String id) {
        StepAccumulator step = steps.get(id);
        if (step != null) {
            return step;
        }
        steps.put(id, step = new StepAccumulatorImpl());
        return step;
    }

    public SplitAccumulator getSplitAccumulator(final String id) {
        SplitAccumulator split = splits.get(id);
        if (split != null) {
            return split;
        }
        splits.put(id, split = new SplitAccumulatorImpl());
        return split;
    }
}
