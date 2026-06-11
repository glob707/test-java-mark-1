package com.test.mark1.core;

import com.microsoft.playwright.Locator;

@FunctionalInterface
public interface Condition {
    void verify(Locator locator);
}
