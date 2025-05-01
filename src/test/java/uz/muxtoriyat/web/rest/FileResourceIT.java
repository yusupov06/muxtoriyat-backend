package uz.muxtoriyat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.muxtoriyat.domain.FileAsserts.*;
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
import uz.muxtoriyat.domain.File;
import uz.muxtoriyat.domain.Source;
import uz.muxtoriyat.domain.enumeration.FileType;
import uz.muxtoriyat.repository.FileRepository;
import uz.muxtoriyat.service.dto.FileDTO;
import uz.muxtoriyat.service.mapper.FileMapper;

/**
 * Integration tests for the {@link FileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final FileType DEFAULT_FILE_TYPE = FileType.IMAGE;
    private static final FileType UPDATED_FILE_TYPE = FileType.VIDEO;

    private static final String ENTITY_API_URL = "/api/files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileMockMvc;

    private File file;

    private File insertedFile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createEntity() {
        return new File()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .fileType(DEFAULT_FILE_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createUpdatedEntity() {
        return new File()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);
    }

    @BeforeEach
    public void initTest() {
        file = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFile != null) {
            fileRepository.delete(insertedFile);
            insertedFile = null;
        }
    }

    @Test
    @Transactional
    void createFile() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);
        var returnedFileDTO = om.readValue(
            restFileMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FileDTO.class
        );

        // Validate the File in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFile = fileMapper.toEntity(returnedFileDTO);
        assertFileUpdatableFieldsEquals(returnedFile, getPersistedFile(returnedFile));

        insertedFile = returnedFile;
    }

    @Test
    @Transactional
    void createFileWithExistingId() throws Exception {
        // Create the File with an existing ID
        file.setId(1L);
        FileDTO fileDTO = fileMapper.toDto(file);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        file.setName(null);

        // Create the File, which fails.
        FileDTO fileDTO = fileMapper.toDto(file);

        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFiles() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList
        restFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));
    }

    @Test
    @Transactional
    void getFile() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get the file
        restFileMockMvc
            .perform(get(ENTITY_API_URL_ID, file.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(file.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64.getEncoder().encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()));
    }

    @Test
    @Transactional
    void getFilesByIdFiltering() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        Long id = file.getId();

        defaultFileFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFileFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFileFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFilesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where name equals to
        defaultFileFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFilesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where name in
        defaultFileFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFilesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where name is not null
        defaultFileFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllFilesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where name contains
        defaultFileFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFilesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where name does not contain
        defaultFileFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllFilesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where description equals to
        defaultFileFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFilesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where description in
        defaultFileFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFilesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where description is not null
        defaultFileFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllFilesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where description contains
        defaultFileFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFilesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where description does not contain
        defaultFileFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFilesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where url equals to
        defaultFileFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllFilesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where url in
        defaultFileFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllFilesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where url is not null
        defaultFileFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllFilesByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where url contains
        defaultFileFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllFilesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where url does not contain
        defaultFileFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllFilesByFileTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where fileType equals to
        defaultFileFiltering("fileType.equals=" + DEFAULT_FILE_TYPE, "fileType.equals=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllFilesByFileTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where fileType in
        defaultFileFiltering("fileType.in=" + DEFAULT_FILE_TYPE + "," + UPDATED_FILE_TYPE, "fileType.in=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllFilesByFileTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        // Get all the fileList where fileType is not null
        defaultFileFiltering("fileType.specified=true", "fileType.specified=false");
    }

    @Test
    @Transactional
    void getAllFilesBySourceIsEqualToSomething() throws Exception {
        Source source;
        if (TestUtil.findAll(em, Source.class).isEmpty()) {
            fileRepository.saveAndFlush(file);
            source = SourceResourceIT.createEntity();
        } else {
            source = TestUtil.findAll(em, Source.class).get(0);
        }
        em.persist(source);
        em.flush();
        file.setSource(source);
        fileRepository.saveAndFlush(file);
        Long sourceId = source.getId();
        // Get all the fileList where source equals to sourceId
        defaultFileShouldBeFound("sourceId.equals=" + sourceId);

        // Get all the fileList where source equals to (sourceId + 1)
        defaultFileShouldNotBeFound("sourceId.equals=" + (sourceId + 1));
    }

    private void defaultFileFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFileShouldBeFound(shouldBeFound);
        defaultFileShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFileShouldBeFound(String filter) throws Exception {
        restFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));

        // Check, that the count call also returns 1
        restFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFileShouldNotBeFound(String filter) throws Exception {
        restFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFileMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFile() throws Exception {
        // Get the file
        restFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFile() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the file
        File updatedFile = fileRepository.findById(file.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFile are not directly saved in db
        em.detach(updatedFile);
        updatedFile
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);
        FileDTO fileDTO = fileMapper.toDto(updatedFile);

        restFileMockMvc
            .perform(put(ENTITY_API_URL_ID, fileDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isOk());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFileToMatchAllProperties(updatedFile);
    }

    @Test
    @Transactional
    void putNonExistingFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(put(ENTITY_API_URL_ID, fileDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileWithPatch() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).fileType(UPDATED_FILE_TYPE);

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFile, file), getPersistedFile(file));
    }

    @Test
    @Transactional
    void fullUpdateFileWithPatch() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFileUpdatableFieldsEquals(partialUpdatedFile, getPersistedFile(partialUpdatedFile));
    }

    @Test
    @Transactional
    void patchNonExistingFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fileDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fileDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFile() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        file.setId(longCount.incrementAndGet());

        // Create the File
        FileDTO fileDTO = fileMapper.toDto(file);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fileDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFile() throws Exception {
        // Initialize the database
        insertedFile = fileRepository.saveAndFlush(file);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the file
        restFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, file.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return fileRepository.count();
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

    protected File getPersistedFile(File file) {
        return fileRepository.findById(file.getId()).orElseThrow();
    }

    protected void assertPersistedFileToMatchAllProperties(File expectedFile) {
        assertFileAllPropertiesEquals(expectedFile, getPersistedFile(expectedFile));
    }

    protected void assertPersistedFileToMatchUpdatableProperties(File expectedFile) {
        assertFileAllUpdatablePropertiesEquals(expectedFile, getPersistedFile(expectedFile));
    }
}
