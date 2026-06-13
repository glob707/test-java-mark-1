package com.test.mark1.elements;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import io.qameta.allure.Step;

public class Select extends Element {

    public Select(Page page, String selector) {
        super(page, selector, "Select");
    }

    public Select(Page page, String selector, String description) {
        super(page, selector, description);
    }

    public Select(Page page, String selector, String description, int timeout) {
        super(page, selector, description, timeout);
    }

    @Step("Select by value '{value}' in {description}")
    public void selectByValue(String value) {
        locator.selectOption(value);
    }

    @Step("Select by text '{label}' in {description}")
    public void selectByVisibleText(String label) {
        locator.selectOption(new SelectOption().setLabel(label));
    }

    @Step("Select by index {index} in {description}")
    public void selectByIndex(int index) {
        locator.selectOption(new SelectOption().setIndex(index));
    }
}
