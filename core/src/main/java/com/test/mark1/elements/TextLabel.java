package com.test.mark1.elements;

import com.microsoft.playwright.Page;

public class TextLabel extends Element {

    public TextLabel(Page page, String selector) {
        super(page, selector, "TextLabel");
    }

    public TextLabel(Page page, String selector, String description) {
        super(page, selector, description);
    }

    public TextLabel(Page page, String selector, String description, int timeoutSeconds) {
        super(page, selector, description, timeoutSeconds);
    }
}
