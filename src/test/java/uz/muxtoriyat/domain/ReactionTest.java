package uz.muxtoriyat.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.muxtoriyat.domain.ReactionTestSamples.*;

import org.junit.jupiter.api.Test;
import uz.muxtoriyat.web.rest.TestUtil;

class ReactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reaction.class);
        Reaction reaction1 = getReactionSample1();
        Reaction reaction2 = new Reaction();
        assertThat(reaction1).isNotEqualTo(reaction2);

        reaction2.setId(reaction1.getId());
        assertThat(reaction1).isEqualTo(reaction2);

        reaction2 = getReactionSample2();
        assertThat(reaction1).isNotEqualTo(reaction2);
    }
}
