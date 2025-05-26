package uz.muxtoriyat.service.dto.view;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.ReadOnlyProperty;

/**
 * A DTO for the {@link uz.muxtoriyat.domain.Source} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Getter
@Setter
@ToString
public class SourceViewDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    @Lob
    private byte[] image;

    private String imageContentType;

    private FileViewDTO file;

    private long views;

    private long likes;

    private long disLikes;

    @ReadOnlyProperty
    private Instant createdDate = Instant.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceViewDTO)) {
            return false;
        }

        SourceViewDTO sourceDTO = (SourceViewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sourceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
