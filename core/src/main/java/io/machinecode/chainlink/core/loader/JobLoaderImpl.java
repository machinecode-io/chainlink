package io.machinecode.chainlink.core.loader;

import gnu.trove.set.hash.TLinkedHashSet;
import io.machinecode.chainlink.spi.Messages;
import io.machinecode.chainlink.spi.jsl.Job;
import io.machinecode.chainlink.spi.loader.JobLoader;
import org.jboss.logging.Logger;

import javax.batch.operations.NoSuchJobException;
import javax.xml.bind.JAXBException;
import java.util.Collections;
import java.util.Set;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class JobLoaderImpl implements JobLoader {

    private static final Logger log = Logger.getLogger(JobLoaderImpl.class);

    private final JarXmlJobLoader jarLoader;
    private final WarXmlJobLoader warLoader;
    private final Set<JobLoader> loaders;

    public JobLoaderImpl(final ClassLoader classLoader, final JobLoader... jobLoaders) throws JAXBException {
        this.jarLoader = new JarXmlJobLoader(classLoader);
        this.warLoader = new WarXmlJobLoader(classLoader);
        this.loaders = new TLinkedHashSet<>();
        Collections.addAll(this.loaders, jobLoaders);
    }

    @Override
    public Job load(final String jslName) throws NoSuchJobException {
        // 1. Provided Loaders
        for (final JobLoader loader : this.loaders) {
            try {
                return loader.load(jslName);
            } catch (final NoSuchJobException e) {
                log.tracef(Messages.get("CHAINLINK-003100.job.loader.not.found"), jslName, loader.getClass().getSimpleName());
            }
        }
        // 2. Archive Loaders
        try {
            return jarLoader.load(jslName);
        } catch (final NoSuchJobException e) {
            log.tracef(Messages.get("CHAINLINK-003100.job.loader.not.found"), jslName, jarLoader.getClass().getSimpleName());
        }
        return warLoader.load(jslName);
    }
}
