package uz.muxtoriyat.service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import uz.muxtoriyat.domain.enumeration.ReactionType;

@Getter
@Setter
@ToString
public class CreateReactionRequest {

    @NotNull
    private Long targetId;

    @NotNull
    private ReactionType reactionType;

    private Boolean state = Boolean.TRUE;
}
