package io.machinecode.chainlink.jsl.fluent.partition;

import io.machinecode.chainlink.spi.element.partition.Analyser;
import io.machinecode.chainlink.jsl.fluent.FluentPropertyReference;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
public class FluentAnalyser extends FluentPropertyReference<FluentAnalyser> implements Analyser {
    @Override
    public FluentAnalyser copy() {
        return copy(new FluentAnalyser());
    }
}
