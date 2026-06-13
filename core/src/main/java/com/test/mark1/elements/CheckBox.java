package com.test.mark1.elements;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class CheckBox extends Element {

    public CheckBox(Page page, String selector) {
        super(page, selector, "CheckBox");
    }

    public CheckBox(Page page, String selector, String description) {
        super(page, selector, description);
    }

    public CheckBox(Page page, String selector, String description, int timeoutSeconds) {
        super(page, selector, description, timeoutSeconds);
    }

    @Step("Check: {description}")
    public void check() {
        locator.check();
    }

    @Step("Uncheck: {description}")
    public void uncheck() {
        locator.uncheck();
    }
}
