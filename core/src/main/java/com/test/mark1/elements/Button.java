package com.test.mark1.elements;

import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class Button extends Element {

    public Button(Page page, String selector) {
        super(page, selector, "Button");
    }

    public Button(Page page, String selector, String description) {
        super(page, selector, description);
    }

    public Button(Page page, String selector, String description, int timeout) {
        super(page, selector, description, timeout);
    }

    @Override
    @Step("Click: {description}")
    public void click() {
        super.click();
    }

    @Step("Hover: {description}")
    public void hover() {
        locator.hover();
    }
}
