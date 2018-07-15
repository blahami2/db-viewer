package cz.blahami2.dbviewer.connections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import cz.blahami2.dbviewer.data.entity.ConnectionEntity;
import cz.blahami2.dbviewer.model.Column;
import cz.blahami2.dbviewer.model.Preview;
import cz.blahami2.dbviewer.model.Schema;
import cz.blahami2.dbviewer.data.repository.ConnectionsRepository;
import cz.blahami2.dbviewer.model.Table;
import lombok.var;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.domzal.junit.docker.rule.DockerRule;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

// TODO wrap DockerRule and migrate to JUnit5
// TODO non-happy scenarios
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConnectionsDetailsApiIT {

    public static final String DB_NAME = "test-db";
    public static final String DB_USER = "postgres";
    public static final String DB_PASS = "postgres";


    @Value("${local.server.port}")
    private int serverPort;

    @Autowired
    private ConnectionsRepository repository;

    @Rule
    public DockerRule database = DockerRule.builder()
            .imageName("postgres") // TODO test with fixed version
            .env("POSTGRES_DB", DB_NAME)
            .env("POSTGRES_USER", DB_USER)
            .env("POSTGRES_PASSWORD", DB_PASS)
            .build();

    private ConnectionsDetailsApiWrapper api;
    private ConnectionEntity connection;
    private ObjectMapper objectMapper = new ObjectMapper();


    @Before
    public void setUp() throws TimeoutException {
        RestAssured.port = serverPort;
        this.api = new ConnectionsDetailsApiWrapper();
        repository.deleteAll();
        database.waitForLogMessage("/usr/local/bin/docker-entrypoint.sh:", 5_000);
        connection = repository.save(new ConnectionEntity("connection1",
                database.getDockerHost() + ":" + database.getExposedContainerPort("5432"), DB_NAME, DB_USER, DB_PASS
        ));
    }

    @Test
    public void getSchemasReturnsSchemas() throws Exception {
        // given
        var expected = objectMapper.writeValueAsString(
                new Schema[]{
                        new Schema("pg_toast"),
                        new Schema("pg_temp_1"),
                        new Schema("pg_toast_temp_1"),
                        new Schema("pg_catalog"),
                        new Schema("public"),
                        new Schema("information_schema")
                });
        // when
        var response = api.getSchemas(connection.getId());
        // then
        var actual = response.print();
        response.then().statusCode(HttpStatus.OK.value());
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getTablesWillReturnTablesForSchema() throws Exception {
        // given
        var expected = objectMapper.writeValueAsString(
                new Table[]{
                        new Table("sql_parts", "postgres"),
                        new Table("sql_languages", "postgres"),
                        new Table("sql_features", "postgres"),
                        new Table("sql_implementation_info", "postgres"),
                        new Table("sql_packages", "postgres"),
                        new Table("sql_sizing", "postgres"),
                        new Table("sql_sizing_profiles", "postgres")
                }
        );
        // when
        var response = api.getTables(connection.getId(), "information_schema");
        // then
        var actual = response.print();
        response.then().statusCode(HttpStatus.OK.value());
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getColumnsWillReturnColumnsForTable() throws Exception {
        // given
        var expected = objectMapper.writeValueAsString(
                new Column[]{
                        new Column("name", "text"),
                        new Column("setting", "text")
                }
        );
        // when
        var response = api.getColumns(connection.getId(), "pg_catalog", "pg_config");
        // then
        var actual = response.print();
        response.then().statusCode(HttpStatus.OK.value());
        JSONAssert.assertEquals(expected, actual, false);
    }

    @Test
    public void getPreviewWillReturnDataPreviewForTable() throws Exception {
        // given
        var expected = objectMapper.writeValueAsString(
                new Preview(
                        Arrays.asList("spcname", "spcowner", "spcacl", "spcoptions"),
                        Arrays.asList(
                                Arrays.asList("pg_default", "10", null, null),
                                Arrays.asList("pg_global", "10", null, null)
                        )
                )
        );
        // when
        var response = api.getPreview(connection.getId(), "pg_catalog", "pg_tablespace");
        // then
        var actual = response.print();
        response.then().statusCode(HttpStatus.OK.value());
        JSONAssert.assertEquals(expected, actual, false);
    }
}
