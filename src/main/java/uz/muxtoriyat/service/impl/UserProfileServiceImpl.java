package uz.muxtoriyat.service.impl;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.jhipster.security.RandomUtil;
import uz.muxtoriyat.domain.User;
import uz.muxtoriyat.repository.UserRepository;
import uz.muxtoriyat.security.SecurityUtils;
import uz.muxtoriyat.service.UserProfileService;
import uz.muxtoriyat.service.dto.request.ResetPasswordDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean resetMyPassword(ResetPasswordDto resetPasswordDto) {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) {
            log.error("Current user is not logged in");
            return false;
        }

        if (!Objects.equals(resetPasswordDto.getNewPassword(), resetPasswordDto.getConfirmPassword())) {
            log.info("User new password does not match confirm password");
            return false;
        }

        Optional<User> userByLogin = userRepository.findOneByLogin(currentUserLogin.get());
        if (userByLogin.isEmpty()) {
            log.error("user not found with login {}", currentUserLogin.get());
            return false;
        }

        User user = userByLogin.orElseThrow();

        if (!passwordEncoder.matches(resetPasswordDto.getOldPassword(), user.getPassword())) {
            log.error("Old password does not match user password");
            return false;
        }

        String encryptedPassword = passwordEncoder.encode(resetPasswordDto.getNewPassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        return true;
    }
}
