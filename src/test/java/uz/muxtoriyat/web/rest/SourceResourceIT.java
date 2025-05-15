package uz.muxtoriyat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.muxtoriyat.domain.SourceAsserts.*;
import static uz.muxtoriyat.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.muxtoriyat.IntegrationTest;
import uz.muxtoriyat.domain.Category;
import uz.muxtoriyat.domain.Source;
import uz.muxtoriyat.domain.enumeration.FileType;
import uz.muxtoriyat.repository.SourceRepository;
import uz.muxtoriyat.service.dto.SourceDTO;
import uz.muxtoriyat.service.mapper.SourceMapper;

/**
 * Integration tests for the {@link SourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SourceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_CONTENT_TYPE = "image/png";

    private static final FileType DEFAULT_FILE_TYPE = FileType.IMAGE;
    private static final FileType UPDATED_FILE_TYPE = FileType.VIDEO;

    private static final String ENTITY_API_URL = "/api/sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private SourceMapper sourceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourceMockMvc;

    private Source source;

    private Source insertedSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Source createEntity() {
        return new Source()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .fileUrl(DEFAULT_FILE_URL)
            .fileContent(DEFAULT_FILE_CONTENT)
            .fileContentContentType(DEFAULT_FILE_CONTENT_CONTENT_TYPE)
            .fileType(DEFAULT_FILE_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Source createUpdatedEntity() {
        return new Source()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);
    }

    @BeforeEach
    public void initTest() {
        source = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSource != null) {
            sourceRepository.delete(insertedSource);
            insertedSource = null;
        }
    }

    @Test
    @Transactional
    void createSource() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);
        var returnedSourceDTO = om.readValue(
            restSourceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SourceDTO.class
        );

        // Validate the Source in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSource = sourceMapper.toEntity(returnedSourceDTO);
        assertSourceUpdatableFieldsEquals(returnedSource, getPersistedSource(returnedSource));

        insertedSource = returnedSource;
    }

    @Test
    @Transactional
    void createSourceWithExistingId() throws Exception {
        // Create the Source with an existing ID
        source.setId(1L);
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSources() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList
        restSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(source.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].fileContentContentType").value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE_CONTENT))))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));
    }

    @Test
    @Transactional
    void getSource() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get the source
        restSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, source.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(source.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64.getEncoder().encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL))
            .andExpect(jsonPath("$.fileContentContentType").value(DEFAULT_FILE_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileContent").value(Base64.getEncoder().encodeToString(DEFAULT_FILE_CONTENT)))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()));
    }

    @Test
    @Transactional
    void getSourcesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        Long id = source.getId();

        defaultSourceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSourceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSourceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSourcesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where name equals to
        defaultSourceFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSourcesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where name in
        defaultSourceFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSourcesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where name is not null
        defaultSourceFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSourcesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where name contains
        defaultSourceFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSourcesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where name does not contain
        defaultSourceFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSourcesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where description equals to
        defaultSourceFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSourcesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where description in
        defaultSourceFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSourcesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where description is not null
        defaultSourceFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllSourcesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where description contains
        defaultSourceFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSourcesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where description does not contain
        defaultSourceFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSourcesByFileUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where fileUrl equals to
        defaultSourceFiltering("fileUrl.equals=" + DEFAULT_FILE_URL, "fileUrl.equals=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllSourcesByFileUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where fileUrl in
        defaultSourceFiltering("fileUrl.in=" + DEFAULT_FILE_URL + "," + UPDATED_FILE_URL, "fileUrl.in=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllSourcesByFileUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where fileUrl is not null
        defaultSourceFiltering("fileUrl.specified=true", "fileUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllSourcesByFileUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where fileUrl contains
        defaultSourceFiltering("fileUrl.contains=" + DEFAULT_FILE_URL, "fileUrl.contains=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllSourcesByFileUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where fileUrl does not contain
        defaultSourceFiltering("fileUrl.doesNotContain=" + UPDATED_FILE_URL, "fileUrl.doesNotContain=" + DEFAULT_FILE_URL);
    }

    @Test
    @Transactional
    void getAllSourcesByFileTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where fileType equals to
        defaultSourceFiltering("fileType.equals=" + DEFAULT_FILE_TYPE, "fileType.equals=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllSourcesByFileTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where fileType in
        defaultSourceFiltering("fileType.in=" + DEFAULT_FILE_TYPE + "," + UPDATED_FILE_TYPE, "fileType.in=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllSourcesByFileTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        // Get all the sourceList where fileType is not null
        defaultSourceFiltering("fileType.specified=true", "fileType.specified=false");
    }

    @Test
    @Transactional
    void getAllSourcesByCategoryIsEqualToSomething() throws Exception {
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            sourceRepository.saveAndFlush(source);
            category = CategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        source.setCategory(category);
        sourceRepository.saveAndFlush(source);
        Long categoryId = category.getId();
        // Get all the sourceList where category equals to categoryId
        defaultSourceShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the sourceList where category equals to (categoryId + 1)
        defaultSourceShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultSourceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSourceShouldBeFound(shouldBeFound);
        defaultSourceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSourceShouldBeFound(String filter) throws Exception {
        restSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(source.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].fileContentContentType").value(hasItem(DEFAULT_FILE_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileContent").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE_CONTENT))))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));

        // Check, that the count call also returns 1
        restSourceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSourceShouldNotBeFound(String filter) throws Exception {
        restSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSourceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSource() throws Exception {
        // Get the source
        restSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSource() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the source
        Source updatedSource = sourceRepository.findById(source.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSource are not directly saved in db
        em.detach(updatedSource);
        updatedSource
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);
        SourceDTO sourceDTO = sourceMapper.toDto(updatedSource);

        restSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Source in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSourceToMatchAllProperties(updatedSource);
    }

    @Test
    @Transactional
    void putNonExistingSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        source.setId(longCount.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sourceDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        source.setId(longCount.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        source.setId(longCount.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sourceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Source in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSourceWithPatch() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the source using partial update
        Source partialUpdatedSource = new Source();
        partialUpdatedSource.setId(source.getId());

        partialUpdatedSource.fileUrl(UPDATED_FILE_URL).fileType(UPDATED_FILE_TYPE);

        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSource))
            )
            .andExpect(status().isOk());

        // Validate the Source in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSourceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSource, source), getPersistedSource(source));
    }

    @Test
    @Transactional
    void fullUpdateSourceWithPatch() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the source using partial update
        Source partialUpdatedSource = new Source();
        partialUpdatedSource.setId(source.getId());

        partialUpdatedSource
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .fileContent(UPDATED_FILE_CONTENT)
            .fileContentContentType(UPDATED_FILE_CONTENT_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);

        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSource))
            )
            .andExpect(status().isOk());

        // Validate the Source in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSourceUpdatableFieldsEquals(partialUpdatedSource, getPersistedSource(partialUpdatedSource));
    }

    @Test
    @Transactional
    void patchNonExistingSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        source.setId(longCount.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sourceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        source.setId(longCount.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sourceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSource() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        source.setId(longCount.incrementAndGet());

        // Create the Source
        SourceDTO sourceDTO = sourceMapper.toDto(source);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSourceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sourceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Source in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSource() throws Exception {
        // Initialize the database
        insertedSource = sourceRepository.saveAndFlush(source);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the source
        restSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, source.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sourceRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Source getPersistedSource(Source source) {
        return sourceRepository.findById(source.getId()).orElseThrow();
    }

    protected void assertPersistedSourceToMatchAllProperties(Source expectedSource) {
        assertSourceAllPropertiesEquals(expectedSource, getPersistedSource(expectedSource));
    }

    protected void assertPersistedSourceToMatchUpdatableProperties(Source expectedSource) {
        assertSourceAllUpdatablePropertiesEquals(expectedSource, getPersistedSource(expectedSource));
    }
}
