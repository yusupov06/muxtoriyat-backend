package uz.muxtoriyat.service;

import java.util.List;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;
import uz.muxtoriyat.service.dto.view.SourceViewDTO;

public interface ArticleViewService {
    List<ArticleViewDTO> fillReactions(List<ArticleViewDTO> articleViews);
}
