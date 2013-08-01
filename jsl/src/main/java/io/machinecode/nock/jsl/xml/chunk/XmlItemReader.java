package io.machinecode.nock.jsl.xml.chunk;

import io.machinecode.nock.jsl.api.chunk.ItemReader;
import io.machinecode.nock.jsl.xml.XmlPropertyReference;

import javax.xml.bind.annotation.XmlAccessorType;

import static javax.xml.bind.annotation.XmlAccessType.NONE;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
@XmlAccessorType(NONE)
public class XmlItemReader extends XmlPropertyReference<XmlItemReader> implements ItemReader {

    @Override
    public XmlItemReader copy() {
        return copy(new XmlItemReader());
    }
}
