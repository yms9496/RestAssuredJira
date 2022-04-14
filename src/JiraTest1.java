import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import static io.restassured.RestAssured.*;

import java.io.File;


public class JiraTest1 {

	public static void main(String[] args) {
		
		RestAssured.baseURI = "http://localhost:8086";
		
		SessionFilter session = new SessionFilter();
		
		// get session
		String Response = given().header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"username\": \"yms9496\",\r\n"
				+ "    \"password\": \"yash9496\"\r\n"
				+ "    \r\n"
				+ "}")
		.queryParam("Key", "RES")
		.when()
		.filter(session)	// will get the session
		.post("/rest/auth/1/session")
		.then().extract().response().asString();
		
		// add comment
		given().pathParam("id", "10000")
		.header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"body\": \"blah blah blah blah\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}")
		.filter(session)  // Will give the session 
		.post("/rest/api/2/issue/{id}/comment")
		.then().assertThat().statusCode(201);
		
		// upload file to issue 
		given().header("X-Atlassian-Token","nocheck")
		.header("content-Type","multipart/form-data")	// in case of file, this is the content type
		.filter(session)	// will get the session
		.pathParam("id", "10000")
		.multiPart("file", new File("./jira.txt"))  // used when file is sent
		.when()
		.post("/rest/api/2/issue/{id}/attachments")
		.then().assertThat().statusCode(200);
		
		
	}

}
