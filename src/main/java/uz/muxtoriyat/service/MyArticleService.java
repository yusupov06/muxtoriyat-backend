package uz.muxtoriyat.service;

import jakarta.validation.Valid;
import uz.muxtoriyat.service.dto.view.ArticleAddDTO;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;

public interface MyArticleService {
    ArticleViewDTO addArticle(@Valid ArticleAddDTO articleAddDTO);
}
