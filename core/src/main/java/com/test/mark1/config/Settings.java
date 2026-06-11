package com.test.mark1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Settings {
    private static final Logger log = LoggerFactory.getLogger(Settings.class);
    private static final Properties props = new Properties();
    private static final boolean dockerDetected;

    static {
        dockerDetected = Files.exists(Paths.get("/.dockerenv"));
        String env = System.getProperty("env", "dev");
        loadResource("config.properties");
        loadResource("config-" + env + ".properties");
        props.putAll(System.getProperties());
    }

    private static void loadResource(String name) {
        try (InputStream is = Settings.class.getClassLoader().getResourceAsStream(name)) {
            if (is != null) {
                props.load(is);
            } else {
                log.warn("Config resource not found: {}", name);
            }
        } catch (IOException e) {
            log.error("Failed to load config resource: {}", name, e);
        }
    }

    public static boolean isRunningInDocker() {
        return dockerDetected;
    }

    public static String getBaseUrl() {
        return props.getProperty("base.url", "http://localhost:3000");
    }

    public static String getBaseApiUrl() {
        return props.getProperty("base.api.url", "http://localhost:8080");
    }

    public static String getBrowser() {
        return props.getProperty("browser", "chromium");
    }

    public static int getTimeout() {
        int base = Integer.parseInt(props.getProperty("timeout", "10"));
        return isRunningInDocker() ? Math.max(base, 30) : base;
    }

    public static String getEnv() {
        return props.getProperty("env", "dev");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(props.getProperty("headless", "false"));
    }

    public static String getMockMode() {
        return props.getProperty("mock", "off");
    }

    public static boolean isMockOn() {
        return "on".equals(getMockMode());
    }

    public static boolean isMockRecord() {
        return "record".equals(getMockMode());
    }
}
