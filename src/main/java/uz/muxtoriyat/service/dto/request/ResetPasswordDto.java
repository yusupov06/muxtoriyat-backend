package uz.muxtoriyat.service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;
}
