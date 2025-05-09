package uz.muxtoriyat.service.impl;

import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.muxtoriyat.domain.Reaction;
import uz.muxtoriyat.domain.enumeration.ReactionType;
import uz.muxtoriyat.repository.ReactionRepository;
import uz.muxtoriyat.service.ReactionService;
import uz.muxtoriyat.service.dto.ReactionDTO;
import uz.muxtoriyat.service.dto.request.CreateReactionRequest;
import uz.muxtoriyat.service.mapper.ReactionMapper;

/**
 * Service Implementation for managing {@link uz.muxtoriyat.domain.Reaction}.
 */
@Service
@Transactional
public class ReactionServiceImpl implements ReactionService {

    private static final Logger LOG = LoggerFactory.getLogger(ReactionServiceImpl.class);

    private final ReactionRepository reactionRepository;

    private final ReactionMapper reactionMapper;

    public ReactionServiceImpl(ReactionRepository reactionRepository, ReactionMapper reactionMapper) {
        this.reactionRepository = reactionRepository;
        this.reactionMapper = reactionMapper;
    }

    @Override
    public Optional<ReactionDTO> createReaction(CreateReactionRequest request) {
        if (Objects.isNull(request) || Objects.isNull(request.getTargetId()) || Objects.isNull(request.getReactionType())) {
            LOG.warn("Create Reaction with null parameters");
            return Optional.empty();
        }

        ReactionDTO reaction = null;

        if (request.getState()) {
            LOG.warn("Create Reaction: {}", request);
            ReactionDTO reactionDTO = new ReactionDTO();
            reactionDTO.setReactionType(request.getReactionType());
            reactionDTO.setTargetId(request.getTargetId());
            reaction = save(reactionDTO);
        } else {
            Optional<ReactionDTO> optional = getFirstByReactionTypeAndTargetId(request.getReactionType(), request.getTargetId());
            if (optional.isPresent()) {
                reaction = optional.orElseThrow();
                delete(reaction.getId());
            }
        }

        return Optional.ofNullable(reaction);
    }

    private void deleteFirstByReactionTypeAndTargetId(ReactionType reactionType, Long targetId) {
        reactionRepository.deleteFirstByTargetIdAndReactionType(targetId, reactionType);
    }

    private Optional<ReactionDTO> getFirstByReactionTypeAndTargetId(ReactionType reactionType, @NotNull Long targetId) {
        return Optional.ofNullable(reactionRepository.findFirstByTargetIdAndReactionType(targetId, reactionType)).map(
            reactionMapper::toDto
        );
    }

    @Override
    public ReactionDTO save(ReactionDTO reactionDTO) {
        LOG.debug("Request to save Reaction : {}", reactionDTO);
        Reaction reaction = reactionMapper.toEntity(reactionDTO);
        reaction = reactionRepository.save(reaction);
        return reactionMapper.toDto(reaction);
    }

    @Override
    public ReactionDTO update(ReactionDTO reactionDTO) {
        LOG.debug("Request to update Reaction : {}", reactionDTO);
        Reaction reaction = reactionMapper.toEntity(reactionDTO);
        reaction = reactionRepository.save(reaction);
        return reactionMapper.toDto(reaction);
    }

    @Override
    public Optional<ReactionDTO> partialUpdate(ReactionDTO reactionDTO) {
        LOG.debug("Request to partially update Reaction : {}", reactionDTO);

        return reactionRepository
            .findById(reactionDTO.getId())
            .map(existingReaction -> {
                reactionMapper.partialUpdate(existingReaction, reactionDTO);

                return existingReaction;
            })
            .map(reactionRepository::save)
            .map(reactionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReactionDTO> findOne(Long id) {
        LOG.debug("Request to get Reaction : {}", id);
        return reactionRepository.findById(id).map(reactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Reaction : {}", id);
        reactionRepository.deleteById(id);
    }
}
