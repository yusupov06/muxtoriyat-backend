package uz.muxtoriyat.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.muxtoriyat.domain.ReactionAsserts.*;
import static uz.muxtoriyat.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import uz.muxtoriyat.domain.Reaction;
import uz.muxtoriyat.domain.enumeration.ReactionType;
import uz.muxtoriyat.repository.ReactionRepository;
import uz.muxtoriyat.service.dto.ReactionDTO;
import uz.muxtoriyat.service.mapper.ReactionMapper;

/**
 * Integration tests for the {@link ReactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReactionResourceIT {

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_TARGET_ID = 1L;
    private static final Long UPDATED_TARGET_ID = 2L;
    private static final Long SMALLER_TARGET_ID = 1L - 1L;

    private static final ReactionType DEFAULT_REACTION_TYPE = ReactionType.LIKE;
    private static final ReactionType UPDATED_REACTION_TYPE = ReactionType.DISLIKE;

    private static final String ENTITY_API_URL = "/api/reactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private ReactionMapper reactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReactionMockMvc;

    private Reaction reaction;

    private Reaction insertedReaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createEntity() {
        return new Reaction().deviceId(DEFAULT_DEVICE_ID).targetId(DEFAULT_TARGET_ID).reactionType(DEFAULT_REACTION_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createUpdatedEntity() {
        return new Reaction().deviceId(UPDATED_DEVICE_ID).targetId(UPDATED_TARGET_ID).reactionType(UPDATED_REACTION_TYPE);
    }

    @BeforeEach
    public void initTest() {
        reaction = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedReaction != null) {
            reactionRepository.delete(insertedReaction);
            insertedReaction = null;
        }
    }

    @Test
    @Transactional
    void createReaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);
        var returnedReactionDTO = om.readValue(
            restReactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReactionDTO.class
        );

        // Validate the Reaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReaction = reactionMapper.toEntity(returnedReactionDTO);
        assertReactionUpdatableFieldsEquals(returnedReaction, getPersistedReaction(returnedReaction));

        insertedReaction = returnedReaction;
    }

    @Test
    @Transactional
    void createReactionWithExistingId() throws Exception {
        // Create the Reaction with an existing ID
        reaction.setId(1L);
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReactions() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList
        restReactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].targetId").value(hasItem(DEFAULT_TARGET_ID.intValue())))
            .andExpect(jsonPath("$.[*].reactionType").value(hasItem(DEFAULT_REACTION_TYPE.toString())));
    }

    @Test
    @Transactional
    void getReaction() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get the reaction
        restReactionMockMvc
            .perform(get(ENTITY_API_URL_ID, reaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reaction.getId().intValue()))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID))
            .andExpect(jsonPath("$.targetId").value(DEFAULT_TARGET_ID.intValue()))
            .andExpect(jsonPath("$.reactionType").value(DEFAULT_REACTION_TYPE.toString()));
    }

    @Test
    @Transactional
    void getReactionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        Long id = reaction.getId();

        defaultReactionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReactionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReactionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReactionsByDeviceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where deviceId equals to
        defaultReactionFiltering("deviceId.equals=" + DEFAULT_DEVICE_ID, "deviceId.equals=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllReactionsByDeviceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where deviceId in
        defaultReactionFiltering("deviceId.in=" + DEFAULT_DEVICE_ID + "," + UPDATED_DEVICE_ID, "deviceId.in=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllReactionsByDeviceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where deviceId is not null
        defaultReactionFiltering("deviceId.specified=true", "deviceId.specified=false");
    }

    @Test
    @Transactional
    void getAllReactionsByDeviceIdContainsSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where deviceId contains
        defaultReactionFiltering("deviceId.contains=" + DEFAULT_DEVICE_ID, "deviceId.contains=" + UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllReactionsByDeviceIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where deviceId does not contain
        defaultReactionFiltering("deviceId.doesNotContain=" + UPDATED_DEVICE_ID, "deviceId.doesNotContain=" + DEFAULT_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllReactionsByTargetIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where targetId equals to
        defaultReactionFiltering("targetId.equals=" + DEFAULT_TARGET_ID, "targetId.equals=" + UPDATED_TARGET_ID);
    }

    @Test
    @Transactional
    void getAllReactionsByTargetIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where targetId in
        defaultReactionFiltering("targetId.in=" + DEFAULT_TARGET_ID + "," + UPDATED_TARGET_ID, "targetId.in=" + UPDATED_TARGET_ID);
    }

    @Test
    @Transactional
    void getAllReactionsByTargetIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where targetId is not null
        defaultReactionFiltering("targetId.specified=true", "targetId.specified=false");
    }

    @Test
    @Transactional
    void getAllReactionsByTargetIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where targetId is greater than or equal to
        defaultReactionFiltering("targetId.greaterThanOrEqual=" + DEFAULT_TARGET_ID, "targetId.greaterThanOrEqual=" + UPDATED_TARGET_ID);
    }

    @Test
    @Transactional
    void getAllReactionsByTargetIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where targetId is less than or equal to
        defaultReactionFiltering("targetId.lessThanOrEqual=" + DEFAULT_TARGET_ID, "targetId.lessThanOrEqual=" + SMALLER_TARGET_ID);
    }

    @Test
    @Transactional
    void getAllReactionsByTargetIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where targetId is less than
        defaultReactionFiltering("targetId.lessThan=" + UPDATED_TARGET_ID, "targetId.lessThan=" + DEFAULT_TARGET_ID);
    }

    @Test
    @Transactional
    void getAllReactionsByTargetIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where targetId is greater than
        defaultReactionFiltering("targetId.greaterThan=" + SMALLER_TARGET_ID, "targetId.greaterThan=" + DEFAULT_TARGET_ID);
    }

    @Test
    @Transactional
    void getAllReactionsByReactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where reactionType equals to
        defaultReactionFiltering("reactionType.equals=" + DEFAULT_REACTION_TYPE, "reactionType.equals=" + UPDATED_REACTION_TYPE);
    }

    @Test
    @Transactional
    void getAllReactionsByReactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where reactionType in
        defaultReactionFiltering(
            "reactionType.in=" + DEFAULT_REACTION_TYPE + "," + UPDATED_REACTION_TYPE,
            "reactionType.in=" + UPDATED_REACTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllReactionsByReactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList where reactionType is not null
        defaultReactionFiltering("reactionType.specified=true", "reactionType.specified=false");
    }

    private void defaultReactionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReactionShouldBeFound(shouldBeFound);
        defaultReactionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReactionShouldBeFound(String filter) throws Exception {
        restReactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].targetId").value(hasItem(DEFAULT_TARGET_ID.intValue())))
            .andExpect(jsonPath("$.[*].reactionType").value(hasItem(DEFAULT_REACTION_TYPE.toString())));

        // Check, that the count call also returns 1
        restReactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReactionShouldNotBeFound(String filter) throws Exception {
        restReactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReaction() throws Exception {
        // Get the reaction
        restReactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReaction() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reaction
        Reaction updatedReaction = reactionRepository.findById(reaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReaction are not directly saved in db
        em.detach(updatedReaction);
        updatedReaction.deviceId(UPDATED_DEVICE_ID).targetId(UPDATED_TARGET_ID).reactionType(UPDATED_REACTION_TYPE);
        ReactionDTO reactionDTO = reactionMapper.toDto(updatedReaction);

        restReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReactionToMatchAllProperties(updatedReaction);
    }

    @Test
    @Transactional
    void putNonExistingReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReactionWithPatch() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reaction using partial update
        Reaction partialUpdatedReaction = new Reaction();
        partialUpdatedReaction.setId(reaction.getId());

        partialUpdatedReaction.targetId(UPDATED_TARGET_ID);

        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReaction))
            )
            .andExpect(status().isOk());

        // Validate the Reaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReactionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReaction, reaction), getPersistedReaction(reaction));
    }

    @Test
    @Transactional
    void fullUpdateReactionWithPatch() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reaction using partial update
        Reaction partialUpdatedReaction = new Reaction();
        partialUpdatedReaction.setId(reaction.getId());

        partialUpdatedReaction.deviceId(UPDATED_DEVICE_ID).targetId(UPDATED_TARGET_ID).reactionType(UPDATED_REACTION_TYPE);

        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReaction))
            )
            .andExpect(status().isOk());

        // Validate the Reaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReactionUpdatableFieldsEquals(partialUpdatedReaction, getPersistedReaction(partialUpdatedReaction));
    }

    @Test
    @Transactional
    void patchNonExistingReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reaction.setId(longCount.incrementAndGet());

        // Create the Reaction
        ReactionDTO reactionDTO = reactionMapper.toDto(reaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReaction() throws Exception {
        // Initialize the database
        insertedReaction = reactionRepository.saveAndFlush(reaction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reaction
        restReactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, reaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reactionRepository.count();
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

    protected Reaction getPersistedReaction(Reaction reaction) {
        return reactionRepository.findById(reaction.getId()).orElseThrow();
    }

    protected void assertPersistedReactionToMatchAllProperties(Reaction expectedReaction) {
        assertReactionAllPropertiesEquals(expectedReaction, getPersistedReaction(expectedReaction));
    }

    protected void assertPersistedReactionToMatchUpdatableProperties(Reaction expectedReaction) {
        assertReactionAllUpdatablePropertiesEquals(expectedReaction, getPersistedReaction(expectedReaction));
    }
}
