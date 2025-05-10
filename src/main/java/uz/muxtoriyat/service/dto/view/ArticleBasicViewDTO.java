package uz.muxtoriyat.service.dto.view;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.ReadOnlyProperty;

@Setter
@Getter
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleBasicViewDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String title;

    private String description;

    @Lob
    private byte[] image;

    private String imageContentType;

    private long views;

    private long likes;

    private long disLikes;

    private UserViewDTO author;

    @ReadOnlyProperty
    private Instant createdDate = Instant.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleBasicViewDTO)) {
            return false;
        }

        ArticleBasicViewDTO articleDTO = (ArticleBasicViewDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, articleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
