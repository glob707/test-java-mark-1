package com.test.mark1.elements;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

public class Element {
    protected final Page page;
    protected final Locator locator;
    protected final String description;
    protected final String selector;

    public Element(Page page, String selector) {
        this(page, selector, selector);
    }

    public Element(Page page, String selector, String description) {
        this.page = page;
        this.description = description;
        this.selector = selector;
        this.locator = resolveLocator(page, selector);
    }

    public Element(Page page, String selector, String description, int timeoutSeconds) {
        this.page = page;
        this.description = description;
        this.selector = selector;
        this.locator = page.locator(selector);
    }

    protected Element(Page page, Locator locator, String description, String selector) {
        this.page = page;
        this.locator = locator;
        this.description = description;
        this.selector = selector;
    }

    private static Locator resolveLocator(Page page, String selector) {
        String trimmed = selector.trim();
        if (trimmed.startsWith("//") || trimmed.startsWith("(") || trimmed.startsWith("./")) {
            return page.locator("xpath=" + trimmed);
        }
        if (trimmed.startsWith("css=")) {
            return page.locator(trimmed);
        }
        if (trimmed.startsWith("text=")) {
            return page.getByText(trimmed.substring(5));
        }
        if (trimmed.matches("^[\\w\\s\\-]+$")
            && !trimmed.contains("#")
            && !trimmed.contains(".")
            && !trimmed.contains("[")
            && !trimmed.contains("=")
            && !trimmed.contains(":")
            && !trimmed.contains(">")) {
            return page.getByText(trimmed, new Page.GetByTextOptions().setExact(true));
        }
        return page.locator(trimmed);
    }

    public static Element byTestId(Page page, String testId) {
        return new Element(page, page.getByTestId(testId),
            "data-testid=" + testId, "byTestId: " + testId);
    }

    public static Element byTestId(Page page, String testId, String description) {
        return new Element(page, page.getByTestId(testId), description, "byTestId: " + testId);
    }

    public static Element byRole(Page page, AriaRole role, String name) {
        return new Element(page, page.getByRole(role, new Page.GetByRoleOptions().setName(name)),
            "role=" + role + " name=" + name, "byRole: " + role + " " + name);
    }

    public static Element byLabel(Page page, String label) {
        return new Element(page, page.getByLabel(label), "label=" + label, "byLabel: " + label);
    }

    public static Element byPlaceholder(Page page, String placeholder) {
        return new Element(page, page.getByPlaceholder(placeholder),
            "placeholder=" + placeholder, "byPlaceholder: " + placeholder);
    }

    public static Element byAltText(Page page, String altText) {
        return new Element(page, page.getByAltText(altText), "alt=" + altText, "byAltText: " + altText);
    }

    @Step("Check: {description}")
    public Element should(Condition condition) {
        try {
            condition.verify(locator);
        } catch (AssertionError e) {
            throw new AssertionError(description + " - condition failed:\n" + e.getMessage(), e);
        }
        return this;
    }

    @Step("Check not: {description}")
    public Element shouldNot(Condition condition) {
        try {
            new NotCondition(condition).verify(locator);
        } catch (AssertionError e) {
            throw new AssertionError(description + " - condition should not match", e);
        }
        return this;
    }

    @Step("Click: {description}")
    public void click() {
        locator.click();
    }

    @Step("Text from {description}")
    public String text() {
        return locator.textContent();
    }

    @Step("Inner text from {description}")
    public String innerText() {
        return locator.innerText();
    }

    @Step("Check {description} visible")
    public boolean isVisible() { return locator.isVisible(); }

    @Step("Check {description} hidden")
    public boolean isHidden() { return locator.isHidden(); }

    @Step("Check {description} enabled")
    public boolean isEnabled() { return locator.isEnabled(); }

    @Step("Check {description} disabled")
    public boolean isDisabled() { return locator.isDisabled(); }

    @Step("Check {description} checked")
    public boolean isChecked() { return locator.isChecked(); }

    @Step("Count {description}")
    public int count() { return locator.count(); }

    @Step("Get attribute '{name}' from {description}")
    public String getAttribute(String name) { return locator.getAttribute(name); }

    public Element first() {
        return new Element(page, locator.first(), description + " (first)", selector);
    }

    public Element nth(int index) {
        return new Element(page, locator.nth(index), description + " [" + index + "]", selector);
    }

    public Element withTimeout(int timeoutSeconds) {
        return new Element(page, locator, description + " (timeout: " + timeoutSeconds + "s)", selector);
    }
}
