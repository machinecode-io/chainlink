package io.machinecode.chainlink.ee.tomee;

import io.machinecode.chainlink.core.configuration.ConfigurationImpl;
import io.machinecode.chainlink.core.configuration.xml.XmlConfiguration;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class TomEEConfigutation extends ConfigurationImpl {

    public TomEEConfigutation(final Builder builder) throws Exception {
        super(builder);
    }

    public static Builder xmlToBuilder(final XmlConfiguration xml) {
        return configureBuilder(new Builder(), xml);
    }


    public static class Builder extends _Builder<Builder> {
        @Override
        public TomEEConfigutation build() throws Exception {
            return new TomEEConfigutation(this);
        }
    }
}