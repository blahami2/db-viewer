package cz.blahami2.dbviewer.connections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.data.repository.ConnectionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConnectionsApiIT {

    @Value("${local.server.port}")
    private int serverPort;
    @Autowired
    private ConnectionRepository repository;

    private ConnectionsApiWrapper api;
    private static final List<Connection> CONNECTIONS = Arrays.asList(
            new Connection("connection1", "localhost", "testdb", "postgres", "postgres"),
            new Connection("connection2", "localhost", "testdb", "postgres", "postgres")
    );
    private List<Connection> savedConnections;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        RestAssured.port = serverPort;
        this.api = new ConnectionsApiWrapper();
        savedConnections = CONNECTIONS.stream().map(connection -> repository.save(connection)).collect(Collectors.toList());
    }

    @After
    public void tearDown() {
        savedConnections.forEach(connection -> repository.delete(connection));
    }

    @Test
    public void returnsListOfConnectionsOnGet() throws Exception {
        // given
        // - see #setUp
        String expected = objectMapper.writeValueAsString(savedConnections);
        // when
        Response response = api.getAll();
        // then
        response.then().statusCode(200);
        String actual = response.print();
        JSONAssert.assertEquals(expected, actual, false);
    }
}
