package io.machinecode.nock.core.factory.execution;

import io.machinecode.nock.core.descriptor.ListenersImpl;
import io.machinecode.nock.core.descriptor.PropertiesImpl;
import io.machinecode.nock.core.descriptor.execution.StepImpl;
import io.machinecode.nock.core.descriptor.partition.MapperImpl;
import io.machinecode.nock.core.descriptor.partition.PartitionImpl;
import io.machinecode.nock.core.descriptor.task.ChunkImpl;
import io.machinecode.nock.core.descriptor.transition.TransitionImpl;
import io.machinecode.nock.core.expression.Expression;
import io.machinecode.nock.core.expression.JobParameterContext;
import io.machinecode.nock.core.expression.JobPropertyContext;
import io.machinecode.nock.core.expression.PartitionPropertyContext;
import io.machinecode.nock.core.factory.ExecutionFactory;
import io.machinecode.nock.core.factory.PropertiesFactory;
import io.machinecode.nock.core.factory.StepListenersFactory;
import io.machinecode.nock.core.factory.partition.MapperPartitionFactory;
import io.machinecode.nock.core.factory.task.ChunkFactory;
import io.machinecode.nock.core.factory.transition.Transitions;
import io.machinecode.nock.core.work.ListenersWork;
import io.machinecode.nock.core.work.execution.StepWork;
import io.machinecode.nock.core.work.partition.MapperWork;
import io.machinecode.nock.core.work.partition.PartitionWork;
import io.machinecode.nock.core.work.task.ChunkWork;
import io.machinecode.nock.core.work.transition.TransitionWork;
import io.machinecode.nock.spi.element.execution.Execution;
import io.machinecode.nock.spi.element.execution.Step;
import io.machinecode.nock.spi.element.partition.Mapper;
import io.machinecode.nock.spi.element.task.Chunk;

import javax.batch.api.listener.StepListener;
import java.util.List;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class ChunkMapperStepFactory implements ExecutionFactory<Step<? extends Chunk, ? extends Mapper>, StepImpl<ChunkImpl, MapperImpl>, StepWork<ChunkWork, MapperWork, PartitionWork<MapperWork>>> {

    public static final ChunkMapperStepFactory INSTANCE = new ChunkMapperStepFactory();

    @Override
    public StepImpl<ChunkImpl, MapperImpl> produceDescriptor(final Step<? extends Chunk, ? extends Mapper> that, final Execution execution, final JobPropertyContext context) {
        final String id = Expression.resolveDescriptorProperty(that.getId(), context);
        final String next = Expression.resolveDescriptorProperty(that.getNext() == null ? execution == null ? null : execution.getId() : that.getNext(), context);
        final String startLimit = Expression.resolveDescriptorProperty(that.getStartLimit(), context);
        final String allowStartIfComplete = Expression.resolveDescriptorProperty(that.getAllowStartIfComplete(), context);
        final PropertiesImpl properties = PropertiesFactory.INSTANCE.produceDescriptor(that.getProperties(), context);
        final ListenersImpl listeners = StepListenersFactory.INSTANCE.produceDescriptor(that.getListeners(), context);
        final List<TransitionImpl> transitions = Transitions.immutableCopyTransitionsDescriptor(that.getTransitions(), context);
        final ChunkImpl task = that.getTask() == null ? null : ChunkFactory.INSTANCE.produceDescriptor(that.getTask(), context);
        final PartitionImpl<MapperImpl> partition = that.getPartition() == null ? null : MapperPartitionFactory.INSTANCE.produceDescriptor(that.getPartition(), context);
        return new StepImpl<ChunkImpl, MapperImpl>(
                id,
                next,
                startLimit,
                allowStartIfComplete,
                properties,
                listeners,
                transitions,
                task,
                partition
        );
    }

    @Override
    public StepWork<ChunkWork, MapperWork, PartitionWork<MapperWork>> produceExecution(final StepImpl<ChunkImpl, MapperImpl> that, final Execution execution, final JobParameterContext context) {
        final String id = Expression.resolveExecutionProperty(that.getId(), context);
        final String next = Expression.resolveExecutionProperty(that.getNext() == null ? execution == null ? null : execution.getId() : that.getNext(), context);
        final String startLimit = Expression.resolveExecutionProperty(that.getStartLimit(), context);
        final String allowStartIfComplete = Expression.resolveExecutionProperty(that.getAllowStartIfComplete(), context);

        final ListenersWork<StepListener> listeners = StepListenersFactory.INSTANCE.produceExecution(that.getListeners(), context);
        final List<TransitionWork> transitions = Transitions.immutableCopyTransitionsExecution(that.getTransitions(), context);
        final ChunkWork task = that.getTask() == null ? null : ChunkFactory.INSTANCE.produceExecution(that.getTask(), context);
        final PartitionWork<MapperWork> partition = that.getPartition() == null ? null : MapperPartitionFactory.INSTANCE.produceExecution(that.getPartition(), context);
        return new StepWork<ChunkWork, MapperWork, PartitionWork<MapperWork>>(
                id,
                next,
                startLimit,
                allowStartIfComplete,
                listeners,
                transitions,
                task,
                partition
        );
    }

    @Override
    public StepWork<ChunkWork, MapperWork, PartitionWork<MapperWork>> producePartitioned(final StepWork<ChunkWork, MapperWork, PartitionWork<MapperWork>> that, final Execution execution, final PartitionPropertyContext context) {
        final String id = Expression.resolvePartitionProperty(that.getId(), context);
        final String next = Expression.resolvePartitionProperty(that.getNext() == null ? execution == null ? null : execution.getId() : that.getNext(), context);
        final String startLimit = Expression.resolvePartitionProperty(that.getStartLimit(), context);
        final String allowStartIfComplete = Expression.resolvePartitionProperty(that.getAllowStartIfComplete(), context);

        final ListenersWork<StepListener> listeners = StepListenersFactory.INSTANCE.producePartitioned(that.getListeners(), context);
        final List<TransitionWork> transitions = Transitions.immutableCopyTransitionsPartition(that.getTransitions(), context);
        final ChunkWork task = that.getTask() == null ? null : ChunkFactory.INSTANCE.producePartitioned(that.getTask(), context);
        final PartitionWork<MapperWork> partition = that.getPartition() == null ? null : MapperPartitionFactory.INSTANCE.producePartitioned(that.getPartition(), context);
        return new StepWork<ChunkWork, MapperWork, PartitionWork<MapperWork>>(
                id,
                next,
                startLimit,
                allowStartIfComplete,
                listeners,
                transitions,
                task,
                partition
        );
    }
}
