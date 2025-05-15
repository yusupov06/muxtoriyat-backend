package uz.muxtoriyat.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.muxtoriyat.domain.enumeration.VisibilityType;
import uz.muxtoriyat.security.SecurityUtils;
import uz.muxtoriyat.service.ArticleService;
import uz.muxtoriyat.service.MyArticleService;
import uz.muxtoriyat.service.UserService;
import uz.muxtoriyat.service.dto.ArticleDTO;
import uz.muxtoriyat.service.dto.UserDTO;
import uz.muxtoriyat.service.dto.view.ArticleAddDTO;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;
import uz.muxtoriyat.service.mapper.ArticleMapper;
import uz.muxtoriyat.web.rest.errors.BadRequestAlertException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyArticleServiceImpl implements MyArticleService {

    private final ArticleMapper articleMapper;
    private final UserService userService;
    private final ArticleService articleService;

    @Override
    public ArticleViewDTO addArticle(ArticleAddDTO articleAddDTO) {
        log.debug("Adding article {}", articleAddDTO);
        ArticleDTO articleDTO = articleMapper.toDto(articleAddDTO);
        UserDTO author = getLoggedUser();
        articleDTO.setVisibility(VisibilityType.BLANK);
        articleDTO.setAuthor(author);
        ArticleDTO saved = articleService.save(articleDTO);
        log.debug("Saved article {}", saved);
        return articleMapper.toViewDto(saved);
    }

    private UserDTO getLoggedUser() {
        Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
        if (userLogin.isEmpty()) {
            throw new BadRequestAlertException("Invalid user login", "userLogin", "null");
        }
        Optional<UserDTO> userByLogin = userService.getUserByLogin(userLogin.orElseThrow());
        if (userByLogin.isEmpty()) {
            throw new BadRequestAlertException("Invalid user login", "userLogin", "null");
        }
        return userByLogin.get();
    }
}
