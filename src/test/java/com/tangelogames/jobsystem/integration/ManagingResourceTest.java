package com.tangelogames.jobsystem.integration;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ManagingResourceTest {

    @Test
    void testManagingStatusEndpoint() {
        final Response response = given()
                .when().get("/managing/status")
                .then()
                .statusCode(200)
                .extract().response();

        var responseBody = response.getBody();
        var responseString = responseBody.print();

        // verify
    }
}