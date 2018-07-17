package cz.blahami2.dbviewer.connections;

import com.jayway.restassured.response.Response;
import cz.blahami2.dbviewer.shared.testapi.JsonRestApi;

public class ConnectionsDetailsApiWrapper {

    private static final String BASE_PATH = "/connections/";

    private final JsonRestApi api = new JsonRestApi();

    public Response getSchemas(long connectionId) {
        return api.givenJsonHeaders()
                .get(BASE_PATH + connectionId + "/schemas");
    }

    public Response getTables(long connectionId, String schemaName) {
        return api.givenJsonHeaders()
                .get(BASE_PATH + connectionId + "/schemas/" + schemaName + "/tables");
    }

    public Response getColumns(long connectionId, String schemaName, String tableName) {
        return api.givenJsonHeaders()
                .get(BASE_PATH + connectionId + "/schemas/" + schemaName + "/tables/" + tableName + "/columns");
    }

    public Response getPreview(long connectionId, String schemaName, String tableName) {
        return api.givenJsonHeaders()
                .get(BASE_PATH + connectionId + "/schemas/" + schemaName + "/tables/" + tableName + "/preview");
    }
}
