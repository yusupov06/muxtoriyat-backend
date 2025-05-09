package uz.muxtoriyat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import uz.muxtoriyat.domain.enumeration.ReactionType;

/**
 * Criteria class for the {@link uz.muxtoriyat.domain.Reaction} entity. This class is used
 * in {@link uz.muxtoriyat.web.rest.ReactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReactionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ReactionType
     */
    public static class ReactionTypeFilter extends Filter<ReactionType> {

        public ReactionTypeFilter() {}

        public ReactionTypeFilter(ReactionTypeFilter filter) {
            super(filter);
        }

        @Override
        public ReactionTypeFilter copy() {
            return new ReactionTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter deviceId;

    private LongFilter targetId;

    private ReactionTypeFilter reactionType;

    private Boolean distinct;

    public ReactionCriteria() {}

    public ReactionCriteria(ReactionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.deviceId = other.optionalDeviceId().map(StringFilter::copy).orElse(null);
        this.targetId = other.optionalTargetId().map(LongFilter::copy).orElse(null);
        this.reactionType = other.optionalReactionType().map(ReactionTypeFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ReactionCriteria copy() {
        return new ReactionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDeviceId() {
        return deviceId;
    }

    public Optional<StringFilter> optionalDeviceId() {
        return Optional.ofNullable(deviceId);
    }

    public StringFilter deviceId() {
        if (deviceId == null) {
            setDeviceId(new StringFilter());
        }
        return deviceId;
    }

    public void setDeviceId(StringFilter deviceId) {
        this.deviceId = deviceId;
    }

    public LongFilter getTargetId() {
        return targetId;
    }

    public Optional<LongFilter> optionalTargetId() {
        return Optional.ofNullable(targetId);
    }

    public LongFilter targetId() {
        if (targetId == null) {
            setTargetId(new LongFilter());
        }
        return targetId;
    }

    public void setTargetId(LongFilter targetId) {
        this.targetId = targetId;
    }

    public ReactionTypeFilter getReactionType() {
        return reactionType;
    }

    public Optional<ReactionTypeFilter> optionalReactionType() {
        return Optional.ofNullable(reactionType);
    }

    public ReactionTypeFilter reactionType() {
        if (reactionType == null) {
            setReactionType(new ReactionTypeFilter());
        }
        return reactionType;
    }

    public void setReactionType(ReactionTypeFilter reactionType) {
        this.reactionType = reactionType;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReactionCriteria that = (ReactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(targetId, that.targetId) &&
            Objects.equals(reactionType, that.reactionType) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deviceId, targetId, reactionType, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReactionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDeviceId().map(f -> "deviceId=" + f + ", ").orElse("") +
            optionalTargetId().map(f -> "targetId=" + f + ", ").orElse("") +
            optionalReactionType().map(f -> "reactionType=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
