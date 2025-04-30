package uz.muxtoriyat.service.mapper;

import org.mapstruct.*;
import uz.muxtoriyat.domain.Article;
import uz.muxtoriyat.domain.Reaction;
import uz.muxtoriyat.service.dto.ArticleDTO;
import uz.muxtoriyat.service.dto.ReactionDTO;

/**
 * Mapper for the entity {@link Reaction} and its DTO {@link ReactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReactionMapper extends EntityMapper<ReactionDTO, Reaction> {
    @Mapping(target = "article", source = "article", qualifiedByName = "articleId")
    ReactionDTO toDto(Reaction s);

    @Named("articleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ArticleDTO toDtoArticleId(Article article);
}
