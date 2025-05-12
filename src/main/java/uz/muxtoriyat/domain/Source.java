package uz.muxtoriyat.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * A Source.
 */
@Setter
@Getter
@Entity
@Table(name = "source")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Source extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "parent" }, allowSetters = true)
    private Category category;

    @OneToMany(mappedBy = "source")
    @JsonIgnoreProperties(value = { "source" }, allowSetters = true)
    private List<File> files;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Source id(Long id) {
        this.setId(id);
        return this;
    }

    public Source name(String name) {
        this.setName(name);
        return this;
    }

    public Source description(String description) {
        this.setDescription(description);
        return this;
    }

    public Source image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public Source imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public Source category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Source)) {
            return false;
        }
        return getId() != null && getId().equals(((Source) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Source{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
