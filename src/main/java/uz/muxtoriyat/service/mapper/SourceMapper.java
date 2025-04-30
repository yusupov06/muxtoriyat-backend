package uz.muxtoriyat.service.mapper;

import org.mapstruct.*;
import uz.muxtoriyat.domain.Category;
import uz.muxtoriyat.domain.Source;
import uz.muxtoriyat.service.dto.CategoryDTO;
import uz.muxtoriyat.service.dto.SourceDTO;

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
}
