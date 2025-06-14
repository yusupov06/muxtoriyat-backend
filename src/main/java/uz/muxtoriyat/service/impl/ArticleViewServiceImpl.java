package uz.muxtoriyat.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.muxtoriyat.domain.enumeration.ReactionType;
import uz.muxtoriyat.repository.ArticleRepository;
import uz.muxtoriyat.service.ArticleViewService;
import uz.muxtoriyat.service.ReactionCounterService;
import uz.muxtoriyat.service.dto.view.ArticleBasicViewDTO;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;
import uz.muxtoriyat.service.mapper.ArticleMapper;

@Service
@RequiredArgsConstructor
public class ArticleViewServiceImpl implements ArticleViewService {

    private final ArticleMapper articleMapper;
    private final ArticleRepository articleRepository;
    private final ReactionCounterService reactionCounterService;

    @Override
    public Optional<ArticleViewDTO> findOneAsView(Long id) {
        return articleRepository.findById(id).map(articleMapper::toViewDto);
    }

    @Override
    public Optional<ArticleViewDTO> findOneByAuthorIdAsView(Long id, Long authorId) {
        return articleRepository.findByIdAndAuthor_Id(id, authorId).map(articleMapper::toViewDto);
    }

    @Override
    public List<ArticleViewDTO> fillReactions(List<ArticleViewDTO> articleViews) {
        articleViews.forEach(this::fillReactions);
        return articleViews;
    }

    @Override
    public List<ArticleBasicViewDTO> fillBasicReactions(List<ArticleBasicViewDTO> articleViews) {
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

    public ArticleBasicViewDTO fillReactions(ArticleBasicViewDTO articleViewDTO) {
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
