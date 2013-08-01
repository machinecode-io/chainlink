package io.machinecode.nock.jsl.validation.partition;

import io.machinecode.nock.jsl.api.partition.Partition;
import io.machinecode.nock.jsl.validation.ValidationContext;
import io.machinecode.nock.jsl.validation.Validator;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
public class PartitionValidator extends Validator<Partition> {

    public static final PartitionValidator INSTANCE = new PartitionValidator();

    protected PartitionValidator() {
        super("partition");
    }

    @Override
    public void doValidate(final Partition that, final ValidationContext context) {
        if (that.getReducer() != null) {
            PartitionReducerValidator.INSTANCE.validate(that.getReducer(), context);
        }
        if (that.getMapper() != null) {
            MapperValidator.validate(that.getMapper(), context);
        }
        if (that.getAnalyzer() != null) {
            AnalyserValidator.INSTANCE.validate(that.getAnalyzer(), context);
        }
        if (that.getCollector() != null) {
            CollectorValidator.INSTANCE.validate(that.getCollector(), context);
        }
    }
}
