package com.java_selenium.tests.api_tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


import java.util.List;

import static io.restassured.RestAssured.given;

public class Hotel_API_Test2
{
    // Base URL for the Mock API
    private static final String BASE_URL = "https://677d4dbc4496848554ca042d.mockapi.io/api/v1";

    private static String bookingId;

    @BeforeClass
    public void setup()
    {
        // Set base URI for all requests
        RestAssured.baseURI = BASE_URL;
    }

    // Test 1: Fetch Available Hotels
    @Test(priority = 1)
    public void testFetchAvailableHotels() {
        // Sending GET request to fetch hotels (available rooms)
        Response response = given()
                .when()
                .get("/hotels")
                .then()
                .statusCode(200)  // Verify status code 200
                .contentType(ContentType.JSON)
                .extract()
                .response();

        // Print the full response to understand its structure
        System.out.println("Response: " + response.asString());

        // Extract the list of hotel names using the correct JSON path
        List<String> hotelNames = response.jsonPath().getList("name");

        // Assert that the list of hotels is not empty
        Assert.assertFalse(hotelNames.isEmpty(), "No hotels found in response");

        System.out.println("Available Hotels: ");
        // Print out the list of available hotel names
        for (String hotel : hotelNames)
        {
            System.out.println(hotel);
        }
    }




    // Test 2: Book a Hotel Room
    @Test(priority = 2, dependsOnMethods = "testFetchAvailableHotels")
    public void testBookHotelRoom() {
        // JSON body for creating a new booking
        String bookingRequestBody = """
                {
                  "hotel_id": "1",
                  "guest_name": "John Doe",
                  "check_in_date": "2025-03-01",
                  "check_out_date": "2025-03-07",
                  "room_type": "Deluxe"
                }""";

        // Sending POST request to create a new booking (room reservation)
        Response response = given()
                .contentType(ContentType.JSON)
                .body(bookingRequestBody)
                .when()
                .post("/bookings")
                .then()
                .statusCode(201)  // Verify status code 201 (created)
                .extract()
                .response();

        // Log the full response body for debugging
        System.out.println("Response: " + response.getBody().asString());

        // Extract the booking ID from the response
        bookingId = response.jsonPath().getString("id");

        // Check if bookingId is null
        Assert.assertNotNull(bookingId, "Booking ID should not be null");
        System.out.println("Booking ID (Hotel Booking): " + bookingId);
    }


    // Test 3: Get Booking Details
    @Test(priority = 3, dependsOnMethods = "testBookHotelRoom") // Ensure booking exists before fetching details
    public void testGetBookingDetails() {
        // Sending GET request to fetch booking details using booking ID
        Response response = given()
                .pathParam("bookingId", bookingId)
                .when()
                .get("/bookings/{bookingId}")
                .then()
                .statusCode(200)  // Verify status code 200
                .contentType(ContentType.JSON)
                .extract()
                .response();

        // Validate booking details
        String guestName = response.jsonPath().getString("guest_name");
        Assert.assertEquals(guestName, "John Doe", "Guest name should match the booking details");
        System.out.println("Booking Details: " + response.prettyPrint());
    }

    // Test 4: Cancel a Booking
    @Test(priority = 4, dependsOnMethods = "testGetBookingDetails") // Ensure booking exists before attempting to cancel
    public void testCancelBooking() {
        System.out.println("Starting test: Cancel Booking");

        // Sending DELETE request to cancel the booking using booking ID
        Response response = given()
                .pathParam("bookingId", bookingId)  // Use the existing bookingId from the previous test
                .when()
                .delete("/bookings/{bookingId}")
                .then()
                .statusCode(200)  // Verify status code 200 (successful deletion)
                .extract()
                .response();

        // Print the response to check if the cancellation was successful
        System.out.println("Cancel Booking Response: " + response.prettyPrint());

        // Optionally, you could attempt to fetch booking details again to confirm it's deleted
        Response getResponseAfterCancel = given()
                .pathParam("bookingId", bookingId)
                .when()
                .get("/bookings/{bookingId}")
                .then()
                .statusCode(500)
                .extract()
                .response();

        // Ensure the booking is no longer available
        String errorMessage = getResponseAfterCancel.jsonPath().getString("error");
        //Assert.assertEquals(errorMessage, "Booking not found", "Booking still exists after cancellation");

        System.out.println("Test complete: Cancel Booking");
    }





}
