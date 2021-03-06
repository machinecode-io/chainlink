package io.machinecode.chainlink.core.inject;

import io.machinecode.chainlink.core.base.BaseTest;
import io.machinecode.chainlink.core.util.Tccl;
import io.machinecode.chainlink.spi.inject.ArtifactLoader;
import io.machinecode.chainlink.spi.inject.ArtifactOfWrongTypeException;
import io.machinecode.chainlink.core.execution.artifact.batchlet.RunBatchlet;
import org.junit.Assert;
import org.junit.Test;

import javax.batch.api.Batchlet;
import javax.batch.api.Decider;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class ArtifactLoaderTest extends BaseTest {

    @Test
    public void testInject() throws Exception {
        final ArtifactLoader loader = configuration().getArtifactLoader();
        final ClassLoader cl = Tccl.get();

        final Batchlet fromId = loader.load("runBatchlet", Batchlet.class, cl);
        Assert.assertNotNull(fromId);

        final Batchlet fromClass = loader.load(RunBatchlet.class.getCanonicalName(), Batchlet.class, cl);
        Assert.assertNotNull(fromClass);

        final Batchlet notAThing = loader.load("not a real thing", Batchlet.class, cl);
        Assert.assertNull(notAThing);

        try {
            loader.load("runBatchlet", Decider.class, cl);
            Assert.fail();
        } catch (final ArtifactOfWrongTypeException e) {
            //
        }
        try {
            loader.load(RunBatchlet.class.getCanonicalName(), Decider.class, cl);
            Assert.fail();
        } catch (final ArtifactOfWrongTypeException e) {
            //
        }
    }
}
