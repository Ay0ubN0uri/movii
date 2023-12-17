package com.movii.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MovieTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Movie getMovieSample1() {
        return new Movie()
            .id(1L)
            .title("title1")
            .tmbdId("tmbdId1")
            .videoUrl("videoUrl1")
            .duration("duration1")
            .youtubeTrailer("youtubeTrailer1")
            .views(1L)
            .director("director1");
    }

    public static Movie getMovieSample2() {
        return new Movie()
            .id(2L)
            .title("title2")
            .tmbdId("tmbdId2")
            .videoUrl("videoUrl2")
            .duration("duration2")
            .youtubeTrailer("youtubeTrailer2")
            .views(2L)
            .director("director2");
    }

    public static Movie getMovieRandomSampleGenerator() {
        return new Movie()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .tmbdId(UUID.randomUUID().toString())
            .videoUrl(UUID.randomUUID().toString())
            .duration(UUID.randomUUID().toString())
            .youtubeTrailer(UUID.randomUUID().toString())
            .views(longCount.incrementAndGet())
            .director(UUID.randomUUID().toString());
    }
}
