package uz.muxtoriyat.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.muxtoriyat.service.dto.SourceDTO;

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
     * Get all the sources.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SourceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" source.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SourceDTO> findOne(Long id);

    /**
     * Delete the "id" source.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
