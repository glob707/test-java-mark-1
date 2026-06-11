package com.test.mark1.utils;

import net.datafaker.Faker;

import java.util.Locale;

public class Generator {
    private static final Faker faker = new Faker(
        new Locale(System.getProperty("faker.locale", "en")));

    public static String randomString(int length) {
        return faker.lorem().characters(length, true, true);
    }

    public static String randomName() {
        return faker.animal().name();
    }

    public static int randomInt(int min, int max) {
        return faker.number().numberBetween(min, max);
    }

    public static String email() {
        return faker.internet().emailAddress();
    }
}
