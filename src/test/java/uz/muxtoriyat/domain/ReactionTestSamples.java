package uz.muxtoriyat.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ReactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Reaction getReactionSample1() {
        return new Reaction().id(1L).deviceId("deviceId1").targetId(1L);
    }

    public static Reaction getReactionSample2() {
        return new Reaction().id(2L).deviceId("deviceId2").targetId(2L);
    }

    public static Reaction getReactionRandomSampleGenerator() {
        return new Reaction().id(longCount.incrementAndGet()).deviceId(UUID.randomUUID().toString()).targetId(longCount.incrementAndGet());
    }
}
