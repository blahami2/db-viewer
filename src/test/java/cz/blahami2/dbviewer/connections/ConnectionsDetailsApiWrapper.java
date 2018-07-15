package cz.blahami2.dbviewer.connections;

import com.jayway.restassured.response.Response;
import cz.blahami2.dbviewer.shared.testapi.JsonRestApi;

public class ConnectionsDetailsApiWrapper {

    private static final String BASE_PATH = "/connections/";

    private final JsonRestApi api = new JsonRestApi();

    public Response getSchemas(long connectionId) {
        return api.givenJsonHeaders()
                .get(BASE_PATH + connectionId + "/schema");
    }

    public Response getTables(long connectionId, String schemaName) {
        return api.givenJsonHeaders()
                .get(BASE_PATH + connectionId + "/schema/" + schemaName + "/table");
    }

    public Response getColumns(long connectionId, String schemaName, String tableName) {
        return api.givenJsonHeaders()
                .get(BASE_PATH + connectionId + "/schema/" + schemaName + "/table/" + tableName + "/column");
    }

    public Response getPreview(long connectionId, String schemaName, String tableName) {
        return api.givenJsonHeaders()
                .get(BASE_PATH + connectionId + "/schema/" + schemaName + "/table/" + tableName + "/preview");
    }
}
