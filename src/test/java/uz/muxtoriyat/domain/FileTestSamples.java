package uz.muxtoriyat.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static File getFileSample1() {
        return new File().id(1L).name("name1").description("description1").url("url1");
    }

    public static File getFileSample2() {
        return new File().id(2L).name("name2").description("description2").url("url2");
    }

    public static File getFileRandomSampleGenerator() {
        return new File()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .url(UUID.randomUUID().toString());
    }
}
