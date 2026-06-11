package com.test.mark1.core;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class Have {
    private Have() {}

    public static Condition text(String expected) {
        return loc -> assertThat(loc).hasText(expected);
    }

    public static Condition textContaining(String text) {
        return loc -> assertThat(loc).containsText(text);
    }

    public static Condition count(int expected) {
        return loc -> assertThat(loc).hasCount(expected);
    }

    public static Condition attribute(String name, String value) {
        return loc -> assertThat(loc).hasAttribute(name, value);
    }

    public static Condition cssClass(String className) {
        return loc -> assertThat(loc).hasClass(className);
    }

    public static Condition containsClass(String className) {
        return loc -> assertThat(loc).containsClass(className);
    }

    public static Condition not(Condition condition) {
        return new NotCondition(condition);
    }

    public static Condition value(String expected) {
        return loc -> assertThat(loc).hasValue(expected);
    }
}
