package io.machinecode.chainlink.repository.mongo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.machinecode.chainlink.core.repository.PartitionExecutionImpl;
import org.bson.types.ObjectId;

import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.Metric;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * @author <a href="mailto:brent.n.douglas@gmail.com">Brent Douglas</a>
 * @since 1.0
 */
@JsonAutoDetect(
        getterVisibility = NONE,
        isGetterVisibility = NONE,
        setterVisibility = NONE,
        fieldVisibility = NONE,
        creatorVisibility = NONE
)
@JsonDeserialize(builder = MongoPartitionExecution.Builder.class)
public class MongoPartitionExecution extends PartitionExecutionImpl {
    private static final long serialVersionUID = 1L;

    private final ObjectId _id;

    public MongoPartitionExecution(final _Builder builder) {
        super(builder);
        this._id = builder._id;
    }

    @JsonProperty(Fields._ID)
    public ObjectId get_id() {
        return _id;
    }

    @Override
    @JsonProperty(Fields.PARTITION_EXECUTION_ID)
    public long getPartitionExecutionId() {
        return super.getPartitionExecutionId();
    }

    @Override
    @JsonProperty(Fields.STEP_EXECUTION_ID)
    public long getStepExecutionId() {
        return super.getStepExecutionId();
    }

    @Override
    @JsonProperty(Fields.PARTITION_ID)
    public int getPartitionId() {
        return super.getPartitionId();
    }

    @Override
    @JsonProperty(Fields.BATCH_STATUS)
    public BatchStatus getBatchStatus() {
        return super.getBatchStatus();
    }

    @Override
    @JsonProperty(Fields.CREATE_TIME)
    public Date getCreateTime() {
        return super.getCreateTime();
    }

    @Override
    @JsonProperty(Fields.START_TIME)
    public Date getStartTime() {
        return super.getStartTime();
    }

    @Override
    @JsonProperty(Fields.UPDATED_TIME)
    public Date getUpdatedTime() {
        return super.getUpdatedTime();
    }

    @Override
    @JsonProperty(Fields.END_TIME)
    public Date getEndTime() {
        return super.getEndTime();
    }

    @Override
    @JsonProperty(Fields.EXIT_STATUS)
    public String getExitStatus() {
        return super.getExitStatus();
    }

    @Override
    @JsonProperty(Fields.METRICS)
    public Metric[] getMetrics() {
        return super.getMetrics();
    }

    @Override
    @JsonIgnore
    public Properties getPartitionParameters() {
        return super.getPartitionParameters();
    }

    @JsonProperty(Fields.PARTITION_PARAMETERS)
    public MongoProperties getPartitionProperties() {
        final Properties properties = super.getPartitionParameters();
        return properties == null ? null : new MongoProperties(properties);
    }

    @Override
    @JsonProperty(Fields.PERSISTENT_USER_DATA)
    @JsonSerialize(using = BytesSerializer.class)
    public Serializable getPersistentUserData() {
        return super.getPersistentUserData();
    }

    @Override
    @JsonProperty(Fields.READER_CHECKPOINT)
    @JsonSerialize(using = BytesSerializer.class)
    public Serializable getReaderCheckpoint() {
        return super.getReaderCheckpoint();
    }

    @Override
    @JsonProperty(Fields.WRITER_CHECKPOINT)
    @JsonSerialize(using = BytesSerializer.class)
    public Serializable getWriterCheckpoint() {
        return super.getWriterCheckpoint();
    }

    @Override
    protected void _toString(final StringBuilder sb) {
        super._toString(sb);
        sb.append(", _id=").append(_id);
    }

    public static Builder from(final MongoPartitionExecution that) {
        final Builder builder = new Builder();
        _from(builder, that);
        return builder;
    }

    protected static void  _from(final _Builder<?> builder, final MongoPartitionExecution that) {
        builder._id = that._id;
        PartitionExecutionImpl._from(builder, that);
    }

    @JsonAutoDetect(
            getterVisibility = NONE,
            isGetterVisibility = NONE,
            setterVisibility = NONE,
            fieldVisibility = NONE,
            creatorVisibility = NONE
    )
    @SuppressWarnings("unchecked")
    public abstract static class _Builder<T extends _Builder<T>> extends PartitionExecutionImpl._Builder<T> {
        private ObjectId _id;

        @JsonProperty(Fields._ID)
        public T set_id(final ObjectId _id) {
            this._id = _id;
            return (T)this;
        }

        @Override
        @JsonProperty(Fields.PARTITION_EXECUTION_ID)
        public T setPartitionExecutionId(final long partitionExecutionId) {
            return super.setPartitionExecutionId(partitionExecutionId);
        }

        @Override
        @JsonProperty(Fields.STEP_EXECUTION_ID)
        public T setStepExecutionId(final long stepExecutionId) {
            return super.setStepExecutionId(stepExecutionId);
        }

        @Override
        @JsonProperty(Fields.PARTITION_ID)
        public T setPartitionId(final int partitionId) {
            return super.setPartitionId(partitionId);
        }

        @Override
        @JsonProperty(Fields.BATCH_STATUS)
        public T setBatchStatus(final BatchStatus batchStatus) {
            return super.setBatchStatus(batchStatus);
        }

        @Override
        @JsonProperty(Fields.CREATE_TIME)
        public T setCreateTime(final Date create) {
            return super.setCreateTime(create);
        }

        @Override
        @JsonProperty(Fields.START_TIME)
        public T setStartTime(final Date start) {
            return super.setStartTime(start);
        }

        @Override
        @JsonProperty(Fields.UPDATED_TIME)
        public T setUpdatedTime(final Date updated) {
            return super.setUpdatedTime(updated);
        }

        @Override
        @JsonProperty(Fields.END_TIME)
        public T setEndTime(final Date end) {
            return super.setEndTime(end);
        }

        @Override
        @JsonProperty(Fields.EXIT_STATUS)
        public T setExitStatus(final String exitStatus) {
            return super.setExitStatus(exitStatus);
        }

        @Override
        @JsonProperty(Fields.METRICS)
        @JsonDeserialize(contentAs = MongoMetric.class)
        public T setMetrics(final Metric[] metrics) {
            return super.setMetrics(metrics);
        }

        @Override
        @JsonIgnore
        public T setPartitionParameters(final Properties partitionParameters) {
            return super.setPartitionParameters(partitionParameters);
        }

        @JsonProperty(Fields.PARTITION_PARAMETERS)
        public T setPartitionProperties(final MongoProperties properties) {
            return super.setPartitionParameters(properties == null ? null : properties.toProperties());
        }

        @Override
        @JsonProperty(Fields.PERSISTENT_USER_DATA)
        @JsonDeserialize(using = BytesDeserializer.class)
        public T setPersistentUserData(final Serializable persistentUserData) {
            return super.setPersistentUserData(persistentUserData);
        }

        @Override
        @JsonProperty(Fields.READER_CHECKPOINT)
        @JsonDeserialize(using = BytesDeserializer.class)
        public T setReaderCheckpoint(final Serializable reader) {
            return super.setReaderCheckpoint(reader);
        }

        @Override
        @JsonProperty(Fields.WRITER_CHECKPOINT)
        @JsonDeserialize(using = BytesDeserializer.class)
        public T setWriterCheckpoint(final Serializable writer) {
            return super.setWriterCheckpoint(writer);
        }
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder extends _Builder<Builder> {
        @Override
        public MongoPartitionExecution build() {
            return new MongoPartitionExecution(this);
        }
    }
}
