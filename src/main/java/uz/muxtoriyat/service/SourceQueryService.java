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
import uz.muxtoriyat.domain.Source;
import uz.muxtoriyat.repository.SourceRepository;
import uz.muxtoriyat.service.criteria.SourceCriteria;
import uz.muxtoriyat.service.dto.SourceDTO;
import uz.muxtoriyat.service.dto.view.SourceViewDTO;
import uz.muxtoriyat.service.mapper.SourceMapper;

/**
 * Service for executing complex queries for {@link Source} entities in the database.
 * The main input is a {@link SourceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SourceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SourceQueryService extends QueryService<Source> {

    private static final Logger LOG = LoggerFactory.getLogger(SourceQueryService.class);

    private final SourceRepository sourceRepository;

    private final SourceMapper sourceMapper;

    public SourceQueryService(SourceRepository sourceRepository, SourceMapper sourceMapper) {
        this.sourceRepository = sourceRepository;
        this.sourceMapper = sourceMapper;
    }

    /**
     * Return a {@link Page} of {@link SourceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SourceDTO> findByCriteria(SourceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Source> specification = createSpecification(criteria);
        return sourceRepository.findAll(specification, page).map(sourceMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<SourceViewDTO> findByCriteriaAsView(SourceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Source> specification = createSpecification(criteria);
        return sourceRepository.findAll(specification, page).map(sourceMapper::toViewDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SourceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Source> specification = createSpecification(criteria);
        return sourceRepository.count(specification);
    }

    /**
     * Function to convert {@link SourceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Source> createSpecification(SourceCriteria criteria) {
        Specification<Source> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Source_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Source_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Source_.description));
            }
            if (criteria.getFileUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUrl(), Source_.fileUrl));
            }
            if (criteria.getFileType() != null) {
                specification = specification.and(buildSpecification(criteria.getFileType(), Source_.fileType));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root -> root.join(Source_.category, JoinType.LEFT).get(Category_.id))
                );
            }
        }
        return specification;
    }
}
