package cz.blahami2.dbviewer.connections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.data.repository.ConnectionsRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

// TODO non-happy scenarios
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConnectionsApiIT {

    @Value("${local.server.port}")
    private int serverPort;
    @Autowired
    private ConnectionsRepository repository;

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
        repository.deleteAll();
        savedConnections = CONNECTIONS.stream().map(connection -> repository.save(connection)).collect(Collectors.toList());
    }

    @After
    public void tearDown() {
        repository.deleteAll();
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

    @Test
    public void addsConnectionAndReturnsResourceOnPost() throws Exception {
        // given
        // - prepare connection
        ConnectionBuilder builder = new ConnectionBuilder().setName("connection3");
        Connection connection = builder.build();
        // when
        Response response = api.addConnection(connection);
        // then
        response.then().statusCode(201);
        String actual = response.print();
        // - prepare expected connection (received as response) with ID
        Connection expectedConnection = builder.build();
        String location = response.header("Location");
        long id = Long.parseLong(location.replaceAll("^.*/", ""));
        expectedConnection.setId(id);
        String expected = objectMapper.writeValueAsString(expectedConnection);
        // - assert match
        JSONAssert.assertEquals(expected, actual, false);
        // - assert resource persisted
        assertThat(repository.findById(id)).contains(expectedConnection);
    }

    @Test
    public void updatesConnectionAndReturnsResourceOnPut() throws Exception {
        // given
        final String newDbName = "completelyNewDatabaseName123";
        Connection connection = savedConnections.get(0);
        connection.setDatabaseName(newDbName);
        // when
        Response response = api.updateConnection(connection);
        // then
        response.then().statusCode(200);
        String actual = response.print();
        // - prepare expected connection
        String expected = objectMapper.writeValueAsString(connection);
        // - assert match
        JSONAssert.assertEquals(expected, actual, false);
        // - assert resource persisted
        assertThat(repository.findById(connection.getId())).contains(connection);
    }

    @Test
    public void deletedConnectionAndReturnsResourceOnDelete() throws Exception {
        // given
        Connection connection = savedConnections.get(0);
        // when
        Response response = api.deleteConnection(connection.getId());
        // then
        response.then().statusCode(200);
        String actual = response.print();
        // - prepare expected connection
        String expected = objectMapper.writeValueAsString(connection);
        // - assert match
        JSONAssert.assertEquals(expected, actual, false);
        // - assert resource deleted
        assertThat(repository.findById(connection.getId())).isEmpty();
    }

    private static class ConnectionBuilder {
        private String name = "connection123";
        private String hostName = "hostName123";
        private String databaseName = "database123";
        private String userName = "userName123";
        private String password = "password123";

        public ConnectionBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Connection build(){
            return new Connection(name, hostName, databaseName, userName, password);
        }
    }
}
