package uz.muxtoriyat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import uz.muxtoriyat.domain.enumeration.FileType;

/**
 * Criteria class for the {@link uz.muxtoriyat.domain.Source} entity. This class is used
 * in {@link uz.muxtoriyat.web.rest.SourceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sources?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Setter
@Getter
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SourceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FileType
     */
    public static class FileTypeFilter extends Filter<FileType> {

        public FileTypeFilter() {}

        public FileTypeFilter(FileTypeFilter filter) {
            super(filter);
        }

        @Override
        public FileTypeFilter copy() {
            return new FileTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private StringFilter fileUrl;

    private FileTypeFilter fileType;

    private LongFilter categoryId;

    private Boolean distinct;

    public SourceCriteria() {}

    public SourceCriteria(SourceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.fileUrl = other.optionalFileUrl().map(StringFilter::copy).orElse(null);
        this.fileType = other.optionalFileType().map(FileTypeFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SourceCriteria copy() {
        return new SourceCriteria(this);
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public Optional<StringFilter> optionalFileUrl() {
        return Optional.ofNullable(fileUrl);
    }

    public StringFilter fileUrl() {
        if (fileUrl == null) {
            setFileUrl(new StringFilter());
        }
        return fileUrl;
    }

    public Optional<FileTypeFilter> optionalFileType() {
        return Optional.ofNullable(fileType);
    }

    public FileTypeFilter fileType() {
        if (fileType == null) {
            setFileType(new FileTypeFilter());
        }
        return fileType;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SourceCriteria that = (SourceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(fileUrl, that.fileUrl) &&
            Objects.equals(fileType, that.fileType) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, fileUrl, fileType, categoryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SourceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalFileUrl().map(f -> "fileUrl=" + f + ", ").orElse("") +
            optionalFileType().map(f -> "fileType=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
