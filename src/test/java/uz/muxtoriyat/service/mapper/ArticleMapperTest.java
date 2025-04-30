package uz.muxtoriyat.service.mapper;

import static uz.muxtoriyat.domain.ArticleAsserts.*;
import static uz.muxtoriyat.domain.ArticleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArticleMapperTest {

    private ArticleMapper articleMapper;

    @BeforeEach
    void setUp() {
        articleMapper = new ArticleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArticleSample1();
        var actual = articleMapper.toEntity(articleMapper.toDto(expected));
        assertArticleAllPropertiesEquals(expected, actual);
    }
}
