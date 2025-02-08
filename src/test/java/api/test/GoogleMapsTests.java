package api.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.GoogleMapsEndpoints;
import api.payload.GoogleMaps;
import api.payload.Location;
import api.payload.UpdatePlacePojo;
import io.restassured.response.Response;

public class GoogleMapsTests {
	
	String place_id;
	
	GoogleMaps data;
	Faker faker;
	Location location;
	
	@BeforeClass()
	public void setupTestData() {
		
		//Initialize Faker and objects
		faker = new Faker();
		data = new GoogleMaps();
		location = new Location();
		
		//Set location details
		location.setLat(faker.address().latitude());
		location.setLng(faker.address().longitude());
		
		//Set types details
		List<String> types = new ArrayList<String>();
		types.add(faker.options().option("Shop", "PVR", "Mall", "Cafe", "Supermarket"));
		
		// Set Google Maps data
		data.setLocation(location); // Assign location
		data.setAccuracy(faker.number().numberBetween(10, 90));
		data.setName(faker.address().streetAddress());
		data.setPhone_number(faker.phoneNumber().cellPhone());
		data.setAddress(faker.address().fullAddress());
		data.setTypes(types); // Assign types
		data.setWebsite(faker.internet().url());
		data.setLanguage(faker.options().option("English", "Spanish", "French", "German","Russian", "Portuguese", "Japanese"));
		
	}

	
		//Add Place
		@Test(priority=1)
		public void testAddPlace() {
			
		// Send add request		
		Response response = GoogleMapsEndpoints.addPlace(data);
		response.then().log().body();
		
		// Validate the status code
		Assert.assertEquals(response.getStatusCode(), 200, "Failed to add place");
		
		// Extract place_id from the response
		place_id = response.jsonPath().get("place_id");
		Assert.assertNotNull(place_id, "Place ID is null after adding place!" );
		System.out.println("Place ID created: " + place_id);
		
		}
		
		//Get Place
		@Test(priority=2,dependsOnMethods = "testAddPlace")
		public void testGetPlace() {
			
		// Send get request	
		Response response =GoogleMapsEndpoints.getPlace(place_id);
		response.then().log().body();
		
		// Validate the status code
		Assert.assertEquals(response.getStatusCode(), 200,"Failed to get place details");
		
		
		}
		
		
		//Update Place
		 @Test(priority = 3, dependsOnMethods = "testGetPlace")
		public void testUpdatePlace() {
		
	    // Setup updated data
		UpdatePlacePojo updatedata = new UpdatePlacePojo();
		
		String NewAddress = faker.address().fullAddress();

		updatedata.setPlace_id(place_id);
		updatedata.setAddress(NewAddress);
		updatedata.setKey("qaclick123");
		
		// Send update request
		Response response = GoogleMapsEndpoints.updatePlace(updatedata); 
		response.then().log().body();
		
		// Validate the status code
		Assert.assertEquals(response.getStatusCode(), 200,"Failed to update place");
		
		// Send get request	
		Response getResponse  =GoogleMapsEndpoints.getPlace(place_id);
		getResponse.then().log().body();
		
		// Extract address from the response
		String UpdatedAddress = getResponse.jsonPath().get("address");
	
		// Validate that the address is updated correctly
		Assert.assertEquals(UpdatedAddress,NewAddress,"Address did not update as expected!" );
		
	}
	
		//Delete Place
		 @Test(priority = 4, dependsOnMethods = "testUpdatePlace")
		public void testDeletePlace() {
				
		// Send delete request	
		Response response = GoogleMapsEndpoints.deletePlace(place_id);
		response.then().log().body();
		
		// Validate the status code
		Assert.assertEquals(response.getStatusCode(),200,"Failed to delete place");
		
			}
	
		
}
