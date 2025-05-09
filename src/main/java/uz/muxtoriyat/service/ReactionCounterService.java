package uz.muxtoriyat.service;

import java.util.Optional;
import uz.muxtoriyat.domain.enumeration.ReactionType;
import uz.muxtoriyat.service.dto.ReactionDTO;
import uz.muxtoriyat.service.dto.request.CreateReactionRequest;

public interface ReactionCounterService {
    Long countReactions(Long targetId, ReactionType reactionType);
}
