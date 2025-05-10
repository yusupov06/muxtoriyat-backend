package uz.muxtoriyat.service.impl;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.muxtoriyat.domain.enumeration.ReactionType;
import uz.muxtoriyat.service.ArticleViewService;
import uz.muxtoriyat.service.ReactionCounterService;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;

@Service
@RequiredArgsConstructor
public class ArticleViewServiceImpl implements ArticleViewService {

    private final ReactionCounterService reactionCounterService;

    @Override
    public List<ArticleViewDTO> fillReactions(List<ArticleViewDTO> articleViews) {
        articleViews.forEach(this::fillReactions);
        return articleViews;
    }

    public ArticleViewDTO fillReactions(ArticleViewDTO articleViewDTO) {
        Long likes = reactionCounterService.countReactions(articleViewDTO.getId(), ReactionType.LIKE);
        articleViewDTO.setLikes(safeParse(likes));
        Long views = reactionCounterService.countReactions(articleViewDTO.getId(), ReactionType.VIEW);
        articleViewDTO.setViews(safeParse(views));
        return articleViewDTO;
    }

    private long safeParse(Long likes) {
        if (Objects.isNull(likes)) return 0;
        return likes;
    }
}
