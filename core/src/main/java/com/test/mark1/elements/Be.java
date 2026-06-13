package com.test.mark1.elements;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class Be {
    private Be() {}

    public static final Condition visible = loc -> assertThat(loc).isVisible();
    public static final Condition hidden = loc -> assertThat(loc).isHidden();
    public static final Condition enabled = loc -> assertThat(loc).isEnabled();
    public static final Condition disabled = loc -> assertThat(loc).isDisabled();
    public static final Condition checked = loc -> assertThat(loc).isChecked();
    public static final Condition focused = loc -> assertThat(loc).isFocused();
    public static final Condition empty = loc -> assertThat(loc).isEmpty();
    public static final Condition attached = loc -> assertThat(loc).isAttached();
}
