package uz.muxtoriyat.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.muxtoriyat.web.rest.TestUtil;

class ReactionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReactionDTO.class);
        ReactionDTO reactionDTO1 = new ReactionDTO();
        reactionDTO1.setId(1L);
        ReactionDTO reactionDTO2 = new ReactionDTO();
        assertThat(reactionDTO1).isNotEqualTo(reactionDTO2);
        reactionDTO2.setId(reactionDTO1.getId());
        assertThat(reactionDTO1).isEqualTo(reactionDTO2);
        reactionDTO2.setId(2L);
        assertThat(reactionDTO1).isNotEqualTo(reactionDTO2);
        reactionDTO1.setId(null);
        assertThat(reactionDTO1).isNotEqualTo(reactionDTO2);
    }
}
