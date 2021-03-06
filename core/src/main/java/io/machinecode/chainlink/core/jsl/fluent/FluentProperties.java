package io.machinecode.chainlink.core.jsl.fluent;

import io.machinecode.chainlink.spi.jsl.inherit.InheritableProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class FluentProperties extends FluentMergeableList<FluentProperties> implements InheritableProperties<FluentProperties, FluentProperty> {

    private String partition;
    private List<FluentProperty> properties = new ArrayList<>(0);


    @Override
    public String getPartition() {
        return partition;
    }

    public FluentProperties setPartition(final String partition) {
        this.partition = partition;
        return this;
    }

    @Override
    public List<FluentProperty> getProperties() {
        return this.properties;
    }

    public FluentProperties addProperty(final FluentProperty property) {
        this.properties.add(property);
        return this;
    }

    public FluentProperties addProperty(final String name, final String value) {
        this.properties.add(new FluentProperty().setName(name).setValue(value));
        return this;
    }

    @Override
    public FluentProperties setProperties(final List<FluentProperty> properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public FluentProperties copy() {
        return copy(new FluentProperties());
    }

    @Override
    public FluentProperties copy(final FluentProperties that) {
        return PropertiesTool.copy(this, that);
    }

    @Override
    public FluentProperties merge(final FluentProperties that) {
        return PropertiesTool.merge(this, that);
    }
}
