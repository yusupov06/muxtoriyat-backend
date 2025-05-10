package uz.muxtoriyat.service;

import java.util.List;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;

public interface ArticleViewService {
    List<ArticleViewDTO> fillReactions(List<ArticleViewDTO> articleViews);

    ArticleViewDTO fillReactions(ArticleViewDTO articleViewDTO);
}
