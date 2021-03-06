package io.machinecode.chainlink.core.validation.partition;

import io.machinecode.chainlink.core.validation.PropertiesValidator;
import io.machinecode.chainlink.core.validation.visitor.ValidatingVisitor;
import io.machinecode.chainlink.core.validation.visitor.VisitorNode;
import io.machinecode.chainlink.spi.jsl.Properties;
import io.machinecode.chainlink.spi.jsl.partition.Plan;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
public class PlanValidator extends ValidatingVisitor<Plan> {

    public static final PlanValidator INSTANCE = new PlanValidator();

    protected PlanValidator() {
        super(Plan.ELEMENT);
    }

    @Override
    public void doVisit(final Plan that, final VisitorNode context) {
        if (that.getProperties() != null) {
            for (final Properties properties : that.getProperties()) {
                PropertiesValidator.INSTANCE.visit(properties, context);
            }
        }
        //if (that.getPartitions() < 0) {
        //    context.addProblem(Problem.attributePositive("partitions", that.getPartitions()));
        //}
        //This can be null and will get set in the PlanImpl constructor
        //if (that.getThreads() != null && that.getThreads() < 0) {
        //    context.addProblem(Problem.attributePositive("threads", that.getThreads()));
        //}
    }
}
