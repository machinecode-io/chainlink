package io.machinecode.chainlink.transport.infinispan.configuration;

import org.infinispan.commands.module.ExtendedModuleCommandFactory;
import org.infinispan.commands.module.ModuleCommandExtensions;
import org.infinispan.commands.module.ModuleCommandInitializer;

/**
 * From ServiceLoader
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 */
public class ChainlinkModuleCommandExtensions implements ModuleCommandExtensions {

    @Override
    public ExtendedModuleCommandFactory getModuleCommandFactory() {
        return new ChainlinkModuleCommandFactory();
    }

    @Override
    public ModuleCommandInitializer getModuleCommandInitializer() {
        return new ChainlinkModuleCommandInitializer();
    }
}
