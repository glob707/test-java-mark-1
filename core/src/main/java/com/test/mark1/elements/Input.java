package com.test.mark1.elements;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class Input extends Element {

    public Input(Page page, String selector) {
        super(page, selector, "Input");
    }

    public Input(Page page, String selector, String description) {
        super(page, selector, description);
    }

    public Input(Page page, String selector, String description, int timeout) {
        super(page, selector, description, timeout);
    }

    @Step("Fill '{text}' in {description}")
    public void fill(String text) {
        locator.fill(text);
    }

    @Step("Clear {description}")
    public void clear() {
        locator.clear();
    }

    @Step("Clear and fill '{text}' in {description}")
    public void fillWithClear(String text) {
        locator.clear();
        locator.fill(text);
    }

    @Step("Type slowly '{text}' in {description}")
    public void typeSlowly(String text) {
        locator.pressSequentially(text, new Locator.PressSequentiallyOptions().setDelay(50));
    }
}
