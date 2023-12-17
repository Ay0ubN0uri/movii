package com.movii.domain;

import static com.movii.domain.MovieTestSamples.*;
import static com.movii.domain.UserMoviesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.movii.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserMoviesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMovies.class);
        UserMovies userMovies1 = getUserMoviesSample1();
        UserMovies userMovies2 = new UserMovies();
        assertThat(userMovies1).isNotEqualTo(userMovies2);

        userMovies2.setId(userMovies1.getId());
        assertThat(userMovies1).isEqualTo(userMovies2);

        userMovies2 = getUserMoviesSample2();
        assertThat(userMovies1).isNotEqualTo(userMovies2);
    }

    @Test
    void movieTest() throws Exception {
        UserMovies userMovies = getUserMoviesRandomSampleGenerator();
        Movie movieBack = getMovieRandomSampleGenerator();

        userMovies.setMovie(movieBack);
        assertThat(userMovies.getMovie()).isEqualTo(movieBack);

        userMovies.movie(null);
        assertThat(userMovies.getMovie()).isNull();
    }
}
