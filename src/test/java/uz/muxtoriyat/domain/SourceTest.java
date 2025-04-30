package uz.muxtoriyat.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.muxtoriyat.domain.CategoryTestSamples.*;
import static uz.muxtoriyat.domain.SourceTestSamples.*;

import org.junit.jupiter.api.Test;
import uz.muxtoriyat.web.rest.TestUtil;

class SourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Source.class);
        Source source1 = getSourceSample1();
        Source source2 = new Source();
        assertThat(source1).isNotEqualTo(source2);

        source2.setId(source1.getId());
        assertThat(source1).isEqualTo(source2);

        source2 = getSourceSample2();
        assertThat(source1).isNotEqualTo(source2);
    }

    @Test
    void categoryTest() {
        Source source = getSourceRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        source.setCategory(categoryBack);
        assertThat(source.getCategory()).isEqualTo(categoryBack);

        source.category(null);
        assertThat(source.getCategory()).isNull();
    }
}
