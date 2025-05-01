package uz.muxtoriyat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.muxtoriyat.repository.SourceRepository;
import uz.muxtoriyat.service.SourceQueryService;
import uz.muxtoriyat.service.SourceService;
import uz.muxtoriyat.service.criteria.SourceCriteria;
import uz.muxtoriyat.service.dto.SourceDTO;
import uz.muxtoriyat.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.muxtoriyat.domain.Source}.
 */
@RestController
@RequestMapping("/api/sources")
public class SourceResource {

    private static final Logger LOG = LoggerFactory.getLogger(SourceResource.class);

    private static final String ENTITY_NAME = "source";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceService sourceService;

    private final SourceRepository sourceRepository;

    private final SourceQueryService sourceQueryService;

    public SourceResource(SourceService sourceService, SourceRepository sourceRepository, SourceQueryService sourceQueryService) {
        this.sourceService = sourceService;
        this.sourceRepository = sourceRepository;
        this.sourceQueryService = sourceQueryService;
    }

    /**
     * {@code POST  /sources} : Create a new source.
     *
     * @param sourceDTO the sourceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourceDTO, or with status {@code 400 (Bad Request)} if the source has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SourceDTO> createSource(@RequestBody SourceDTO sourceDTO) throws URISyntaxException {
        LOG.debug("REST request to save Source : {}", sourceDTO);
        if (sourceDTO.getId() != null) {
            throw new BadRequestAlertException("A new source cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sourceDTO = sourceService.save(sourceDTO);
        return ResponseEntity.created(new URI("/api/sources/" + sourceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sourceDTO.getId().toString()))
            .body(sourceDTO);
    }

    /**
     * {@code PUT  /sources/:id} : Updates an existing source.
     *
     * @param id the id of the sourceDTO to save.
     * @param sourceDTO the sourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceDTO,
     * or with status {@code 400 (Bad Request)} if the sourceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SourceDTO> updateSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SourceDTO sourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Source : {}, {}", id, sourceDTO);
        if (sourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sourceDTO = sourceService.update(sourceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceDTO.getId().toString()))
            .body(sourceDTO);
    }

    /**
     * {@code PATCH  /sources/:id} : Partial updates given fields of an existing source, field will ignore if it is null
     *
     * @param id the id of the sourceDTO to save.
     * @param sourceDTO the sourceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceDTO,
     * or with status {@code 400 (Bad Request)} if the sourceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sourceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sourceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SourceDTO> partialUpdateSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SourceDTO sourceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Source partially : {}, {}", id, sourceDTO);
        if (sourceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SourceDTO> result = sourceService.partialUpdate(sourceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sources} : get all the sources.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sources in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SourceDTO>> getAllSources(
        SourceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Sources by criteria: {}", criteria);

        Page<SourceDTO> page = sourceQueryService.findByCriteria(criteria, pageable);
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
    public ResponseEntity<SourceDTO> getSource(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Source : {}", id);
        Optional<SourceDTO> sourceDTO = sourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sourceDTO);
    }

    /**
     * {@code DELETE  /sources/:id} : delete the "id" source.
     *
     * @param id the id of the sourceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSource(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Source : {}", id);
        sourceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
