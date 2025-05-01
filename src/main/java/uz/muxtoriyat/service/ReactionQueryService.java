package uz.muxtoriyat.service;

import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.muxtoriyat.domain.*; // for static metamodels
import uz.muxtoriyat.domain.Reaction;
import uz.muxtoriyat.repository.ReactionRepository;
import uz.muxtoriyat.service.criteria.ReactionCriteria;
import uz.muxtoriyat.service.dto.ReactionDTO;
import uz.muxtoriyat.service.mapper.ReactionMapper;

/**
 * Service for executing complex queries for {@link Reaction} entities in the database.
 * The main input is a {@link ReactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReactionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReactionQueryService extends QueryService<Reaction> {

    private static final Logger LOG = LoggerFactory.getLogger(ReactionQueryService.class);

    private final ReactionRepository reactionRepository;

    private final ReactionMapper reactionMapper;

    public ReactionQueryService(ReactionRepository reactionRepository, ReactionMapper reactionMapper) {
        this.reactionRepository = reactionRepository;
        this.reactionMapper = reactionMapper;
    }

    /**
     * Return a {@link List} of {@link ReactionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReactionDTO> findByCriteria(ReactionCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Reaction> specification = createSpecification(criteria);
        return reactionMapper.toDto(reactionRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReactionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Reaction> specification = createSpecification(criteria);
        return reactionRepository.count(specification);
    }

    /**
     * Function to convert {@link ReactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reaction> createSpecification(ReactionCriteria criteria) {
        Specification<Reaction> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Reaction_.id));
            }
            if (criteria.getDeviceId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceId(), Reaction_.deviceId));
            }
            if (criteria.getReactionType() != null) {
                specification = specification.and(buildSpecification(criteria.getReactionType(), Reaction_.reactionType));
            }
            if (criteria.getArticleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getArticleId(), root -> root.join(Reaction_.article, JoinType.LEFT).get(Article_.id))
                );
            }
        }
        return specification;
    }
}
