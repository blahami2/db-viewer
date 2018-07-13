package cz.blahami2.dbviewer.connections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import cz.blahami2.dbviewer.data.Schema;
import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.data.repository.ConnectionsRepository;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.domzal.junit.docker.rule.DockerRule;

// TODO non-happy scenarios
@ExtendWith(SpringExtension.class)
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
    private DockerRule database = DockerRule.builder()
            .imageName("postgres") // TODO test with fixed version
            .expose("25432", "5432")
            .env("POSTGRES_DB", DB_NAME)
            .env("POSTGRES_USER", DB_USER)
            .env("POSTGRES_PASSWORD", DB_PASS)
            .build();

    private ConnectionsDetailsApiWrapper api;
    private Connection connection;
    private ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setUp() {
        RestAssured.port = serverPort;
        this.api = new ConnectionsDetailsApiWrapper();
        repository.deleteAll();
        connection = repository.save(new Connection("connection1", "localhost:25432", DB_NAME, DB_USER, DB_PASS));
    }

    @Test
    void printName() throws Exception {
        // given
        String expected = objectMapper.writeValueAsString(new Schema("public", "postgres", ""));
        // when
        Response response = api.getSchemas(connection.getId());
        // then
        String actual = response.print();
        response.then().statusCode(HttpStatus.OK.value());
        JSONAssert.assertEquals(expected, actual, false);
    }

}
