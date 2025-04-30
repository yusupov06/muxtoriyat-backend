package uz.muxtoriyat.service.mapper;

import static uz.muxtoriyat.domain.ReactionAsserts.*;
import static uz.muxtoriyat.domain.ReactionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReactionMapperTest {

    private ReactionMapper reactionMapper;

    @BeforeEach
    void setUp() {
        reactionMapper = new ReactionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getReactionSample1();
        var actual = reactionMapper.toEntity(reactionMapper.toDto(expected));
        assertReactionAllPropertiesEquals(expected, actual);
    }
}
