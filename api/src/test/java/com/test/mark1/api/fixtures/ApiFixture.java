package com.test.mark1.api.fixtures;

import com.test.mark1.api.client.ApiClient;
import com.test.mark1.api.client.ApiConfig;
import org.testng.annotations.BeforeClass;

public class ApiFixture {
    protected ApiClient client;
    protected ApiConfig config;

    @BeforeClass
    public void setupApi() {
        config = new ApiConfig();
        client = new ApiClient(config);
    }
}
