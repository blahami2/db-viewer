package cz.blahami2.dbviewer.connections;

import com.jayway.restassured.response.Response;
import cz.blahami2.dbviewer.shared.testapi.JsonRestApi;

public class ConnectionsApiWrapper {

    private static final String BASE_PATH = "/connections";

    private final JsonRestApi api;

    public ConnectionsApiWrapper() {
        this.api = new JsonRestApi();
    }

    public Response getAll(){
        return api.givenJsonHeaders()
                .get(BASE_PATH);
    }
}
