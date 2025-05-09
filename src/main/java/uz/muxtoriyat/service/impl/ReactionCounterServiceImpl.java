package uz.muxtoriyat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.muxtoriyat.domain.enumeration.ReactionType;
import uz.muxtoriyat.repository.ReactionRepository;
import uz.muxtoriyat.service.ReactionCounterService;

@Service
@RequiredArgsConstructor
public class ReactionCounterServiceImpl implements ReactionCounterService {

    private final ReactionRepository reactionRepository;

    @Override
    public Long countReactions(Long targetId, ReactionType reactionType) {
        return reactionRepository.countReactionsByTargetIdAndReactionType(targetId, reactionType);
    }
}
