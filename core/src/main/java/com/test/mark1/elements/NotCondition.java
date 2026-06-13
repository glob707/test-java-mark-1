package com.test.mark1.elements;

import com.microsoft.playwright.Locator;

public class NotCondition implements Condition {
    private final Condition condition;

    public NotCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public void verify(Locator locator) {
        try {
            condition.verify(locator);
        } catch (AssertionError e) {
            return;
        }
        throw new AssertionError("condition should not match");
    }
}
