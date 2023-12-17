package com.movii.domain;

import static com.movii.domain.GenreTestSamples.*;
import static com.movii.domain.MovieTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.movii.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GenreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Genre.class);
        Genre genre1 = getGenreSample1();
        Genre genre2 = new Genre();
        assertThat(genre1).isNotEqualTo(genre2);

        genre2.setId(genre1.getId());
        assertThat(genre1).isEqualTo(genre2);

        genre2 = getGenreSample2();
        assertThat(genre1).isNotEqualTo(genre2);
    }

    @Test
    void movieTest() throws Exception {
        Genre genre = getGenreRandomSampleGenerator();
        Movie movieBack = getMovieRandomSampleGenerator();

        genre.addMovie(movieBack);
        assertThat(genre.getMovies()).containsOnly(movieBack);
        assertThat(movieBack.getGenre()).isEqualTo(genre);

        genre.removeMovie(movieBack);
        assertThat(genre.getMovies()).doesNotContain(movieBack);
        assertThat(movieBack.getGenre()).isNull();

        genre.movies(new HashSet<>(Set.of(movieBack)));
        assertThat(genre.getMovies()).containsOnly(movieBack);
        assertThat(movieBack.getGenre()).isEqualTo(genre);

        genre.setMovies(new HashSet<>());
        assertThat(genre.getMovies()).doesNotContain(movieBack);
        assertThat(movieBack.getGenre()).isNull();
    }
}
