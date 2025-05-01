package uz.muxtoriyat.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.muxtoriyat.domain.File;
import uz.muxtoriyat.repository.FileRepository;
import uz.muxtoriyat.service.FileService;
import uz.muxtoriyat.service.dto.FileDTO;
import uz.muxtoriyat.service.mapper.FileMapper;

/**
 * Service Implementation for managing {@link uz.muxtoriyat.domain.File}.
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    public FileServiceImpl(FileRepository fileRepository, FileMapper fileMapper) {
        this.fileRepository = fileRepository;
        this.fileMapper = fileMapper;
    }

    @Override
    public FileDTO save(FileDTO fileDTO) {
        LOG.debug("Request to save File : {}", fileDTO);
        File file = fileMapper.toEntity(fileDTO);
        file = fileRepository.save(file);
        return fileMapper.toDto(file);
    }

    @Override
    public FileDTO update(FileDTO fileDTO) {
        LOG.debug("Request to update File : {}", fileDTO);
        File file = fileMapper.toEntity(fileDTO);
        file = fileRepository.save(file);
        return fileMapper.toDto(file);
    }

    @Override
    public Optional<FileDTO> partialUpdate(FileDTO fileDTO) {
        LOG.debug("Request to partially update File : {}", fileDTO);

        return fileRepository
            .findById(fileDTO.getId())
            .map(existingFile -> {
                fileMapper.partialUpdate(existingFile, fileDTO);

                return existingFile;
            })
            .map(fileRepository::save)
            .map(fileMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileDTO> findOne(Long id) {
        LOG.debug("Request to get File : {}", id);
        return fileRepository.findById(id).map(fileMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete File : {}", id);
        fileRepository.deleteById(id);
    }
}
