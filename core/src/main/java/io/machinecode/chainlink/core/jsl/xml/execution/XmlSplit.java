package io.machinecode.chainlink.core.jsl.xml.execution;

import io.machinecode.chainlink.spi.jsl.inherit.execution.InheritableSplit;
import io.machinecode.chainlink.spi.loader.InheritableJobLoader;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import java.util.ArrayList;
import java.util.List;

import static io.machinecode.chainlink.spi.jsl.Job.NAMESPACE;
import static javax.xml.bind.annotation.XmlAccessType.NONE;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
@XmlAccessorType(NONE)
//@XmlType(name = "Split", propOrder = {
//        "flows"
//})
public class XmlSplit implements XmlExecution<XmlSplit>, InheritableSplit<XmlSplit, XmlFlow> {

    @XmlID
    @XmlSchemaType(name = "ID")
    @XmlAttribute(name = "id", required = true)
    private String id;

    @XmlAttribute(name = "next", required = true)
    private String next;

    @XmlElement(name = "flow", namespace = NAMESPACE, required = false)
    private List<XmlFlow> flows = new ArrayList<XmlFlow>();


    @Override
    public String getId() {
        return id;
    }

    public XmlSplit setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String getNext() {
        return next;
    }

    public XmlSplit setNext(final String next) {
        this.next = next;
        return this;
    }

    @Override
    public List<XmlFlow> getFlows() {
        return flows;
    }

    public XmlSplit setFlows(final List<XmlFlow> flows) {
        this.flows = flows;
        return this;
    }

    @Override
    public XmlSplit inherit(final InheritableJobLoader repository, final String defaultJobXml) {
        return SplitTool.inherit(this, repository, defaultJobXml);
    }

    @Override
    public XmlSplit copy() {
        return copy(new XmlSplit());
    }

    @Override
    public XmlSplit copy(final XmlSplit that) {
        return SplitTool.copy(this, that);
    }
}
