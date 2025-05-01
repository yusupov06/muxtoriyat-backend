package uz.muxtoriyat.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import uz.muxtoriyat.domain.enumeration.FileType;

/**
 * Criteria class for the {@link uz.muxtoriyat.domain.File} entity. This class is used
 * in {@link uz.muxtoriyat.web.rest.FileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /files?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FileCriteria implements Serializable, Criteria {

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

    private StringFilter url;

    private FileTypeFilter fileType;

    private LongFilter sourceId;

    private Boolean distinct;

    public FileCriteria() {}

    public FileCriteria(FileCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.url = other.optionalUrl().map(StringFilter::copy).orElse(null);
        this.fileType = other.optionalFileType().map(FileTypeFilter::copy).orElse(null);
        this.sourceId = other.optionalSourceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FileCriteria copy() {
        return new FileCriteria(this);
    }

    public LongFilter getId() {
        return id;
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

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
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

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
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

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getUrl() {
        return url;
    }

    public Optional<StringFilter> optionalUrl() {
        return Optional.ofNullable(url);
    }

    public StringFilter url() {
        if (url == null) {
            setUrl(new StringFilter());
        }
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public FileTypeFilter getFileType() {
        return fileType;
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

    public void setFileType(FileTypeFilter fileType) {
        this.fileType = fileType;
    }

    public LongFilter getSourceId() {
        return sourceId;
    }

    public Optional<LongFilter> optionalSourceId() {
        return Optional.ofNullable(sourceId);
    }

    public LongFilter sourceId() {
        if (sourceId == null) {
            setSourceId(new LongFilter());
        }
        return sourceId;
    }

    public void setSourceId(LongFilter sourceId) {
        this.sourceId = sourceId;
    }

    public Boolean getDistinct() {
        return distinct;
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

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FileCriteria that = (FileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(url, that.url) &&
            Objects.equals(fileType, that.fileType) &&
            Objects.equals(sourceId, that.sourceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, url, fileType, sourceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalUrl().map(f -> "url=" + f + ", ").orElse("") +
            optionalFileType().map(f -> "fileType=" + f + ", ").orElse("") +
            optionalSourceId().map(f -> "sourceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
