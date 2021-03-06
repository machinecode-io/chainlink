package io.machinecode.chainlink.spi.jsl.inherit.transition;

import io.machinecode.chainlink.spi.jsl.inherit.Copyable;
import io.machinecode.chainlink.spi.jsl.transition.Next;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public interface InheritableNext<T extends InheritableNext<T>>
        extends Copyable<T>, Next {

    T setOn(final String on);

    T setTo(final String to);

    class NextTool {

        public static <T extends InheritableNext<T>>
        T copy(final T _this, final T that) {
            that.setOn(_this.getOn());
            that.setTo(_this.getTo());
            return that;
        }
    }
}
