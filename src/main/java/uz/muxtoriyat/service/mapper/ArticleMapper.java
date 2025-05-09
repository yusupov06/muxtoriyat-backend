package uz.muxtoriyat.service.mapper;

import org.mapstruct.*;
import uz.muxtoriyat.domain.Article;
import uz.muxtoriyat.domain.Category;
import uz.muxtoriyat.service.dto.ArticleDTO;
import uz.muxtoriyat.service.dto.CategoryDTO;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;

/**
 * Mapper for the entity {@link Article} and its DTO {@link ArticleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArticleMapper extends EntityMapper<ArticleDTO, Article> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    ArticleDTO toDto(Article s);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    ArticleViewDTO toViewDto(Article article);
}
