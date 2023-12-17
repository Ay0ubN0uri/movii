package com.movii.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.movii.IntegrationTest;
import com.movii.domain.Movie;
import com.movii.repository.MovieRepository;
import com.movii.service.MovieService;
import com.movii.service.dto.MovieDTO;
import com.movii.service.mapper.MovieMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
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
 * Integration tests for the {@link MovieResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MovieResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TMBD_ID = "AAAAAAAAAA";
    private static final String UPDATED_TMBD_ID = "BBBBBBBBBB";

    private static final byte[] DEFAULT_THUMBNAIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_THUMBNAIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_THUMBNAIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_THUMBNAIL_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_BANNER = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_BANNER = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_BANNER_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_BANNER_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_RELEASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RELEASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VIDEO_URL = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    private static final String DEFAULT_YOUTUBE_TRAILER = "AAAAAAAAAA";
    private static final String UPDATED_YOUTUBE_TRAILER = "BBBBBBBBBB";

    private static final Long DEFAULT_VIEWS = 1L;
    private static final Long UPDATED_VIEWS = 2L;

    private static final String DEFAULT_DIRECTOR = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTOR = "BBBBBBBBBB";

    private static final Double DEFAULT_AVERAGE_RATING = 1D;
    private static final Double UPDATED_AVERAGE_RATING = 2D;

    private static final String ENTITY_API_URL = "/api/movies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MovieRepository movieRepository;

    @Mock
    private MovieRepository movieRepositoryMock;

    @Autowired
    private MovieMapper movieMapper;

    @Mock
    private MovieService movieServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovieMockMvc;

    private Movie movie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createEntity(EntityManager em) {
        Movie movie = new Movie()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .tmbdId(DEFAULT_TMBD_ID)
            .thumbnail(DEFAULT_THUMBNAIL)
            .thumbnailContentType(DEFAULT_THUMBNAIL_CONTENT_TYPE)
            .banner(DEFAULT_BANNER)
            .bannerContentType(DEFAULT_BANNER_CONTENT_TYPE)
            .releaseDate(DEFAULT_RELEASE_DATE)
            .videoUrl(DEFAULT_VIDEO_URL)
            .duration(DEFAULT_DURATION)
            .youtubeTrailer(DEFAULT_YOUTUBE_TRAILER)
            .views(DEFAULT_VIEWS)
            .director(DEFAULT_DIRECTOR)
            .averageRating(DEFAULT_AVERAGE_RATING);
        return movie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createUpdatedEntity(EntityManager em) {
        Movie movie = new Movie()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .tmbdId(UPDATED_TMBD_ID)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .banner(UPDATED_BANNER)
            .bannerContentType(UPDATED_BANNER_CONTENT_TYPE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .videoUrl(UPDATED_VIDEO_URL)
            .duration(UPDATED_DURATION)
            .youtubeTrailer(UPDATED_YOUTUBE_TRAILER)
            .views(UPDATED_VIEWS)
            .director(UPDATED_DIRECTOR)
            .averageRating(UPDATED_AVERAGE_RATING);
        return movie;
    }

    @BeforeEach
    public void initTest() {
        movie = createEntity(em);
    }

    @Test
    @Transactional
    void createMovie() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();
        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);
        restMovieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isCreated());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate + 1);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMovie.getTmbdId()).isEqualTo(DEFAULT_TMBD_ID);
        assertThat(testMovie.getThumbnail()).isEqualTo(DEFAULT_THUMBNAIL);
        assertThat(testMovie.getThumbnailContentType()).isEqualTo(DEFAULT_THUMBNAIL_CONTENT_TYPE);
        assertThat(testMovie.getBanner()).isEqualTo(DEFAULT_BANNER);
        assertThat(testMovie.getBannerContentType()).isEqualTo(DEFAULT_BANNER_CONTENT_TYPE);
        assertThat(testMovie.getReleaseDate()).isEqualTo(DEFAULT_RELEASE_DATE);
        assertThat(testMovie.getVideoUrl()).isEqualTo(DEFAULT_VIDEO_URL);
        assertThat(testMovie.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testMovie.getYoutubeTrailer()).isEqualTo(DEFAULT_YOUTUBE_TRAILER);
        assertThat(testMovie.getViews()).isEqualTo(DEFAULT_VIEWS);
        assertThat(testMovie.getDirector()).isEqualTo(DEFAULT_DIRECTOR);
        assertThat(testMovie.getAverageRating()).isEqualTo(DEFAULT_AVERAGE_RATING);
    }

    @Test
    @Transactional
    void createMovieWithExistingId() throws Exception {
        // Create the Movie with an existing ID
        movie.setId(1L);
        MovieDTO movieDTO = movieMapper.toDto(movie);

        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setTitle(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReleaseDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setReleaseDate(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMovies() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList
        restMovieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(movie.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].tmbdId").value(hasItem(DEFAULT_TMBD_ID)))
            .andExpect(jsonPath("$.[*].thumbnailContentType").value(hasItem(DEFAULT_THUMBNAIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].thumbnail").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL))))
            .andExpect(jsonPath("$.[*].bannerContentType").value(hasItem(DEFAULT_BANNER_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].banner").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_BANNER))))
            .andExpect(jsonPath("$.[*].releaseDate").value(hasItem(DEFAULT_RELEASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(DEFAULT_VIDEO_URL)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].youtubeTrailer").value(hasItem(DEFAULT_YOUTUBE_TRAILER)))
            .andExpect(jsonPath("$.[*].views").value(hasItem(DEFAULT_VIEWS.intValue())))
            .andExpect(jsonPath("$.[*].director").value(hasItem(DEFAULT_DIRECTOR)))
            .andExpect(jsonPath("$.[*].averageRating").value(hasItem(DEFAULT_AVERAGE_RATING.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoviesWithEagerRelationshipsIsEnabled() throws Exception {
        when(movieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMovieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(movieServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoviesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(movieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMovieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(movieRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get the movie
        restMovieMockMvc
            .perform(get(ENTITY_API_URL_ID, movie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(movie.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.tmbdId").value(DEFAULT_TMBD_ID))
            .andExpect(jsonPath("$.thumbnailContentType").value(DEFAULT_THUMBNAIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.thumbnail").value(Base64.getEncoder().encodeToString(DEFAULT_THUMBNAIL)))
            .andExpect(jsonPath("$.bannerContentType").value(DEFAULT_BANNER_CONTENT_TYPE))
            .andExpect(jsonPath("$.banner").value(Base64.getEncoder().encodeToString(DEFAULT_BANNER)))
            .andExpect(jsonPath("$.releaseDate").value(DEFAULT_RELEASE_DATE.toString()))
            .andExpect(jsonPath("$.videoUrl").value(DEFAULT_VIDEO_URL))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.youtubeTrailer").value(DEFAULT_YOUTUBE_TRAILER))
            .andExpect(jsonPath("$.views").value(DEFAULT_VIEWS.intValue()))
            .andExpect(jsonPath("$.director").value(DEFAULT_DIRECTOR))
            .andExpect(jsonPath("$.averageRating").value(DEFAULT_AVERAGE_RATING.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingMovie() throws Exception {
        // Get the movie
        restMovieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie
        Movie updatedMovie = movieRepository.findById(movie.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMovie are not directly saved in db
        em.detach(updatedMovie);
        updatedMovie
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .tmbdId(UPDATED_TMBD_ID)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .banner(UPDATED_BANNER)
            .bannerContentType(UPDATED_BANNER_CONTENT_TYPE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .videoUrl(UPDATED_VIDEO_URL)
            .duration(UPDATED_DURATION)
            .youtubeTrailer(UPDATED_YOUTUBE_TRAILER)
            .views(UPDATED_VIEWS)
            .director(UPDATED_DIRECTOR)
            .averageRating(UPDATED_AVERAGE_RATING);
        MovieDTO movieDTO = movieMapper.toDto(updatedMovie);

        restMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movieDTO))
            )
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMovie.getTmbdId()).isEqualTo(UPDATED_TMBD_ID);
        assertThat(testMovie.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testMovie.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testMovie.getBanner()).isEqualTo(UPDATED_BANNER);
        assertThat(testMovie.getBannerContentType()).isEqualTo(UPDATED_BANNER_CONTENT_TYPE);
        assertThat(testMovie.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testMovie.getVideoUrl()).isEqualTo(UPDATED_VIDEO_URL);
        assertThat(testMovie.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testMovie.getYoutubeTrailer()).isEqualTo(UPDATED_YOUTUBE_TRAILER);
        assertThat(testMovie.getViews()).isEqualTo(UPDATED_VIEWS);
        assertThat(testMovie.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testMovie.getAverageRating()).isEqualTo(UPDATED_AVERAGE_RATING);
    }

    @Test
    @Transactional
    void putNonExistingMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(longCount.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(longCount.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(longCount.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMovieWithPatch() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie using partial update
        Movie partialUpdatedMovie = new Movie();
        partialUpdatedMovie.setId(movie.getId());

        partialUpdatedMovie
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .banner(UPDATED_BANNER)
            .bannerContentType(UPDATED_BANNER_CONTENT_TYPE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .duration(UPDATED_DURATION)
            .views(UPDATED_VIEWS)
            .director(UPDATED_DIRECTOR)
            .averageRating(UPDATED_AVERAGE_RATING);

        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovie))
            )
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMovie.getTmbdId()).isEqualTo(DEFAULT_TMBD_ID);
        assertThat(testMovie.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testMovie.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testMovie.getBanner()).isEqualTo(UPDATED_BANNER);
        assertThat(testMovie.getBannerContentType()).isEqualTo(UPDATED_BANNER_CONTENT_TYPE);
        assertThat(testMovie.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testMovie.getVideoUrl()).isEqualTo(DEFAULT_VIDEO_URL);
        assertThat(testMovie.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testMovie.getYoutubeTrailer()).isEqualTo(DEFAULT_YOUTUBE_TRAILER);
        assertThat(testMovie.getViews()).isEqualTo(UPDATED_VIEWS);
        assertThat(testMovie.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testMovie.getAverageRating()).isEqualTo(UPDATED_AVERAGE_RATING);
    }

    @Test
    @Transactional
    void fullUpdateMovieWithPatch() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie using partial update
        Movie partialUpdatedMovie = new Movie();
        partialUpdatedMovie.setId(movie.getId());

        partialUpdatedMovie
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .tmbdId(UPDATED_TMBD_ID)
            .thumbnail(UPDATED_THUMBNAIL)
            .thumbnailContentType(UPDATED_THUMBNAIL_CONTENT_TYPE)
            .banner(UPDATED_BANNER)
            .bannerContentType(UPDATED_BANNER_CONTENT_TYPE)
            .releaseDate(UPDATED_RELEASE_DATE)
            .videoUrl(UPDATED_VIDEO_URL)
            .duration(UPDATED_DURATION)
            .youtubeTrailer(UPDATED_YOUTUBE_TRAILER)
            .views(UPDATED_VIEWS)
            .director(UPDATED_DIRECTOR)
            .averageRating(UPDATED_AVERAGE_RATING);

        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovie))
            )
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMovie.getTmbdId()).isEqualTo(UPDATED_TMBD_ID);
        assertThat(testMovie.getThumbnail()).isEqualTo(UPDATED_THUMBNAIL);
        assertThat(testMovie.getThumbnailContentType()).isEqualTo(UPDATED_THUMBNAIL_CONTENT_TYPE);
        assertThat(testMovie.getBanner()).isEqualTo(UPDATED_BANNER);
        assertThat(testMovie.getBannerContentType()).isEqualTo(UPDATED_BANNER_CONTENT_TYPE);
        assertThat(testMovie.getReleaseDate()).isEqualTo(UPDATED_RELEASE_DATE);
        assertThat(testMovie.getVideoUrl()).isEqualTo(UPDATED_VIDEO_URL);
        assertThat(testMovie.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testMovie.getYoutubeTrailer()).isEqualTo(UPDATED_YOUTUBE_TRAILER);
        assertThat(testMovie.getViews()).isEqualTo(UPDATED_VIEWS);
        assertThat(testMovie.getDirector()).isEqualTo(UPDATED_DIRECTOR);
        assertThat(testMovie.getAverageRating()).isEqualTo(UPDATED_AVERAGE_RATING);
    }

    @Test
    @Transactional
    void patchNonExistingMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(longCount.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, movieDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(longCount.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setId(longCount.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeDelete = movieRepository.findAll().size();

        // Delete the movie
        restMovieMockMvc
            .perform(delete(ENTITY_API_URL_ID, movie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
