package uz.muxtoriyat.service.mapper;

import java.util.List;
import java.util.Objects;
import org.mapstruct.*;
import uz.muxtoriyat.domain.Category;
import uz.muxtoriyat.domain.File;
import uz.muxtoriyat.domain.Source;
import uz.muxtoriyat.service.dto.CategoryDTO;
import uz.muxtoriyat.service.dto.FileDTO;
import uz.muxtoriyat.service.dto.SourceDTO;

/**
 * Mapper for the entity {@link Source} and its DTO {@link SourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface SourceMapper extends EntityMapper<SourceDTO, Source> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    @Mapping(target = "file", expression = "java(getFile(s))")
    SourceDTO toDto(Source s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    default FileDTO getFile(Source source) {
        if (Objects.isNull(source) || Objects.isNull(source.getFiles()) || source.getFiles().isEmpty()) {
            return null;
        }
        List<File> files = source.getFiles();
        return mapToFileDTO(files.get(0));
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "url", source = "url")
    FileDTO mapToFileDTO(File file);
}
