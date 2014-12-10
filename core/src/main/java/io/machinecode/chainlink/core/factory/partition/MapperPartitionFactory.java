package io.machinecode.chainlink.core.factory.partition;

import io.machinecode.chainlink.core.element.partition.AnalyserImpl;
import io.machinecode.chainlink.core.element.partition.CollectorImpl;
import io.machinecode.chainlink.core.element.partition.MapperImpl;
import io.machinecode.chainlink.core.element.partition.PartitionImpl;
import io.machinecode.chainlink.core.element.partition.ReducerImpl;
import io.machinecode.chainlink.core.factory.ElementFactory;
import io.machinecode.chainlink.spi.element.partition.Mapper;
import io.machinecode.chainlink.spi.element.partition.Partition;
import io.machinecode.chainlink.spi.expression.JobPropertyContext;
import io.machinecode.chainlink.spi.expression.PropertyContext;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class MapperPartitionFactory implements ElementFactory<Partition<? extends Mapper>, PartitionImpl<MapperImpl>> {

    public static final MapperPartitionFactory INSTANCE = new MapperPartitionFactory();

    @Override
    public PartitionImpl<MapperImpl> produceExecution(final Partition<? extends Mapper> that, final JobPropertyContext context) {
        final CollectorImpl collector = that.getCollector() == null ? null : CollectorFactory.INSTANCE.produceExecution(that.getCollector(), context);
        final AnalyserImpl analyser = that.getAnalyzer() == null ? null : AnalyserFactory.INSTANCE.produceExecution(that.getAnalyzer(), context);
        final ReducerImpl reducer = that.getReducer() == null ? null : ReducerFactory.INSTANCE.produceExecution(that.getReducer(), context);
        final MapperImpl mapper = that.getStrategy() == null ? null : MapperFactory.INSTANCE.produceExecution(that.getStrategy(), context);
        return new PartitionImpl<MapperImpl>(
                collector,
                analyser,
                reducer,
                mapper
        );
    }

    @Override
    public PartitionImpl<MapperImpl> producePartitioned(final PartitionImpl<MapperImpl> that, final PropertyContext context) {
        final CollectorImpl collector = that.getCollector() == null ? null : CollectorFactory.INSTANCE.producePartitioned(that.getCollector(), context);
        final AnalyserImpl analyser = that.getAnalyzer() == null ? null : AnalyserFactory.INSTANCE.producePartitioned(that.getAnalyzer(), context);
        final ReducerImpl reducer = that.getReducer() == null ? null : ReducerFactory.INSTANCE.producePartitioned(that.getReducer(), context);
        final MapperImpl strategy = that.getStrategy() == null ? null : MapperFactory.INSTANCE.producePartitioned(that.getStrategy(), context);
        return new PartitionImpl<MapperImpl>(
                collector,
                analyser,
                reducer,
                strategy
        );
    }
}
