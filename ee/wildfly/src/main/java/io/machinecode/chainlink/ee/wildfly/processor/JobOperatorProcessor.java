package io.machinecode.chainlink.ee.wildfly.processor;

import io.machinecode.chainlink.core.configuration.SubSystemModelImpl;
import io.machinecode.chainlink.ee.wildfly.WildFlyConstants;
import io.machinecode.chainlink.ee.wildfly.WildFlyEnvironment;
import io.machinecode.chainlink.ee.wildfly.service.ChainlinkService;
import io.machinecode.chainlink.ee.wildfly.service.ConfigurationService;
import io.machinecode.chainlink.ee.wildfly.service.JobOperatorService;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.txn.service.TxnServices;
import org.jboss.dmr.ModelNode;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;

import javax.transaction.TransactionManager;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class JobOperatorProcessor implements DeploymentUnitProcessor {

    final boolean global;
    final String name;
    final ModelNode model;

    public JobOperatorProcessor(final boolean global, final String name, final ModelNode model) {
        this.global = global;
        this.name = name;
        this.model = model;
    }

    @Override
    public void deploy(final DeploymentPhaseContext deploymentPhaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = deploymentPhaseContext.getDeploymentUnit();
        final Module module = deploymentUnit.getAttachment(Attachments.MODULE);
        if (module == null) {
            throw new DeploymentUnitProcessingException(getClass().getName() + " run in wrong phase."); //TODO Message
        }
        final ClassLoader loader = module.getClassLoader();
        final ServiceName serviceName = deploymentUnit.getServiceName();

        final JobOperatorService service = new JobOperatorService(serviceName, loader, this.name, this.global, this.model);

        deploymentPhaseContext.getServiceTarget()
                .addService(serviceName.append(WildFlyConstants.JOB_OPERATOR).append(this.name), service)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .addDependency(ChainlinkService.SERVICE_NAME, WildFlyEnvironment.class, service.getEnvironment())
                .addDependency(TxnServices.JBOSS_TXN_TRANSACTION_MANAGER, TransactionManager.class, service.getTransactionManager())
                .addDependency(ConfigurationService.SERVICE_NAME, SubSystemModelImpl.class, service.getScope())
                .install();
    }

    @Override
    public void undeploy(final DeploymentUnit deploymentUnit) {
        // no op
    }
}
