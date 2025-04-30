package uz.muxtoriyat.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.muxtoriyat.domain.FileTestSamples.*;
import static uz.muxtoriyat.domain.SourceTestSamples.*;

import org.junit.jupiter.api.Test;
import uz.muxtoriyat.web.rest.TestUtil;

class FileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(File.class);
        File file1 = getFileSample1();
        File file2 = new File();
        assertThat(file1).isNotEqualTo(file2);

        file2.setId(file1.getId());
        assertThat(file1).isEqualTo(file2);

        file2 = getFileSample2();
        assertThat(file1).isNotEqualTo(file2);
    }

    @Test
    void sourceTest() {
        File file = getFileRandomSampleGenerator();
        Source sourceBack = getSourceRandomSampleGenerator();

        file.setSource(sourceBack);
        assertThat(file.getSource()).isEqualTo(sourceBack);

        file.source(null);
        assertThat(file.getSource()).isNull();
    }
}
