package com.java_selenium.tests.api_tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Rest_API_Test_01
{
    @Test
    public void testGetRequest() {
        // Set the base URL
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // Perform the GET request
        Response response = RestAssured.given()
                                       .when()
                                       .get("/posts/1")
                                       .then()
                                       .statusCode(200)
                                       .extract()
                                       .response();

        // Validate response content
        String title = response.jsonPath().getString("title");
        Assert.assertEquals(title, "sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
    }

}
