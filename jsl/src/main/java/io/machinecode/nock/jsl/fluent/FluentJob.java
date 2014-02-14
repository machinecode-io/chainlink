package io.machinecode.nock.jsl.fluent;

import io.machinecode.nock.jsl.fluent.execution.FluentExecution;
import io.machinecode.nock.jsl.inherit.InheritableJob;
import io.machinecode.nock.spi.JobRepository;
import io.machinecode.nock.spi.element.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class FluentJob extends FluentInheritable<FluentJob> implements InheritableJob<FluentJob, FluentProperties, FluentListeners, FluentExecution>, Job {

    private String id;
    private String version = "1.0";
    private String restartable = "true";
    private FluentProperties properties;
    private FluentListeners listeners;
    private List<FluentExecution> executions = new ArrayList<FluentExecution>(0);

    @Override
    public String getId() {
        return this.id;
    }

    public FluentJob setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    public FluentJob setVersion(final String version) {
        this.version = version;
        return this;
    }

    @Override
    public String getRestartable() {
        return this.restartable;
    }

    public FluentJob setRestartable(final String restartable) {
        this.restartable = restartable;
        return this;
    }

    @Override
    public FluentProperties getProperties() {
        return this.properties;
    }

    @Override
    public FluentJob setProperties(final FluentProperties properties) {
        this.properties = properties;
        return this;
    }

    public FluentJob addProperty(final String name, final String value) {
        if (this.properties == null) {
            this.properties = new FluentProperties();
        }
        this.properties.addProperty(name, value);
        return this;
    }

    @Override
    public FluentListeners getListeners() {
        return this.listeners;
    }

    @Override
    public FluentJob setListeners(final FluentListeners listeners) {
        this.listeners = listeners;
        return this;
    }

    public FluentJob addListener(final FluentListener listener) {
        if (this.listeners == null) {
            this.listeners = new FluentListeners();
        }
        this.listeners.addListener(listener);
        return this;
    }

    @Override
    public List<FluentExecution> getExecutions() {
        return this.executions;
    }

    @Override
    public FluentJob setExecutions(final List<FluentExecution> executions) {
        this.executions = executions;
        return this;
    }

    public FluentJob addExecution(final FluentExecution execution) {
        this.executions.add(execution);
        return this;
    }

    @Override
    public FluentJob inherit(final JobRepository repository, final String defaultJobXml) {
        return JobTool.inherit(FluentJob.class, this, repository, defaultJobXml);
    }

    @Override
    public FluentJob copy() {
        return copy(new FluentJob());
    }

    @Override
    public FluentJob copy(final FluentJob that) {
        return JobTool.copy(this, that);
    }
}
