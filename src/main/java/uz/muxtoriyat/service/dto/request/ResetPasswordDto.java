package uz.muxtoriyat.service.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResetPasswordDto {

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;
}
