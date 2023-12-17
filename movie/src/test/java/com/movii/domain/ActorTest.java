package com.movii.domain;

import static com.movii.domain.ActorTestSamples.*;
import static com.movii.domain.MovieTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.movii.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ActorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Actor.class);
        Actor actor1 = getActorSample1();
        Actor actor2 = new Actor();
        assertThat(actor1).isNotEqualTo(actor2);

        actor2.setId(actor1.getId());
        assertThat(actor1).isEqualTo(actor2);

        actor2 = getActorSample2();
        assertThat(actor1).isNotEqualTo(actor2);
    }

    @Test
    void moviesTest() throws Exception {
        Actor actor = getActorRandomSampleGenerator();
        Movie movieBack = getMovieRandomSampleGenerator();

        actor.addMovies(movieBack);
        assertThat(actor.getMovies()).containsOnly(movieBack);

        actor.removeMovies(movieBack);
        assertThat(actor.getMovies()).doesNotContain(movieBack);

        actor.movies(new HashSet<>(Set.of(movieBack)));
        assertThat(actor.getMovies()).containsOnly(movieBack);

        actor.setMovies(new HashSet<>());
        assertThat(actor.getMovies()).doesNotContain(movieBack);
    }
}
