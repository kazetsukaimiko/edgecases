package com.example.nitrite.support;

import org.dizitart.no2.Nitrite;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class HappyFamilyTestBase {
    protected static final Random RANDOM = new Random(System.currentTimeMillis());

    protected static final List<String> DESCRIPTIONS = Arrays.asList(
        "Careful",
        "Anxious",
        "Pretentious",
        "Lazy",
        "Insane",
        "Frustrating",
        "Basic",
        "Illustrious",
        "Limp"
    );

    protected static final List<String> THINGS = Arrays.asList(
        "Bus",
        "Noodle",
        "Orange",
        "File Cabinet",
        "Tank",
        "Speaker",
        "Tube",
        "Llama"
    );

    protected static <T> T randomElementFrom(List<T> list) {
        return list == null || list.isEmpty() ?
            null : list.get(RANDOM.nextInt(list.size()));
    }

    protected static String randomName() {
        return randomElementFrom(DESCRIPTIONS) + " " + randomElementFrom(THINGS);
    }

    protected Pet spawn(String name) {
        Pet spawned = RANDOM.nextBoolean() ?
            new Dog() : new Cat();
        spawned.setName(name);
        return spawned;
    }

    protected Cat spawnCat() {
        Cat c = new Cat();
        c.setName(randomName());
        return c;
    }

    protected Dog spawnDog() {
        Dog d = new Dog();
        d.setName(randomName());
        return d;
    }

    protected Nitrite getDb() {
        Path nitritePath = Paths.get("/tmp", UUID.randomUUID().toString());
        return Nitrite.builder()
                .compressed()
                .filePath(nitritePath.toFile())
                .openOrCreate("nitrite", "nitrite");
    }


}
