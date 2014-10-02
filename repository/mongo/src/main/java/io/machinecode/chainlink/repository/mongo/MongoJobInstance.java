package io.machinecode.chainlink.repository.mongo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.machinecode.chainlink.repository.core.JobInstanceImpl;
import org.bson.types.ObjectId;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * @author Brent Douglas <brent.n.douglas@gmail.com>
 */
@JsonAutoDetect(
        getterVisibility = NONE,
        isGetterVisibility = NONE,
        setterVisibility = NONE,
        fieldVisibility = NONE,
        creatorVisibility = NONE
)
@JsonDeserialize(builder = MongoJobInstance.Builder.class)
public class MongoJobInstance extends JobInstanceImpl {

    private final ObjectId _id;

    public MongoJobInstance(final _Builder builder) {
        super(builder);
        this._id = builder._id;
    }

    @JsonProperty(Fields._ID)
    public ObjectId get_id() {
        return _id;
    }

    @Override
    @JsonProperty(Fields.JOB_INSTANCE_ID)
    public long getInstanceId() {
        return super.getInstanceId();
    }

    @Override
    @JsonProperty(Fields.JOB_NAME)
    public String getJobName() {
        return super.getJobName();
    }

    @Override
    @JsonProperty(Fields.JSL_NAME)
    public String getJslName() {
        return super.getJslName();
    }

    @Override
    @JsonProperty(Fields.CREATE_TIME)
    public Date getCreateTime() {
        return super.getCreateTime();
    }

    @Override
    protected void _toString(final StringBuilder sb) {
        super._toString(sb);
        sb.append(", _id=").append(_id);
    }

    @JsonAutoDetect(
            getterVisibility = NONE,
            isGetterVisibility = NONE,
            setterVisibility = NONE,
            fieldVisibility = NONE,
            creatorVisibility = NONE
    )
    @SuppressWarnings("unchecked")
    public static class _Builder<T extends _Builder<T>> extends JobInstanceImpl._Builder<T> {
        ObjectId _id;

        @JsonProperty(Fields._ID)
        public T set_id(final ObjectId _id) {
            this._id = _id;
            return (T)this;
        }

        @Override
        @JsonProperty(Fields.JOB_INSTANCE_ID)
        public T setInstanceId(final long instanceId) {
            return super.setInstanceId(instanceId);
        }

        @Override
        @JsonProperty(Fields.JOB_NAME)
        public T setJobName(final String jobName) {
            return super.setJobName(jobName);
        }

        @Override
        @JsonProperty(Fields.JSL_NAME)
        public T setJslName(final String jslName) {
            return super.setJslName(jslName);
        }

        @Override
        @JsonProperty(Fields.CREATE_TIME)
        public T setCreateTime(final Date createTime) {
            return super.setCreateTime(createTime);
        }
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder extends _Builder<Builder> {
        public MongoJobInstance build() {
            return new MongoJobInstance(this);
        }
    }
}
