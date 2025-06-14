package uz.muxtoriyat.service;

import java.util.List;
import java.util.Optional;
import uz.muxtoriyat.service.dto.view.ArticleBasicViewDTO;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;

public interface ArticleViewService {
    Optional<ArticleViewDTO> findOneAsView(Long id);

    ArticleViewDTO fillReactions(ArticleViewDTO articleViewDTO);

    List<ArticleViewDTO> fillReactions(List<ArticleViewDTO> articleViews);

    Optional<ArticleViewDTO> findOneByAuthorIdAsView(Long authorId, Long id);

    List<ArticleBasicViewDTO> fillBasicReactions(List<ArticleBasicViewDTO> articleViews);
}
