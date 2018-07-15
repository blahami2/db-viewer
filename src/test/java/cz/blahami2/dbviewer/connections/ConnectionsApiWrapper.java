package cz.blahami2.dbviewer.connections;

import com.jayway.restassured.response.Response;
import cz.blahami2.dbviewer.data.entity.ConnectionEntity;
import cz.blahami2.dbviewer.shared.testapi.JsonRestApi;

public class ConnectionsApiWrapper {

    private static final String BASE_PATH = "/connections";

    private final JsonRestApi api = new JsonRestApi();

    public Response getAll() {
        return api.givenJsonHeaders()
                .get(BASE_PATH);
    }

    public Response getConnection(long id){
        return api.givenJsonHeaders()
                .get(BASE_PATH + "/" + id);
    }

    public Response addConnection(ConnectionEntity connection) {
        return api.givenJsonHeaders()
                .body(connection)
                .post(BASE_PATH);
    }

    public Response updateConnection(ConnectionEntity connection) {
        return api.givenJsonHeaders()
                .body(connection)
                .put(BASE_PATH + "/" + connection.getId());
    }

    public Response deleteConnection(Long id){
        return api.givenJsonHeaders()
                .delete(BASE_PATH + "/" + id);
    }
}
