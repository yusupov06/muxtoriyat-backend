package uz.muxtoriyat.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import uz.muxtoriyat.domain.enumeration.VisibilityType;

/**
 * A DTO for the {@link uz.muxtoriyat.domain.Article} entity.
 */
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String title;

    private String description;

    @Lob
    private String content;

    @Lob
    private byte[] image;

    private String imageContentType;

    private VisibilityType visibility;

    private CategoryDTO category;

    private UserDTO author;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleDTO)) {
            return false;
        }

        ArticleDTO articleDTO = (ArticleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, articleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", content='" + getContent() + "'" +
            ", image='" + getImage() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", category=" + getCategory() +
            ", author=" + getAuthor() +
            "}";
    }
}
