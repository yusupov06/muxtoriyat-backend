package uz.muxtoriyat.service.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import uz.muxtoriyat.domain.enumeration.ReactionType;

/**
 * A DTO for the {@link uz.muxtoriyat.domain.Reaction} entity.
 */
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReactionDTO implements Serializable {

    private Long id;

    private String deviceId;

    private Long targetId;

    private ReactionType reactionType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReactionDTO reactionDTO)) {
            return false;
        }

        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReactionDTO{" +
            "id=" + getId() +
            ", deviceId='" + getDeviceId() + "'" +
            ", targetId=" + getTargetId() +
            ", reactionType='" + getReactionType() + "'" +
            "}";
    }
}
