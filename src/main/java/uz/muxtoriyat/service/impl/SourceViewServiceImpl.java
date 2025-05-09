package uz.muxtoriyat.service.impl;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.muxtoriyat.domain.enumeration.ReactionType;
import uz.muxtoriyat.service.ReactionCounterService;
import uz.muxtoriyat.service.SourceViewService;
import uz.muxtoriyat.service.dto.view.SourceViewDTO;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceViewServiceImpl implements SourceViewService {

    private final ReactionCounterService reactionCounterService;

    @Override
    public List<SourceViewDTO> fillReactions(List<SourceViewDTO> sourceViews) {
        sourceViews.forEach(this::fillReactions);
        return sourceViews;
    }

    private void fillReactions(SourceViewDTO sourceViewDTO) {
        Long likes = reactionCounterService.countReactions(sourceViewDTO.getId(), ReactionType.LIKE);
        sourceViewDTO.setLikes(safeParse(likes));
        Long views = reactionCounterService.countReactions(sourceViewDTO.getId(), ReactionType.VIEW);
        sourceViewDTO.setViews(safeParse(views));
    }

    private long safeParse(Long likes) {
        if (Objects.isNull(likes)) return 0;
        return likes;
    }
}
