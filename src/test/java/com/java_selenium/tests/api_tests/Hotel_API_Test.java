package com.java_selenium.tests.api_tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class Hotel_API_Test
{
    // Base URL for JSONPlaceholder API
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static int postId;

    @BeforeClass
    public void setup()
    {
        // Set the base URI for all requests
        RestAssured.baseURI = BASE_URL;
    }

    // Test 1: Fetch Available Posts (Simulate Fetching Available Hotels)
    @Test
    public void testFetchAvailableHotels() {
        // Sending GET request to fetch posts (available hotels)
        Response response = given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)  // Verify status code 200
                .contentType(ContentType.JSON)
                .extract()
                .response();

        // Extract and print the title of the first post (acting as hotel name)
        String postTitle = response.jsonPath().getString("[0].title"); // Get the title of the first post
        Assert.assertNotNull(postTitle, "Post title should not be null");
        System.out.println("Available Post: " + postTitle);
    }

    // Test 2: Book a Room (Simulate Posting a Booking)
    @Test
    public void testBookHotelRoom() {
        // JSON body for creating a new post (acting as booking a room)
        String bookingRequestBody = "{\n" +
                "  \"title\": \"Hotel Booking\",\n" +
                "  \"body\": \"Room booked for John Doe\",\n" +
                "  \"userId\": 1\n" +
                "}";

        // Sending POST request to create a new post (acting as booking)
        Response response = given()
                .contentType(ContentType.JSON)
                .body(bookingRequestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)  // Verify status code 201 (created)
                .extract()
                .response();

        // Extract the post ID from the response
        postId = response.jsonPath().getInt("id");
        Assert.assertNotNull(postId, "Post ID should not be null");
        System.out.println("Post ID (Booking ID): " + postId);
    }

    // Test 3: Get Booking Details (Simulate Fetching Booking Details)
    @Test(dependsOnMethods = "testBookHotelRoom") // Ensure booking exists before fetching details
    public void testGetBookingDetails() {
        // Sending GET request to fetch post details (acting as booking details)
        Response response = given()
                .pathParam("postId", postId)
                .when()
                .get("/posts/{postId}")
                .then()
                .statusCode(200)  // Verify status code 200
                .contentType(ContentType.JSON)
                .extract()
                .response();

        // Validate post details (guest name, booking details, etc.)
        String postTitle = response.jsonPath().getString("title");
        Assert.assertEquals(postTitle, "Hotel Booking", "Post title should match the booking title");
        System.out.println("Booking Details: " + response.prettyPrint());
    }

    // Test 4: Cancel the Booking (Simulate Deleting a Booking)
    @Test(dependsOnMethods = "testGetBookingDetails") // Ensure booking exists before canceling
    public void testCancelBooking() {
        // Sending DELETE request to cancel the post (cancel the booking)
        Response response = given()
                .pathParam("postId", postId)
                .when()
                .delete("/posts/{postId}")
                .then()
                .statusCode(200)  // Verify status code 200
                .extract()
                .response();

        // Verify that post was deleted successfully
        System.out.println("Post successfully canceled (deleted): " + postId);
    }
}
