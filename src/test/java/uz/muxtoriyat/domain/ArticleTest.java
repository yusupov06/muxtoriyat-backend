package uz.muxtoriyat.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.muxtoriyat.domain.ArticleTestSamples.*;
import static uz.muxtoriyat.domain.CategoryTestSamples.*;

import org.junit.jupiter.api.Test;
import uz.muxtoriyat.web.rest.TestUtil;

class ArticleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Article.class);
        Article article1 = getArticleSample1();
        Article article2 = new Article();
        assertThat(article1).isNotEqualTo(article2);

        article2.setId(article1.getId());
        assertThat(article1).isEqualTo(article2);

        article2 = getArticleSample2();
        assertThat(article1).isNotEqualTo(article2);
    }

    @Test
    void categoryTest() {
        Article article = getArticleRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        article.setCategory(categoryBack);
        assertThat(article.getCategory()).isEqualTo(categoryBack);

        article.category(null);
        assertThat(article.getCategory()).isNull();
    }
}
