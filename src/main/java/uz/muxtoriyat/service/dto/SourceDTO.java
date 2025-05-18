package uz.muxtoriyat.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import uz.muxtoriyat.domain.enumeration.FileType;

/**
 * A DTO for the {@link uz.muxtoriyat.domain.Source} entity.
 */
@Setter
@Getter
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SourceDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    @Lob
    private byte[] image;

    private String imageContentType;

    private String fileUrl;

    @Lob
    private byte[] fileContent;

    private String fileContentContentType;

    private FileType fileType;

    private CategoryDTO category;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceDTO sourceDTO)) {
            return false;
        }

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
            ", image='" + getImage() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", fileContent='" + getFileContent() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", category=" + getCategory() +
            "}";
    }
}
