package cz.blahami2.dbviewer.shared.testapi;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.specification.RequestSpecification;

public class JsonRestApi {

    public RequestSpecification given() {
        return RestAssured.given();
    }

    public RequestSpecification givenJsonHeaders() {
        return given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .log().all();
    }
}
