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
import uz.muxtoriyat.service.SourceQueryService;
import uz.muxtoriyat.service.SourceService;
import uz.muxtoriyat.service.criteria.SourceCriteria;
import uz.muxtoriyat.service.dto.view.SourceViewDTO;

/**
 * REST controller for managing {@link uz.muxtoriyat.domain.Source}.
 */
@RestController
@RequestMapping("/api/view/sources")
@RequiredArgsConstructor
public class SourceViewResource {

    private static final Logger LOG = LoggerFactory.getLogger(SourceViewResource.class);

    private final SourceService sourceService;

    private final SourceQueryService sourceQueryService;

    /**
     * {@code GET  /sources} : get all the sources.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sources in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SourceViewDTO>> getAllSources(
        SourceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Sources by criteria: {}", criteria);

        Page<SourceViewDTO> page = sourceQueryService.findByCriteriaAsView(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sources/count} : count all the sources.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSources(SourceCriteria criteria) {
        LOG.debug("REST request to count Sources by criteria: {}", criteria);
        return ResponseEntity.ok().body(sourceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sources/:id} : get the "id" source.
     *
     * @param id the id of the sourceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SourceViewDTO> getSource(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Source : {}", id);
        Optional<SourceViewDTO> sourceDTO = sourceService.findOneAsView(id);
        return ResponseUtil.wrapOrNotFound(sourceDTO);
    }
}
