package io.machinecode.chainlink.jsl.core.inherit.execution;

import io.machinecode.chainlink.jsl.core.inherit.Util;
import io.machinecode.chainlink.spi.Inheritable;
import io.machinecode.chainlink.spi.loader.JobRepository;
import io.machinecode.chainlink.spi.element.execution.Flow;
import io.machinecode.chainlink.spi.element.execution.Split;

import java.util.List;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
public interface InheritableSplit<T extends InheritableSplit<T, F>,
        F extends Inheritable<F> & Flow>
        extends InheritableExecution<T>, Split {

    T setId(final String id);

    T setNext(final String next);

    @Override
    List<F> getFlows();

    T setFlows(final List<F> flows);

    class SplitTool {

        public static <T extends InheritableSplit<T, F>,
                F extends Inheritable<F> & Flow>
        T inherit(final T _this, final JobRepository repository, final String defaultJobXml) {
            final T copy = _this.copy();
            copy.setId(_this.getId());
            copy.setNext(_this.getNext());
            copy.setFlows(Util.inheritingList(repository, defaultJobXml, _this.getFlows()));
            return copy;
        }

        public static <T extends InheritableSplit<T, F>,
                F extends Inheritable<F> & Flow>
        T copy(final T _this, final T that) {
            that.setId(_this.getId());
            that.setNext(_this.getNext());
            that.setFlows(Util.copyList(_this.getFlows()));
            return that;
        }
    }
}
