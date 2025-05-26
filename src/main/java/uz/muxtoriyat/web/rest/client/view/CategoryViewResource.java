package uz.muxtoriyat.web.rest.client.view;

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
import uz.muxtoriyat.service.CategoryQueryService;
import uz.muxtoriyat.service.CategoryService;
import uz.muxtoriyat.service.criteria.CategoryCriteria;
import uz.muxtoriyat.service.dto.view.CategoryViewDTO;

/**
 * REST controller for managing {@link uz.muxtoriyat.domain.Category}.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/view/categories")
public class CategoryViewResource {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryViewResource.class);

    private final CategoryService categoryService;

    private final CategoryQueryService categoryQueryService;

    /**
     * {@code GET  /categories} : get all the categories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CategoryViewDTO>> getAllCategories(
        CategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Categories by criteria: {}", criteria);

        Page<CategoryViewDTO> page = categoryQueryService.findByCriteriaAsView(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /categories/count} : count all the categories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCategories(CategoryCriteria criteria) {
        LOG.debug("REST request to count Categories by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /categories/:id} : get the "id" category.
     *
     * @param id the id of the categoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryViewDTO> getCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Category : {}", id);
        Optional<CategoryViewDTO> categoryDTO = categoryService.findOneAsView(id);
        return ResponseUtil.wrapOrNotFound(categoryDTO);
    }
}
