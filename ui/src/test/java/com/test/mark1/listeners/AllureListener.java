package com.test.mark1.listeners;

import com.microsoft.playwright.Page;
import com.test.mark1.PlaywrightManager;
import io.qameta.allure.Allure;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;

public class AllureListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        PlaywrightManager.clearArtifacts();
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        Page page = PlaywrightManager.getPage();

        if (testResult.isSuccess()) {
            attachVideo();
            return;
        }

        if (page != null) {
            attachScreenshot(page);
            attachPageSource(page);
        }
        attachVideo();
        attachTrace();
    }

    private void attachScreenshot(Page page) {
        try {
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment("Screenshot", "image/png",
                new ByteArrayInputStream(screenshot), "png");
        } catch (Exception ignored) {}
    }

    private void attachPageSource(Page page) {
        try {
            String html = page.content();
            Allure.addAttachment("Page Source", "text/html", html, ".html");
        } catch (Exception ignored) {}
    }

    private void attachVideo() {
        var videoPath = PlaywrightManager.getVideoPath();
        if (videoPath == null || !Files.exists(videoPath)) return;
        try (InputStream is = Files.newInputStream(videoPath)) {
            Allure.addAttachment("Video", "video/webm", is, "webm");
        } catch (Exception ignored) {}
    }

    private void attachTrace() {
        var tracePath = PlaywrightManager.getTracePath();
        if (tracePath == null || !Files.exists(tracePath)) return;
        try (InputStream is = Files.newInputStream(tracePath)) {
            Allure.addAttachment("Trace", "application/zip", is, "zip");
        } catch (Exception ignored) {}
    }
}
