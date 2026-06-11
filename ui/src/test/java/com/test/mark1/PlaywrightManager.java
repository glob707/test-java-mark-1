package com.test.mark1;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.test.mark1.config.Settings;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PlaywrightManager {
    private static Playwright playwright;
    private static Browser browser;
    private static final ThreadLocal<BrowserContext> contextHolder = new ThreadLocal<>();
    private static final ThreadLocal<Page> pageHolder = new ThreadLocal<>();
    private static final ThreadLocal<Path> videoPathHolder = new ThreadLocal<>();
    private static final ThreadLocal<Path> tracePathHolder = new ThreadLocal<>();
    private static boolean started = false;

    public synchronized static void start() {
        if (started) return;
        playwright = Playwright.create();
        var options = new BrowserType.LaunchOptions()
            .setHeadless(Settings.isHeadless());
        browser = switch (Settings.getBrowser()) {
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> playwright.chromium().launch(options);
        };
        started = true;
        Runtime.getRuntime().addShutdownHook(new Thread(PlaywrightManager::stop));
    }

    public static void createContext() {
        createContext(null);
    }

    public static void createContext(String testName) {
        BrowserContext ctx;

        if (Settings.isMockRecord() && testName != null) {
            ctx = HarManager.createRecordingContext(browser, testName);
        } else {
            ctx = browser.newContext(HarManager.recordOptions());
            if (testName != null) {
                HarManager.configureContext(ctx, testName);
            }
        }

        ctx.tracing().start(new Tracing.StartOptions()
            .setScreenshots(true)
            .setSnapshots(true));

        contextHolder.set(ctx);
        pageHolder.set(ctx.newPage());
    }

    public static void closeContext() {
        Page page = pageHolder.get();
        BrowserContext ctx = contextHolder.get();

        if (page != null) {
            page.close();
            try {
                var video = page.video();
                if (video != null) {
                    videoPathHolder.set(video.path());
                }
            } catch (Exception ignored) {}
        }

        if (ctx != null) {
            Path tracePath = Paths.get("target/traces",
                "trace-" + System.currentTimeMillis() + ".zip");
            ctx.tracing().stop(new Tracing.StopOptions().setPath(tracePath));
            tracePathHolder.set(tracePath);
            ctx.close();
        }

        pageHolder.remove();
        contextHolder.remove();
    }

    public synchronized static void stop() {
        if (!started) return;
        try { if (browser != null) browser.close(); } catch (Exception ignored) {}
        try { if (playwright != null) playwright.close(); } catch (Exception ignored) {}
        started = false;
        playwright = null;
        browser = null;
    }

    public static Page getPage() { return pageHolder.get(); }
    public static BrowserContext getContext() { return contextHolder.get(); }
    public static Path getVideoPath() { return videoPathHolder.get(); }
    public static Path getTracePath() { return tracePathHolder.get(); }

    public static void clearArtifacts() {
        videoPathHolder.remove();
        tracePathHolder.remove();
    }
}
