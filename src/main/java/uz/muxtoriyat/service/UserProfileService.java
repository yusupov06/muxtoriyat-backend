package uz.muxtoriyat.service;

import uz.muxtoriyat.service.dto.request.ResetPasswordDto;

public interface UserProfileService {
    boolean resetMyPassword(ResetPasswordDto resetPasswordDto);
}
