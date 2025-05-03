package uz.muxtoriyat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import uz.muxtoriyat.domain.enumeration.FileType;

/**
 * A File.
 */
@Setter
@Getter
@Entity
@Table(name = "file")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "url")
    private String url;

    @Lob
    @Column(name = "content")
    private byte[] content;

    @Column(name = "content_content_type")
    private String contentContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileType fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private Source source;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public File id(Long id) {
        this.setId(id);
        return this;
    }

    public File name(String name) {
        this.setName(name);
        return this;
    }

    public File description(String description) {
        this.setDescription(description);
        return this;
    }

    public File url(String url) {
        this.setUrl(url);
        return this;
    }

    public File content(byte[] content) {
        this.setContent(content);
        return this;
    }

    public File contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public File fileType(FileType fileType) {
        this.setFileType(fileType);
        return this;
    }

    public File source(Source source) {
        this.setSource(source);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        return getId() != null && getId().equals(((File) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "File{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            ", content='" + getContent() + "'" +
            ", contentContentType='" + getContentContentType() + "'" +
            ", fileType='" + getFileType() + "'" +
            "}";
    }
}
