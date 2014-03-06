package io.machinecode.chainlink.test.seam;

import io.machinecode.chainlink.core.configuration.ConfigurationImpl.Builder;
import io.machinecode.chainlink.repository.memory.MemoryExecutionRepository;
import io.machinecode.chainlink.seam.SeamArtifactLoader;
import io.machinecode.chainlink.spi.ExecutionRepository;
import io.machinecode.chainlink.test.core.execution.ExecutorTest;
import org.jboss.seam.contexts.Lifecycle;
import org.jboss.seam.contexts.ServletLifecycle;
import org.jboss.seam.init.Initialization;
import org.jboss.seam.mock.MockServletContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.servlet.ServletContext;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class SeamLocalExecutorTest extends ExecutorTest {

    @Override
    protected Builder _configuration() {
        return super._configuration()
                .setArtifactLoaders(SeamArtifactLoader.inject("seamArtifactLoader", SeamArtifactLoader.class));
    }
    @Override
    protected ExecutionRepository _repository() {
        return new MemoryExecutionRepository();
    }

    @BeforeClass
    public static void beforeClass() {
        final ServletContext context = new MockServletContext();
        ServletLifecycle.beginApplication(context);
        new Initialization(context).create().init();
    }

    @AfterClass
    public static void AfterClass() {
        Lifecycle.endApplication();
    }
}
