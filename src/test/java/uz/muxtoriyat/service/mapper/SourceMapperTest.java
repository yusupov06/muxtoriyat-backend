package uz.muxtoriyat.service.mapper;

import static uz.muxtoriyat.domain.SourceAsserts.*;
import static uz.muxtoriyat.domain.SourceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SourceMapperTest {

    private SourceMapper sourceMapper;

    @BeforeEach
    void setUp() {
        sourceMapper = new SourceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSourceSample1();
        var actual = sourceMapper.toEntity(sourceMapper.toDto(expected));
        assertSourceAllPropertiesEquals(expected, actual);
    }
}
