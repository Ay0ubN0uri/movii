package com.movii.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ActorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Actor getActorSample1() {
        return new Actor().id(1L).firstName("firstName1").lastName("lastName1").nationality("nationality1");
    }

    public static Actor getActorSample2() {
        return new Actor().id(2L).firstName("firstName2").lastName("lastName2").nationality("nationality2");
    }

    public static Actor getActorRandomSampleGenerator() {
        return new Actor()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .nationality(UUID.randomUUID().toString());
    }
}
