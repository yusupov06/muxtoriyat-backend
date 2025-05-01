package uz.muxtoriyat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.muxtoriyat.domain.ArticleAsserts.*;
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
import uz.muxtoriyat.domain.Article;
import uz.muxtoriyat.domain.Category;
import uz.muxtoriyat.repository.ArticleRepository;
import uz.muxtoriyat.service.dto.ArticleDTO;
import uz.muxtoriyat.service.mapper.ArticleMapper;

/**
 * Integration tests for the {@link ArticleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArticleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleMockMvc;

    private Article article;

    private Article insertedArticle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createEntity() {
        return new Article()
            .name(DEFAULT_NAME)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .content(DEFAULT_CONTENT)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createUpdatedEntity() {
        return new Article()
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @BeforeEach
    public void initTest() {
        article = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedArticle != null) {
            articleRepository.delete(insertedArticle);
            insertedArticle = null;
        }
    }

    @Test
    @Transactional
    void createArticle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);
        var returnedArticleDTO = om.readValue(
            restArticleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArticleDTO.class
        );

        // Validate the Article in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArticle = articleMapper.toEntity(returnedArticleDTO);
        assertArticleUpdatableFieldsEquals(returnedArticle, getPersistedArticle(returnedArticle));

        insertedArticle = returnedArticle;
    }

    @Test
    @Transactional
    void createArticleWithExistingId() throws Exception {
        // Create the Article with an existing ID
        article.setId(1L);
        ArticleDTO articleDTO = articleMapper.toDto(article);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        article.setName(null);

        // Create the Article, which fails.
        ArticleDTO articleDTO = articleMapper.toDto(article);

        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticles() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getArticle() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get the article
        restArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(article.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64.getEncoder().encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getArticlesByIdFiltering() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        Long id = article.getId();

        defaultArticleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultArticleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultArticleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllArticlesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where name equals to
        defaultArticleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllArticlesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where name in
        defaultArticleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllArticlesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where name is not null
        defaultArticleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where name contains
        defaultArticleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllArticlesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where name does not contain
        defaultArticleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllArticlesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where title equals to
        defaultArticleFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllArticlesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where title in
        defaultArticleFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllArticlesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where title is not null
        defaultArticleFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where title contains
        defaultArticleFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllArticlesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where title does not contain
        defaultArticleFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllArticlesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where description equals to
        defaultArticleFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllArticlesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where description in
        defaultArticleFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllArticlesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where description is not null
        defaultArticleFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where description contains
        defaultArticleFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllArticlesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where description does not contain
        defaultArticleFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllArticlesByCategoryIsEqualToSomething() throws Exception {
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            articleRepository.saveAndFlush(article);
            category = CategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        article.setCategory(category);
        articleRepository.saveAndFlush(article);
        Long categoryId = category.getId();
        // Get all the articleList where category equals to categoryId
        defaultArticleShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the articleList where category equals to (categoryId + 1)
        defaultArticleShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultArticleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultArticleShouldBeFound(shouldBeFound);
        defaultArticleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArticleShouldBeFound(String filter) throws Exception {
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArticleShouldNotBeFound(String filter) throws Exception {
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingArticle() throws Exception {
        // Get the article
        restArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticle() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the article
        Article updatedArticle = articleRepository.findById(article.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArticle are not directly saved in db
        em.detach(updatedArticle);
        updatedArticle
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        ArticleDTO articleDTO = articleMapper.toDto(updatedArticle);

        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArticleToMatchAllProperties(updatedArticle);
    }

    @Test
    @Transactional
    void putNonExistingArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, articleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(articleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(articleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleWithPatch() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the article using partial update
        Article partialUpdatedArticle = new Article();
        partialUpdatedArticle.setId(article.getId());

        partialUpdatedArticle.name(UPDATED_NAME).title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedArticle, article), getPersistedArticle(article));
    }

    @Test
    @Transactional
    void fullUpdateArticleWithPatch() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the article using partial update
        Article partialUpdatedArticle = new Article();
        partialUpdatedArticle.setId(article.getId());

        partialUpdatedArticle
            .name(UPDATED_NAME)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .content(UPDATED_CONTENT)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleUpdatableFieldsEquals(partialUpdatedArticle, getPersistedArticle(partialUpdatedArticle));
    }

    @Test
    @Transactional
    void patchNonExistingArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, articleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(articleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // Create the Article
        ArticleDTO articleDTO = articleMapper.toDto(article);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(articleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticle() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the article
        restArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, article.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return articleRepository.count();
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

    protected Article getPersistedArticle(Article article) {
        return articleRepository.findById(article.getId()).orElseThrow();
    }

    protected void assertPersistedArticleToMatchAllProperties(Article expectedArticle) {
        assertArticleAllPropertiesEquals(expectedArticle, getPersistedArticle(expectedArticle));
    }

    protected void assertPersistedArticleToMatchUpdatableProperties(Article expectedArticle) {
        assertArticleAllUpdatablePropertiesEquals(expectedArticle, getPersistedArticle(expectedArticle));
    }
}
