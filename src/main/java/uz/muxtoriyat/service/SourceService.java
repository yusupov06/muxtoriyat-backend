package uz.muxtoriyat.service;

import java.util.Optional;
import uz.muxtoriyat.service.dto.SourceDTO;
import uz.muxtoriyat.service.dto.view.SourceViewDTO;

/**
 * Service Interface for managing {@link uz.muxtoriyat.domain.Source}.
 */
public interface SourceService {
    /**
     * Save a source.
     *
     * @param sourceDTO the entity to save.
     * @return the persisted entity.
     */
    SourceDTO save(SourceDTO sourceDTO);

    /**
     * Updates a source.
     *
     * @param sourceDTO the entity to update.
     * @return the persisted entity.
     */
    SourceDTO update(SourceDTO sourceDTO);

    /**
     * Partially updates a source.
     *
     * @param sourceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SourceDTO> partialUpdate(SourceDTO sourceDTO);

    /**
     * Get the "id" source.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SourceDTO> findOne(Long id);

    Optional<SourceViewDTO> findOneAsView(Long id);

    /**
     * Delete the "id" source.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
