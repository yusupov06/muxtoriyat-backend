package uz.muxtoriyat.service;

import java.util.Optional;
import uz.muxtoriyat.service.dto.ReactionDTO;

/**
 * Service Interface for managing {@link uz.muxtoriyat.domain.Reaction}.
 */
public interface ReactionService {
    /**
     * Save a reaction.
     *
     * @param reactionDTO the entity to save.
     * @return the persisted entity.
     */
    ReactionDTO save(ReactionDTO reactionDTO);

    /**
     * Updates a reaction.
     *
     * @param reactionDTO the entity to update.
     * @return the persisted entity.
     */
    ReactionDTO update(ReactionDTO reactionDTO);

    /**
     * Partially updates a reaction.
     *
     * @param reactionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReactionDTO> partialUpdate(ReactionDTO reactionDTO);

    /**
     * Get the "id" reaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReactionDTO> findOne(Long id);

    /**
     * Delete the "id" reaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
