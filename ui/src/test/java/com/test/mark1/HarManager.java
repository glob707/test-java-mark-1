package com.test.mark1;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.test.mark1.config.Settings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HarManager {
    private static final Path HAR_DIR = Paths.get("target/har");

    public static Path harPath(String testName) {
        return HAR_DIR.resolve(testName.replaceAll("[^a-zA-Z0-9._-]", "_") + ".har");
    }

    public static void configureContext(BrowserContext ctx, String testName) {
        if (Settings.isMockOn()) {
            Path har = harPath(testName);
            if (!Files.exists(har)) {
                throw new IllegalStateException(
                    "HAR not found: " + har + " — run with -Dmock=record first");
            }
            ctx.routeFromHAR(har);
        }
    }

    public static Browser.NewContextOptions recordOptions() {
        return new Browser.NewContextOptions()
            .setViewportSize(1920, 1080)
            .setRecordVideoDir(Paths.get("target/videos"))
            .setRecordVideoSize(1920, 1080);
    }

    public static BrowserContext createRecordingContext(Browser browser, String testName) {
        Path har = harPath(testName);
        try {
            Files.createDirectories(HAR_DIR);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create HAR dir", e);
        }
        return browser.newContext(recordOptions().setRecordHarPath(har));
    }
}
