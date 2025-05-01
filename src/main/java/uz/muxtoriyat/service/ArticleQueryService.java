package uz.muxtoriyat.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.muxtoriyat.domain.*; // for static metamodels
import uz.muxtoriyat.domain.Article;
import uz.muxtoriyat.repository.ArticleRepository;
import uz.muxtoriyat.service.criteria.ArticleCriteria;
import uz.muxtoriyat.service.dto.ArticleDTO;
import uz.muxtoriyat.service.mapper.ArticleMapper;

/**
 * Service for executing complex queries for {@link Article} entities in the database.
 * The main input is a {@link ArticleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ArticleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArticleQueryService extends QueryService<Article> {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleQueryService.class);

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    public ArticleQueryService(ArticleRepository articleRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    /**
     * Return a {@link Page} of {@link ArticleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArticleDTO> findByCriteria(ArticleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.findAll(specification, page).map(articleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArticleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Article> specification = createSpecification(criteria);
        return articleRepository.count(specification);
    }

    /**
     * Function to convert {@link ArticleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Article> createSpecification(ArticleCriteria criteria) {
        Specification<Article> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Article_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Article_.name));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Article_.title));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Article_.description));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root -> root.join(Article_.category, JoinType.LEFT).get(Category_.id))
                );
            }
        }
        return specification;
    }
}
