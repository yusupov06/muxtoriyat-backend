package uz.muxtoriyat.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.muxtoriyat.domain.Source;
import uz.muxtoriyat.repository.SourceRepository;
import uz.muxtoriyat.service.SourceService;
import uz.muxtoriyat.service.dto.SourceDTO;
import uz.muxtoriyat.service.mapper.SourceMapper;

/**
 * Service Implementation for managing {@link uz.muxtoriyat.domain.Source}.
 */
@Service
@Transactional
public class SourceServiceImpl implements SourceService {

    private static final Logger LOG = LoggerFactory.getLogger(SourceServiceImpl.class);

    private final SourceRepository sourceRepository;

    private final SourceMapper sourceMapper;

    public SourceServiceImpl(SourceRepository sourceRepository, SourceMapper sourceMapper) {
        this.sourceRepository = sourceRepository;
        this.sourceMapper = sourceMapper;
    }

    @Override
    public SourceDTO save(SourceDTO sourceDTO) {
        LOG.debug("Request to save Source : {}", sourceDTO);
        Source source = sourceMapper.toEntity(sourceDTO);
        source = sourceRepository.save(source);
        return sourceMapper.toDto(source);
    }

    @Override
    public SourceDTO update(SourceDTO sourceDTO) {
        LOG.debug("Request to update Source : {}", sourceDTO);
        Source source = sourceMapper.toEntity(sourceDTO);
        source = sourceRepository.save(source);
        return sourceMapper.toDto(source);
    }

    @Override
    public Optional<SourceDTO> partialUpdate(SourceDTO sourceDTO) {
        LOG.debug("Request to partially update Source : {}", sourceDTO);

        return sourceRepository
            .findById(sourceDTO.getId())
            .map(existingSource -> {
                sourceMapper.partialUpdate(existingSource, sourceDTO);

                return existingSource;
            })
            .map(sourceRepository::save)
            .map(sourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SourceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Sources");
        return sourceRepository.findAll(pageable).map(sourceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SourceDTO> findOne(Long id) {
        LOG.debug("Request to get Source : {}", id);
        return sourceRepository.findById(id).map(sourceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Source : {}", id);
        sourceRepository.deleteById(id);
    }
}
