package uz.muxtoriyat.service.mapper;

import java.util.Objects;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uz.muxtoriyat.domain.Category;
import uz.muxtoriyat.domain.Source;
import uz.muxtoriyat.service.dto.CategoryDTO;
import uz.muxtoriyat.service.dto.SourceDTO;
import uz.muxtoriyat.service.dto.view.FileViewDTO;
import uz.muxtoriyat.service.dto.view.SourceViewDTO;

/**
 * Mapper for the entity {@link Source} and its DTO {@link SourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface SourceMapper extends EntityMapper<SourceDTO, Source> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    SourceDTO toDto(Source s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    default FileViewDTO getFileAsView(Source source) {
        if (Objects.isNull(source)) {
            return null;
        }
        FileViewDTO fileViewDTO = new FileViewDTO();
        fileViewDTO.setUrl(source.getFileUrl());
        fileViewDTO.setFileType(source.getFileType());
        return fileViewDTO;
    }

    @Mapping(target = "file", expression = "java(getFileAsView(s))")
    SourceViewDTO toViewDto(Source s);
}
