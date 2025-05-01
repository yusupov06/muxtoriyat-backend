package uz.muxtoriyat.service;

import java.util.Optional;
import uz.muxtoriyat.service.dto.CategoryDTO;

/**
 * Service Interface for managing {@link uz.muxtoriyat.domain.Category}.
 */
public interface CategoryService {
    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save.
     * @return the persisted entity.
     */
    CategoryDTO save(CategoryDTO categoryDTO);

    /**
     * Updates a category.
     *
     * @param categoryDTO the entity to update.
     * @return the persisted entity.
     */
    CategoryDTO update(CategoryDTO categoryDTO);

    /**
     * Partially updates a category.
     *
     * @param categoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoryDTO> partialUpdate(CategoryDTO categoryDTO);

    /**
     * Get the "id" category.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoryDTO> findOne(Long id);

    /**
     * Delete the "id" category.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
