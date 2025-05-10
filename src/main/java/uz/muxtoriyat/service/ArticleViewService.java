package uz.muxtoriyat.service;

import java.util.List;
import uz.muxtoriyat.service.dto.view.ArticleBasicViewDTO;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;

public interface ArticleViewService {
    List<ArticleViewDTO> fillReactions(List<ArticleViewDTO> articleViews);

    List<ArticleBasicViewDTO> fillBasicReactions(List<ArticleBasicViewDTO> articleViews);

    ArticleViewDTO fillReactions(ArticleViewDTO articleViewDTO);
}
