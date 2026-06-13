package com.test.mark1.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class Settings {
    private static final Logger log = LoggerFactory.getLogger(Settings.class);
    private static final Map<String, Object> config;
    private static final boolean dockerDetected;

    static {
        dockerDetected = Files.exists(Paths.get("/.dockerenv"));
        String env = System.getProperty("env", "dev");
        config = loadConfig(env);
    }

    private static Map<String, Object> loadConfig(String env) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            Map<String, Object> config = mapper.readValue(
                Settings.class.getClassLoader().getResourceAsStream("config.yaml"),
                new TypeReference<Map<String, Object>>() {}
            );
            if (config.containsKey(env)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> envConfig = (Map<String, Object>) config.get(env);
                mergeWithSystemProperties(envConfig);
                return envConfig;
            }
            log.warn("Environment '{}' not found in config.yaml", env);
            return Map.of();
        } catch (IOException e) {
            log.error("Failed to load config.yaml", e);
            return Map.of();
        }
    }

    private static void mergeWithSystemProperties(Map<String, Object> config) {
        System.getProperties().forEach((key, value) -> {
            if (key instanceof String && value instanceof String) {
                config.put((String) key, value);
            }
        });
    }

    private static String getString(String key, String defaultValue) {
        Object value = config.get(key);
        return value != null ? value.toString() : defaultValue;
    }

    private static int getInt(String key, int defaultValue) {
        Object value = config.get(key);
        if (value != null) {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                log.warn("Invalid integer value for key '{}': {}", key, value);
            }
        }
        return defaultValue;
    }

    private static boolean getBoolean(String key, boolean defaultValue) {
        Object value = config.get(key);
        if (value != null) {
            return Boolean.parseBoolean(value.toString());
        }
        return defaultValue;
    }

    public static boolean isRunningInDocker() {
        return dockerDetected;
    }

    public static String getBaseUrl() {
        return getString("base.url", "http://localhost:3000");
    }

    public static String getBaseApiUrl() {
        return getString("base.api.url", "http://localhost:8080");
    }

    public static String getBrowser() {
        return getString("browser", "chromium");
    }

    public static int getTimeout() {
        int base = getInt("timeout", 10);
        return isRunningInDocker() ? Math.max(base, 30) : base;
    }

    public static String getEnv() {
        return getString("env", "dev");
    }

    public static boolean isHeadless() {
        return getBoolean("headless", false);
    }

    public static String getMockMode() {
        return getString("mock", "off");
    }

    public static boolean isMockOn() {
        return "on".equals(getMockMode());
    }

    public static boolean isMockRecord() {
        return "record".equals(getMockMode());
    }
}
