package com.movii.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserMoviesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserMovies getUserMoviesSample1() {
        return new UserMovies().id(1L).userId(1L);
    }

    public static UserMovies getUserMoviesSample2() {
        return new UserMovies().id(2L).userId(2L);
    }

    public static UserMovies getUserMoviesRandomSampleGenerator() {
        return new UserMovies().id(longCount.incrementAndGet()).userId(longCount.incrementAndGet());
    }
}
