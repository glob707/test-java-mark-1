package com.test.mark1.api.client;

import com.test.mark1.config.Settings;

public class ApiConfig {
    private final String baseUrl;
    private String authToken;

    public ApiConfig() {
        this.baseUrl = Settings.getBaseApiUrl();
    }

    public ApiConfig(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
