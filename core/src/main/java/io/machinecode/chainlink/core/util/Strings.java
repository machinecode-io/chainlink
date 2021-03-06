package io.machinecode.chainlink.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class Strings {

    public static String join(final char delim, final Collection<String> values) {
        final StringBuilder builder = new StringBuilder();
        for (final String that : values) {
            if (builder.length() != 0) {
                builder.append(delim);
            }
            builder.append(that);
        }
        return builder.toString();
    }

    public static String join(final char delim, final String... values) {
        final StringBuilder builder = new StringBuilder();
        for (final String that : values) {
            if (builder.length() != 0) {
                builder.append(delim);
            }
            builder.append(that);
        }
        return builder.toString();
    }

    public static <T> String join(final char delim, final To<T> to, final Collection<? extends T> values) {
        final StringBuilder builder = new StringBuilder();
        for (final T that : values) {
            if (builder.length() != 0) {
                builder.append(delim);
            }
            builder.append(to.to(that));
        }
        return builder.toString();
    }

    public static <T> String join(final char delim, final To<T> to, final T... values) {
        final StringBuilder builder = new StringBuilder();
        for (final T that : values) {
            if (builder.length() != 0) {
                builder.append(delim);
            }
            builder.append(to.to(that));
        }
        return builder.toString();
    }

    public static List<String> split(final String delim, final String value) {
        return Arrays.asList(value.split(delim));
    }

    public static <T> List<T> split(final String delim, final From<T> from, final String value) {
        final String[] values = value.split(delim);
        final List<T> ret = new ArrayList<>(values.length);
        for (final String that : values) {
            ret.add(from.from(that));
        }
        return ret;
    }

    public interface To<T> {
        String to(final T that);
    }

    public interface From<T> {
        T from(final String that);
    }
}
