package com.test.mark1;

import com.microsoft.playwright.Page;
import com.test.mark1.config.Settings;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.setDefaultAssertionTimeout;

@Listeners(com.test.mark1.listeners.AllureListener.class)
public class BaseTest {

    private static final List<Pattern> CONSOLE_ERROR_EXCEPTIONS = List.of(
        Pattern.compile(".*favicon\\.ico.*"),
        Pattern.compile(".*404.*")
    );

    private final ThreadLocal<String> testNameHolder = new ThreadLocal<>();
    private final ThreadLocal<List<String>> consoleErrors = ThreadLocal.withInitial(ArrayList::new);

    @BeforeSuite(alwaysRun = true)
    public void startPlaywright() {
        setDefaultAssertionTimeout(Settings.getTimeout() * 1000L);
        PlaywrightManager.start();
    }

    @BeforeMethod(alwaysRun = true)
    public void createPage(Method method) {
        consoleErrors.get().clear();
        testNameHolder.set(getClass().getName() + "." + method.getName());
        PlaywrightManager.createContext(testNameHolder.get());

        page().onConsoleMessage(msg -> {
            if ("error".equals(msg.type()) || "warning".equals(msg.type())) {
                String text = msg.text();
                boolean isExpected = CONSOLE_ERROR_EXCEPTIONS.stream()
                    .anyMatch(p -> p.matcher(text).matches());
                if (!isExpected) {
                    consoleErrors.get().add("[" + msg.type() + "] " + text);
                }
            }
        });
    }

    @AfterMethod(alwaysRun = true)
    public void closePage() {
        var errors = consoleErrors.get();
        if (!errors.isEmpty()) {
            org.testng.Assert.fail("Console errors detected:\n" + String.join("\n", errors));
        }
        PlaywrightManager.closeContext();
        testNameHolder.remove();
    }

    @AfterSuite(alwaysRun = true)
    public void stopPlaywright() {
        PlaywrightManager.stop();
    }

    protected Page page() {
        return PlaywrightManager.getPage();
    }
}
