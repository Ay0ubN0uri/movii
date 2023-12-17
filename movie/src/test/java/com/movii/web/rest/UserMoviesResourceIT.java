package com.movii.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.movii.IntegrationTest;
import com.movii.domain.UserMovies;
import com.movii.repository.UserMoviesRepository;
import com.movii.service.UserMoviesService;
import com.movii.service.dto.UserMoviesDTO;
import com.movii.service.mapper.UserMoviesMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserMoviesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserMoviesResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/user-movies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserMoviesRepository userMoviesRepository;

    @Mock
    private UserMoviesRepository userMoviesRepositoryMock;

    @Autowired
    private UserMoviesMapper userMoviesMapper;

    @Mock
    private UserMoviesService userMoviesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserMoviesMockMvc;

    private UserMovies userMovies;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMovies createEntity(EntityManager em) {
        UserMovies userMovies = new UserMovies().userId(DEFAULT_USER_ID);
        return userMovies;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserMovies createUpdatedEntity(EntityManager em) {
        UserMovies userMovies = new UserMovies().userId(UPDATED_USER_ID);
        return userMovies;
    }

    @BeforeEach
    public void initTest() {
        userMovies = createEntity(em);
    }

    @Test
    @Transactional
    void createUserMovies() throws Exception {
        int databaseSizeBeforeCreate = userMoviesRepository.findAll().size();
        // Create the UserMovies
        UserMoviesDTO userMoviesDTO = userMoviesMapper.toDto(userMovies);
        restUserMoviesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMoviesDTO)))
            .andExpect(status().isCreated());

        // Validate the UserMovies in the database
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeCreate + 1);
        UserMovies testUserMovies = userMoviesList.get(userMoviesList.size() - 1);
        assertThat(testUserMovies.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createUserMoviesWithExistingId() throws Exception {
        // Create the UserMovies with an existing ID
        userMovies.setId(1L);
        UserMoviesDTO userMoviesDTO = userMoviesMapper.toDto(userMovies);

        int databaseSizeBeforeCreate = userMoviesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserMoviesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMoviesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserMovies in the database
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserMovies() throws Exception {
        // Initialize the database
        userMoviesRepository.saveAndFlush(userMovies);

        // Get all the userMoviesList
        restUserMoviesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userMovies.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserMoviesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userMoviesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserMoviesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userMoviesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserMoviesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userMoviesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserMoviesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userMoviesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserMovies() throws Exception {
        // Initialize the database
        userMoviesRepository.saveAndFlush(userMovies);

        // Get the userMovies
        restUserMoviesMockMvc
            .perform(get(ENTITY_API_URL_ID, userMovies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userMovies.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserMovies() throws Exception {
        // Get the userMovies
        restUserMoviesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserMovies() throws Exception {
        // Initialize the database
        userMoviesRepository.saveAndFlush(userMovies);

        int databaseSizeBeforeUpdate = userMoviesRepository.findAll().size();

        // Update the userMovies
        UserMovies updatedUserMovies = userMoviesRepository.findById(userMovies.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserMovies are not directly saved in db
        em.detach(updatedUserMovies);
        updatedUserMovies.userId(UPDATED_USER_ID);
        UserMoviesDTO userMoviesDTO = userMoviesMapper.toDto(updatedUserMovies);

        restUserMoviesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userMoviesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMoviesDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserMovies in the database
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeUpdate);
        UserMovies testUserMovies = userMoviesList.get(userMoviesList.size() - 1);
        assertThat(testUserMovies.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingUserMovies() throws Exception {
        int databaseSizeBeforeUpdate = userMoviesRepository.findAll().size();
        userMovies.setId(longCount.incrementAndGet());

        // Create the UserMovies
        UserMoviesDTO userMoviesDTO = userMoviesMapper.toDto(userMovies);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMoviesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userMoviesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMoviesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMovies in the database
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserMovies() throws Exception {
        int databaseSizeBeforeUpdate = userMoviesRepository.findAll().size();
        userMovies.setId(longCount.incrementAndGet());

        // Create the UserMovies
        UserMoviesDTO userMoviesDTO = userMoviesMapper.toDto(userMovies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMoviesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userMoviesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMovies in the database
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserMovies() throws Exception {
        int databaseSizeBeforeUpdate = userMoviesRepository.findAll().size();
        userMovies.setId(longCount.incrementAndGet());

        // Create the UserMovies
        UserMoviesDTO userMoviesDTO = userMoviesMapper.toDto(userMovies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMoviesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userMoviesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMovies in the database
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserMoviesWithPatch() throws Exception {
        // Initialize the database
        userMoviesRepository.saveAndFlush(userMovies);

        int databaseSizeBeforeUpdate = userMoviesRepository.findAll().size();

        // Update the userMovies using partial update
        UserMovies partialUpdatedUserMovies = new UserMovies();
        partialUpdatedUserMovies.setId(userMovies.getId());

        restUserMoviesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMovies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserMovies))
            )
            .andExpect(status().isOk());

        // Validate the UserMovies in the database
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeUpdate);
        UserMovies testUserMovies = userMoviesList.get(userMoviesList.size() - 1);
        assertThat(testUserMovies.getUserId()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateUserMoviesWithPatch() throws Exception {
        // Initialize the database
        userMoviesRepository.saveAndFlush(userMovies);

        int databaseSizeBeforeUpdate = userMoviesRepository.findAll().size();

        // Update the userMovies using partial update
        UserMovies partialUpdatedUserMovies = new UserMovies();
        partialUpdatedUserMovies.setId(userMovies.getId());

        partialUpdatedUserMovies.userId(UPDATED_USER_ID);

        restUserMoviesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserMovies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserMovies))
            )
            .andExpect(status().isOk());

        // Validate the UserMovies in the database
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeUpdate);
        UserMovies testUserMovies = userMoviesList.get(userMoviesList.size() - 1);
        assertThat(testUserMovies.getUserId()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingUserMovies() throws Exception {
        int databaseSizeBeforeUpdate = userMoviesRepository.findAll().size();
        userMovies.setId(longCount.incrementAndGet());

        // Create the UserMovies
        UserMoviesDTO userMoviesDTO = userMoviesMapper.toDto(userMovies);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserMoviesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userMoviesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userMoviesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMovies in the database
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserMovies() throws Exception {
        int databaseSizeBeforeUpdate = userMoviesRepository.findAll().size();
        userMovies.setId(longCount.incrementAndGet());

        // Create the UserMovies
        UserMoviesDTO userMoviesDTO = userMoviesMapper.toDto(userMovies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMoviesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userMoviesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserMovies in the database
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserMovies() throws Exception {
        int databaseSizeBeforeUpdate = userMoviesRepository.findAll().size();
        userMovies.setId(longCount.incrementAndGet());

        // Create the UserMovies
        UserMoviesDTO userMoviesDTO = userMoviesMapper.toDto(userMovies);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserMoviesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userMoviesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserMovies in the database
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserMovies() throws Exception {
        // Initialize the database
        userMoviesRepository.saveAndFlush(userMovies);

        int databaseSizeBeforeDelete = userMoviesRepository.findAll().size();

        // Delete the userMovies
        restUserMoviesMockMvc
            .perform(delete(ENTITY_API_URL_ID, userMovies.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserMovies> userMoviesList = userMoviesRepository.findAll();
        assertThat(userMoviesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
