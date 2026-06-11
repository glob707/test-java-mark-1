package com.test.mark1.api.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class ApiClient {
    private final ApiConfig config;
    private final RequestSpecification spec;
    private final ResponseSpecification responseSpec;

    public ApiClient(ApiConfig config) {
        this.config = config;
        this.spec = new RequestSpecBuilder()
            .setBaseUri(config.getBaseUrl())
            .setContentType(ContentType.JSON)
            .addFilter(new AllureRestAssured())
            .build();
        this.responseSpec = new ResponseSpecBuilder().build();
    }

    public RequestSpecification request() {
        var req = RestAssured.given().spec(spec);
        if (config.getAuthToken() != null && !config.getAuthToken().isBlank()) {
            req.auth().oauth2(config.getAuthToken());
        }
        return req;
    }

    public ResponseSpecification responseSpec() {
        return responseSpec;
    }
}
