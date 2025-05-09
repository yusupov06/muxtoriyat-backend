package uz.muxtoriyat.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.muxtoriyat.domain.Reaction;
import uz.muxtoriyat.domain.enumeration.ReactionType;

/**
 * Spring Data JPA repository for the Reaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long>, JpaSpecificationExecutor<Reaction> {
    void deleteFirstByTargetIdAndReactionType(Long targetId, ReactionType reactionType);
    Reaction findFirstByTargetIdAndReactionType(Long targetId, ReactionType reactionType);

    long countReactionsByTargetIdAndReactionType(Long targetId, ReactionType reactionType);
}
