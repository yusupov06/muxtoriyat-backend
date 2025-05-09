package uz.muxtoriyat.service.mapper;

import org.mapstruct.*;
import uz.muxtoriyat.domain.Reaction;
import uz.muxtoriyat.service.dto.ReactionDTO;

/**
 * Mapper for the entity {@link Reaction} and its DTO {@link ReactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReactionMapper extends EntityMapper<ReactionDTO, Reaction> {}
