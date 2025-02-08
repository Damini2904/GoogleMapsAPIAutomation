package api.endpoints;

import static io.restassured.RestAssured.given;


import org.testng.annotations.Test;

import api.payload.GoogleMaps;
import api.payload.UpdatePlacePojo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class GoogleMapsEndpoints {
	
	//Add Place
	@Test(priority=1)
	public static Response addPlace(GoogleMaps Payload) {
		
		Response response = given()
			.contentType(ContentType.JSON)
			.queryParam("key", "qaclick123")
			.body(Payload)
			
		.when()
			.post(Urls.post_url);
		
		return response;
	}
	
	//Get Place
	@Test(priority=2)
	public static Response getPlace(String PlaceId) {
		
		Response response = given()
			.queryParam("key" , "qaclick123")
			.queryParam("place_id", PlaceId)
			
		.when()	
			.get(Urls.get_url);
		
		return response;
	}
	
	//Update Place
	@Test(priority=3)
	public static Response updatePlace(UpdatePlacePojo Payload) {
		
		
		Response response = given()
			.header("Content-Type" , "application/json")
			.queryParam("key" , "qaclick123")
			.body(Payload)
			
		.when()
			.put(Urls.put_url);
		
		return response;
	}
	
	//Delete Place
	@Test(priority=4)
	public static Response deletePlace(String PlaceId) {
		
		String RequestBody ="{\r\n"
                + "    \"place_id\":\"" + PlaceId + "\"\r\n}";
		
		Response response = given()
				.header("Content-Type" , "application/json")
				.queryParam("key" , "qaclick123")
				.body(RequestBody)
				
			.when()
				.delete(Urls.delete_url);
			
			return response;
			
	}
}
