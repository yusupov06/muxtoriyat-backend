package uz.muxtoriyat.web.rest;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.muxtoriyat.security.SecurityUtils;
import uz.muxtoriyat.service.UserProfileService;
import uz.muxtoriyat.service.UserService;
import uz.muxtoriyat.service.dto.UserDTO;
import uz.muxtoriyat.service.dto.request.ResetPasswordDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my-profile")
public class MyProfileResource {

    private static final Logger LOG = LoggerFactory.getLogger(MyProfileResource.class);

    private final UserService userService;

    private final UserProfileService userProfileService;

    @PostMapping("/edit")
    public ResponseEntity<UserDTO> editMyProfile(@RequestBody UserDTO userDTO) {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) {
            LOG.warn("No user logged in");
            return ResponseEntity.notFound().build();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);
        return updatedUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<UserDTO> myProfile() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) {
            LOG.warn("No user logged in");
            return ResponseEntity.notFound().build();
        }
        Optional<UserDTO> userByLogin = userService.getUserByLogin(currentUserLogin.orElseThrow());
        if (userByLogin.isEmpty()) {
            LOG.warn("No user found by login: {}", currentUserLogin.orElseThrow());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userByLogin.orElseThrow());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Boolean> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        boolean reset = userProfileService.resetMyPassword(resetPasswordDto);
        if (reset) {
            return ResponseEntity.ok(reset);
        }
        return ResponseEntity.status(416).build();
    }
}
