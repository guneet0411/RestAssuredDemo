package com.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Regres {

	// First Scenario: REGISTER - SUCCESSFUL
	@Test
	public void registerSuccessful() {
		RestAssured.baseURI = "https://reqres.in";

		// Request Body
		Map<String, String> params = new HashMap<String, String>();
		params.put("email", "eve.holt@reqres.in");
		params.put("password", "pistol");

		Response response = given()
				.body(params)
				.contentType("application/json")
				.post("/api/register");

		String body = response.getBody().asString();
		int status = response.getStatusCode();
		System.out.println("registerSuccessful Response Body:" + body);
		Assert.assertEquals(status, 200);
	}

	// Second Scenario: LOGIN - SUCCESSFUL
	@Test
	public void loginSuccessful() {
		RestAssured.baseURI = "https://reqres.in";

		// Request Body
		Map<String, String> params = new HashMap<String, String>();
		params.put("email", "eve.holt@reqres.in");
		params.put("password", "cityslicka");

		Response response = given()
				.body(params)
				.contentType("application/json")
				.post("/api/login");

		String body = response.getBody().asString();
		int status = response.getStatusCode();
		System.out.println("loginSuccessful Response Body" + body);
		Assert.assertEquals(status, 200);
	}

	// Third Scenario: LIST <RESOURCE>
	@Test
	public void listResource() {
		RestAssured.baseURI = "https://reqres.in";

		Response response = given()
				.header("Accept", ContentType.JSON.getAcceptHeader())
				.when()
				.get("/api/unknown");

		String body = response.getBody().asString();
		int status = response.getStatusCode();
		System.out.println("listResource Response Body" + body);
		Assert.assertEquals(status, 200);

		// Assertion of the first Id
		JsonPath jp = response.jsonPath();
		String ids = jp.getString("data.id");
		assertTrue(ids.contains("1"));

		// Assertion of the first Resource
		List<Map<String, String>> resources = JsonPath.from(body).get("data");
		Assert.assertEquals(6, resources.size());
		System.out.println("First Resource " + resources.get(0));
		System.out.println("All values of First Resourse" + resources.get(0).values());

		HashMap<String, String> expectedFirstResource = new HashMap<String, String>();
		// Adding elements to HashMap
		expectedFirstResource.put("id", "1");
		expectedFirstResource.put("name", "cerulean");
		expectedFirstResource.put("year", "2000");
		expectedFirstResource.put("color", "#98B2D1");
		expectedFirstResource.put("pantone_value", "15-4020");

		assertTrue(expectedFirstResource.keySet().equals(resources.get(0).keySet()));

	}

	// Another way of validating Id
	@Test
	public void listResourceValidateIds() {

		RestAssured.baseURI = "https://reqres.in";

		given()
		.when()
		.get("/api/unknown")
		.then()
		.body("data.id", hasItems(1, 2, 3, 4, 5, 6));

	}

}
