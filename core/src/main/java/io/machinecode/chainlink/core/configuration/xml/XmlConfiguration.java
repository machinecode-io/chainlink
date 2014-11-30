package io.machinecode.chainlink.core.configuration.xml;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.bind.annotation.XmlAccessType.NONE;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
@XmlAccessorType(NONE)
public class XmlConfiguration {

    @XmlID
    @XmlAttribute(name = "id", required = true)
    private String id;

    @XmlElement(name = "executor-factory", namespace = XmlChainlink.NAMESPACE, required = true)
    private XmlClassRef executorFactory;

    @XmlElement(name = "registry-factory", namespace = XmlChainlink.NAMESPACE, required = true)
    private XmlClassRef registryFactory;

    @XmlElement(name = "worker-factory", namespace = XmlChainlink.NAMESPACE, required = true)
    private XmlClassRef workerFactory;

    @XmlElement(name = "marshaller-factory", namespace = XmlChainlink.NAMESPACE, required = true)
    private XmlClassRef marshallerFactory;

    @XmlElement(name = "mbean-server-factory", namespace = XmlChainlink.NAMESPACE, required = true)
    private XmlClassRef mBeanServerFactory;

    @XmlElement(name = "execution-repository-factory", namespace = XmlChainlink.NAMESPACE, required = true)
    private XmlClassRef executionRepositoryFactory;

    @XmlElement(name = "class-loader-factory", namespace = XmlChainlink.NAMESPACE, required = false)
    private XmlClassRef classLoaderFactory;

    @XmlElement(name = "transaction-manager-factory", namespace = XmlChainlink.NAMESPACE, required = false)
    private XmlClassRef transactionManagerFactory;

    @XmlElement(name = "job-loader-factory", namespace = XmlChainlink.NAMESPACE, required = false)
    private List<XmlClassRef> jobLoaderFactories = new ArrayList<XmlClassRef>(0);

    @XmlElement(name = "artifact-loader-factory", namespace = XmlChainlink.NAMESPACE, required = false)
    private List<XmlClassRef> artifactLoaderFactories = new ArrayList<XmlClassRef>(0);

    @XmlElement(name = "injector-factory", namespace = XmlChainlink.NAMESPACE, required = false)
    private List<XmlClassRef> injectorFactories = new ArrayList<XmlClassRef>(0);

    @XmlElement(name = "security-check-factory", namespace = XmlChainlink.NAMESPACE, required = false)
    private List<XmlClassRef> securityCheckFactories = new ArrayList<XmlClassRef>(0);

    @XmlElement(name = "property", namespace = XmlChainlink.NAMESPACE, required = false)
    private List<XmlProperty> properties = new ArrayList<XmlProperty>(0);

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public XmlClassRef getExecutorFactory() {
        return executorFactory;
    }

    public void setExecutorFactory(final XmlClassRef executorFactory) {
        this.executorFactory = executorFactory;
    }

    public XmlClassRef getRegistryFactory() {
        return registryFactory;
    }

    public void setRegistryFactory(final XmlClassRef registryFactory) {
        this.registryFactory = registryFactory;
    }

    public XmlClassRef getWorkerFactory() {
        return workerFactory;
    }

    public void setWorkerFactory(final XmlClassRef workerFactory) {
        this.workerFactory = workerFactory;
    }

    public XmlClassRef getmBeanServerFactory() {
        return mBeanServerFactory;
    }

    public void setmBeanServerFactory(final XmlClassRef mBeanServerFactory) {
        this.mBeanServerFactory = mBeanServerFactory;
    }

    public XmlClassRef getExecutionRepositoryFactory() {
        return executionRepositoryFactory;
    }

    public void setExecutionRepositoryFactory(final XmlClassRef executionRepositoryFactory) {
        this.executionRepositoryFactory = executionRepositoryFactory;
    }

    public XmlClassRef getMarshallerFactory() {
        return marshallerFactory;
    }

    public void setMarshallerFactory(final XmlClassRef marshallerFactory) {
        this.marshallerFactory = marshallerFactory;
    }

    public XmlClassRef getClassLoaderFactory() {
        return classLoaderFactory;
    }

    public void setClassLoaderFactory(final XmlClassRef classLoaderFactory) {
        this.classLoaderFactory = classLoaderFactory;
    }

    public XmlClassRef getTransactionManagerFactory() {
        return transactionManagerFactory;
    }

    public void setTransactionManagerFactory(final XmlClassRef transactionManagerFactory) {
        this.transactionManagerFactory = transactionManagerFactory;
    }

    public List<XmlClassRef> getJobLoaderFactories() {
        return jobLoaderFactories;
    }

    public void setJobLoaderFactories(final List<XmlClassRef> jobLoaderFactories) {
        this.jobLoaderFactories = jobLoaderFactories;
    }

    public List<XmlClassRef> getArtifactLoaderFactories() {
        return artifactLoaderFactories;
    }

    public void setArtifactLoaderFactories(final List<XmlClassRef> artifactLoaderFactories) {
        this.artifactLoaderFactories = artifactLoaderFactories;
    }

    public List<XmlClassRef> getInjectorFactories() {
        return injectorFactories;
    }

    public void setInjectorFactories(final List<XmlClassRef> injectorFactories) {
        this.injectorFactories = injectorFactories;
    }

    public List<XmlClassRef> getSecurityCheckFactories() {
        return securityCheckFactories;
    }

    public void setSecurityCheckFactories(final List<XmlClassRef> securityCheckFactories) {
        this.securityCheckFactories = securityCheckFactories;
    }

    public List<XmlProperty> getProperties() {
        return properties;
    }

    public void setProperties(final List<XmlProperty> properties) {
        this.properties = properties;
    }
}
