package uz.muxtoriyat.service.dto.view;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link uz.muxtoriyat.domain.Category} entity.
 */
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoryViewDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Integer order;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryViewDTO)) {
            return false;
        }

        CategoryViewDTO categoryDTO = (CategoryViewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryViewDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
