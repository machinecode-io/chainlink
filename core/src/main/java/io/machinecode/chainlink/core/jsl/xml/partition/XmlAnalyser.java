package io.machinecode.chainlink.core.jsl.xml.partition;

import io.machinecode.chainlink.core.jsl.xml.XmlPropertyReference;
import io.machinecode.chainlink.spi.jsl.partition.Analyser;

import javax.xml.bind.annotation.XmlAccessorType;

import static javax.xml.bind.annotation.XmlAccessType.NONE;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
@XmlAccessorType(NONE)
//@XmlType(name = "Analyser", propOrder = {
//        "properties"
//})
public class XmlAnalyser extends XmlPropertyReference<XmlAnalyser> implements Analyser {

    @Override
    public XmlAnalyser copy() {
        return copy(new XmlAnalyser());
    }
}
