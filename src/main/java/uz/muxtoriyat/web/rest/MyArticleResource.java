package uz.muxtoriyat.web.rest;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.muxtoriyat.security.SecurityUtils;
import uz.muxtoriyat.service.*;
import uz.muxtoriyat.service.criteria.ArticleCriteria;
import uz.muxtoriyat.service.dto.UserDTO;
import uz.muxtoriyat.service.dto.view.ArticleAddDTO;
import uz.muxtoriyat.service.dto.view.ArticleBasicViewDTO;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my-articles")
public class MyArticleResource {

    private static final Logger LOG = LoggerFactory.getLogger(MyArticleResource.class);

    private final ArticleService articleService;

    private final MyArticleService myArticleService;

    private final ArticleViewService articleViewService;

    private final ArticleQueryService articleQueryService;

    private final UserService userService;

    /**
     * {@code GET  /articles} : get all the articles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArticleBasicViewDTO>> getAllArticles(
        ArticleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Articles by criteria: {}", criteria);
        addAuthorId(criteria);
        Page<ArticleBasicViewDTO> page = articleQueryService.findByCriteriaAsBasicView(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        List<ArticleBasicViewDTO> content = page.getContent();
        articleViewService.fillBasicReactions(content);
        return ResponseEntity.ok().headers(headers).body(content);
    }

    private void addAuthorId(ArticleCriteria criteria) {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) {
            LOG.warn("No user logged in");
        }
        Optional<UserDTO> userByLogin = userService.getUserByLogin(currentUserLogin.orElseThrow());
        if (userByLogin.isEmpty()) {
            LOG.warn("No user found by login: {}", currentUserLogin.orElseThrow());
        }
        LongFilter authorIdFilter = new LongFilter();
        authorIdFilter.setEquals(userByLogin.orElseThrow().getId());
        criteria.setAuthorId(authorIdFilter);
        LOG.debug("Adding authorId filter: {}", authorIdFilter);
    }

    /**
     * {@code GET  /articles/count} : count all the articles.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countArticles(ArticleCriteria criteria) {
        LOG.debug("REST request to count Articles by criteria: {}", criteria);
        addAuthorId(criteria);
        return ResponseEntity.ok().body(articleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /articles/:id} : get the "id" article.
     *
     * @param id the id of the articleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the articleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleViewDTO> getArticle(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Article : {}", id);
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isEmpty()) {
            LOG.warn("No user logged in");
        }
        Optional<Long> userIdByLogin = userService.getUserIdByLogin(currentUserLogin.orElseThrow());
        if (userIdByLogin.isEmpty()) {
            LOG.warn("No user found by login: {}", currentUserLogin.orElseThrow());
        }
        Optional<ArticleViewDTO> articleDTO = articleService.findOneByAuthorIdAsView(userIdByLogin.orElseThrow(), id);
        articleDTO.ifPresent(articleViewService::fillReactions);
        return ResponseUtil.wrapOrNotFound(articleDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<ArticleViewDTO> createArticle(@Valid @RequestBody ArticleAddDTO articleAddDTO) {
        ArticleViewDTO articleViewDTO = myArticleService.addArticle(articleAddDTO);
        return ResponseEntity.ok(articleViewDTO);
    }
}
