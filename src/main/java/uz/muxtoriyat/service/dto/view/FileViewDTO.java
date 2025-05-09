package uz.muxtoriyat.service.dto.view;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uz.muxtoriyat.domain.enumeration.FileType;

/**
 * A DTO for the {@link uz.muxtoriyat.domain.File} entity.
 */
@Setter
@Getter
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileViewDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    private String url;

    private FileType fileType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileViewDTO)) {
            return false;
        }

        FileViewDTO fileDTO = (FileViewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
