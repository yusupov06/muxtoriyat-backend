package uz.muxtoriyat.web.rest;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.muxtoriyat.service.ArticleQueryService;
import uz.muxtoriyat.service.ArticleService;
import uz.muxtoriyat.service.ArticleViewService;
import uz.muxtoriyat.service.criteria.ArticleCriteria;
import uz.muxtoriyat.service.dto.view.ArticleViewDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/view/articles")
public class ArticleViewResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleViewResource.class);

    private final ArticleService articleService;

    private final ArticleViewService articleViewService;

    private final ArticleQueryService articleQueryService;

    /**
     * {@code GET  /articles} : get all the articles.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of articles in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArticleViewDTO>> getAllArticles(
        ArticleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Articles by criteria: {}", criteria);

        Page<ArticleViewDTO> page = articleQueryService.findByCriteriaAsView(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        List<ArticleViewDTO> content = page.getContent();
        articleViewService.fillReactions(content);
        return ResponseEntity.ok().headers(headers).body(content);
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
        Optional<ArticleViewDTO> articleDTO = articleService.findOneAsView(id);
        return ResponseUtil.wrapOrNotFound(articleDTO);
    }
}
