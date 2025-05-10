package uz.muxtoriyat.service.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link uz.muxtoriyat.domain.Source} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
public class SourceDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private CategoryDTO category;

    private FileDTO file;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceDTO)) {
            return false;
        }

        SourceDTO sourceDTO = (SourceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SourceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", category=" + getCategory() +
            "}";
    }
}
