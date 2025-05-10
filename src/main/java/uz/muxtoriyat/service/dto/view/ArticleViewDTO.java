package uz.muxtoriyat.service.dto.view;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uz.muxtoriyat.service.dto.AbstractAuditingDTO;

@Setter
@Getter
@ToString
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleViewDTO extends AbstractAuditingDTO implements Serializable {

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

    private long views;

    private long likes;

    private long disLikes;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArticleViewDTO)) {
            return false;
        }

        ArticleViewDTO articleDTO = (ArticleViewDTO) o;
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
