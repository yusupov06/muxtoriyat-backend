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

    private String url;
    private FileType fileType;
}
