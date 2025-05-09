package uz.muxtoriyat.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import uz.muxtoriyat.domain.enumeration.ReactionType;

/**
 * A Reaction.
 */
@Setter
@Getter
@Entity
@Table(name = "reaction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "target_id")
    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type")
    private ReactionType reactionType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Reaction id(Long id) {
        this.setId(id);
        return this;
    }

    public Reaction deviceId(String deviceId) {
        this.setDeviceId(deviceId);
        return this;
    }

    public Reaction targetId(Long targetId) {
        this.setTargetId(targetId);
        return this;
    }

    public Reaction reactionType(ReactionType reactionType) {
        this.setReactionType(reactionType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reaction)) {
            return false;
        }
        return getId() != null && getId().equals(((Reaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reaction{" +
            "id=" + getId() +
            ", deviceId='" + getDeviceId() + "'" +
            ", targetId=" + getTargetId() +
            ", reactionType='" + getReactionType() + "'" +
            "}";
    }
}
