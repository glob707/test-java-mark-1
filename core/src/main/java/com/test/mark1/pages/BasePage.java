package com.test.mark1.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasePage {
    protected final Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    @Step("Open: {url}")
    public void visit(String url) {
        page.navigate(url);
    }

    @Step("Reload page")
    public void reload() {
        page.reload();
    }

    public String url() {
        return page.url();
    }

    public String title() {
        return page.title();
    }

    @Step("Check URL: {expectedUrl}")
    public void urlShouldBe(String expectedUrl) {
        assertThat(page).hasURL(expectedUrl);
    }

    @Step("Check URL contains: {partialUrl}")
    public void urlShouldContain(String partialUrl) {
        assertThat(page).hasURL(Pattern.compile(".*" + Pattern.quote(partialUrl) + ".*"));
    }

    @Step("Wait for URL: {expectedUrl}")
    public void waitForUrl(String expectedUrl) {
        page.waitForURL(expectedUrl);
    }

    @Step("Wait {milliseconds}ms")
    public void waitFor(int milliseconds) {
        page.waitForTimeout(milliseconds);
    }

    @Step("Wait for element: {selector}")
    public void waitForElement(String selector) {
        page.waitForSelector(selector);
    }

    @Step("Wait for element visible: {selector}")
    public void waitForElementVisible(String selector) {
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
            .setState(WaitForSelectorState.VISIBLE));
    }

    @Step("Press key: {key}")
    public void pressKey(String key) {
        page.keyboard().press(key);
    }

    @Step("Scroll to top")
    public void scrollToTop() {
        page.evaluate("window.scrollTo(0, 0)");
    }

    @Step("Scroll to bottom")
    public void scrollToBottom() {
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
    }

    @Step("Accept dialog after action")
    public void acceptDialog(Runnable action) {
        page.onceDialog(dialog -> dialog.accept());
        action.run();
    }

    @Step("Dismiss dialog after action")
    public void dismissDialog(Runnable action) {
        page.onceDialog(dialog -> dialog.dismiss());
        action.run();
    }

    @Step("Switch to new tab")
    public void switchToNewTab() {
        var pages = page.context().pages();
        if (pages.isEmpty()) return;
        pages.get(pages.size() - 1).bringToFront();
    }

    @Step("Set zoom: {zoom}%")
    public void setZoom(int zoom) {
        if (zoom <= 0) throw new IllegalArgumentException("zoom must be positive, got: " + zoom);
        page.evaluate("document.body.style.zoom = '" + zoom + "%'");
    }

    public byte[] screenshot() {
        return page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
    }

    public String source() {
        return page.content();
    }
}
