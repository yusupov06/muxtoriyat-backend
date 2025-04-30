package uz.muxtoriyat.service.dto;

import java.io.Serializable;
import java.util.Objects;
import uz.muxtoriyat.domain.enumeration.ReactionType;

/**
 * A DTO for the {@link uz.muxtoriyat.domain.Reaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReactionDTO implements Serializable {

    private Long id;

    private String deviceId;

    private ReactionType reactionType;

    private ArticleDTO article;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ReactionType getReactionType() {
        return reactionType;
    }

    public void setReactionType(ReactionType reactionType) {
        this.reactionType = reactionType;
    }

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        this.article = article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReactionDTO)) {
            return false;
        }

        ReactionDTO reactionDTO = (ReactionDTO) o;
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
            ", reactionType='" + getReactionType() + "'" +
            ", article=" + getArticle() +
            "}";
    }
}
