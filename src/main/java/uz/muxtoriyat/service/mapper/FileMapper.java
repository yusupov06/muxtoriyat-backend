package uz.muxtoriyat.service.mapper;

import org.mapstruct.*;
import uz.muxtoriyat.domain.File;
import uz.muxtoriyat.domain.Source;
import uz.muxtoriyat.service.dto.FileDTO;
import uz.muxtoriyat.service.dto.SourceDTO;

/**
 * Mapper for the entity {@link File} and its DTO {@link FileDTO}.
 */
@Mapper(componentModel = "spring")
public interface FileMapper extends EntityMapper<FileDTO, File> {
    @Mapping(target = "source", source = "source", qualifiedByName = "sourceId")
    FileDTO toDto(File s);

    @Named("sourceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SourceDTO toDtoSourceId(Source source);
}
