package uz.muxtoriyat.service;

import java.util.Optional;
import uz.muxtoriyat.service.dto.ArticleDTO;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;

/**
 * Service Interface for managing {@link uz.muxtoriyat.domain.Article}.
 */
public interface ArticleService {
    /**
     * Save a article.
     *
     * @param articleDTO the entity to save.
     * @return the persisted entity.
     */
    ArticleDTO save(ArticleDTO articleDTO);

    /**
     * Updates a article.
     *
     * @param articleDTO the entity to update.
     * @return the persisted entity.
     */
    ArticleDTO update(ArticleDTO articleDTO);

    /**
     * Partially updates a article.
     *
     * @param articleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ArticleDTO> partialUpdate(ArticleDTO articleDTO);

    /**
     * Get the "id" article.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArticleDTO> findOne(Long id);

    Optional<ArticleViewDTO> findOneAsView(Long id);

    /**
     * Delete the "id" article.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
