package com.tangelogames.jobsystem.integration;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
        var result = responseBody.print();

        // verify
        assertThat(result, is("{com.tangelogames.jobsystem.jobs.CustomJob1=com.tangelogames.jobsystem.jobs.CustomJob1 RUNNING, com.tangelogames.jobsystem.jobs.CustomJob2=com.tangelogames.jobsystem.jobs.CustomJob2 WAITING}"));
    }
}