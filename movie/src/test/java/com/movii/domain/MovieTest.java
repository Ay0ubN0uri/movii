package com.movii.domain;

import static com.movii.domain.ActorTestSamples.*;
import static com.movii.domain.GenreTestSamples.*;
import static com.movii.domain.MovieTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.movii.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MovieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Movie.class);
        Movie movie1 = getMovieSample1();
        Movie movie2 = new Movie();
        assertThat(movie1).isNotEqualTo(movie2);

        movie2.setId(movie1.getId());
        assertThat(movie1).isEqualTo(movie2);

        movie2 = getMovieSample2();
        assertThat(movie1).isNotEqualTo(movie2);
    }

    @Test
    void genreTest() throws Exception {
        Movie movie = getMovieRandomSampleGenerator();
        Genre genreBack = getGenreRandomSampleGenerator();

        movie.setGenre(genreBack);
        assertThat(movie.getGenre()).isEqualTo(genreBack);

        movie.genre(null);
        assertThat(movie.getGenre()).isNull();
    }

    @Test
    void actorsTest() throws Exception {
        Movie movie = getMovieRandomSampleGenerator();
        Actor actorBack = getActorRandomSampleGenerator();

        movie.addActors(actorBack);
        assertThat(movie.getActors()).containsOnly(actorBack);
        assertThat(actorBack.getMovies()).containsOnly(movie);

        movie.removeActors(actorBack);
        assertThat(movie.getActors()).doesNotContain(actorBack);
        assertThat(actorBack.getMovies()).doesNotContain(movie);

        movie.actors(new HashSet<>(Set.of(actorBack)));
        assertThat(movie.getActors()).containsOnly(actorBack);
        assertThat(actorBack.getMovies()).containsOnly(movie);

        movie.setActors(new HashSet<>());
        assertThat(movie.getActors()).doesNotContain(actorBack);
        assertThat(actorBack.getMovies()).doesNotContain(movie);
    }
}
