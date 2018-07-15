package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.data.DatabaseConnection;
import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.model.Schema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseDetailsServiceTest {

    @Mock
    private Function<Connection, DatabaseConnection> databaseConnectionFactory;
    @Mock
    private DatabaseConnection databaseConnection;
    @Mock
    private Connection connection;

    private DatabaseDetailsService service;

    @BeforeEach
    void setUp() {
        given(databaseConnectionFactory.apply(connection)).willReturn(databaseConnection);
        this.service = new DatabaseDetailsService(databaseConnectionFactory);
    }

    @Test
    void getSchemasReturnsSchemasInDatabase() throws SQLException {
        // given
        List<Schema> expected = mock(List.class);
        ArgumentCaptor<DatabaseConnection.SqlFunction<ResultSet, Schema>> lambdaCaptor = ArgumentCaptor.forClass(DatabaseConnection.SqlFunction.class);
        given(databaseConnection.getList(eq("select nspname from pg_catalog.pg_namespace;"), lambdaCaptor.capture()))
                .willReturn(expected);
        // when
        List<Schema> actual = service.getSchemas(connection);
        // then
        assertSame(expected, actual);
        // - verify correct lambda function
        DatabaseConnection.SqlFunction<ResultSet, Schema> sqlFunction = lambdaCaptor.getValue();
        ResultSet rs = mock(ResultSet.class);
        given(rs.getString("nspname")).willReturn("schema123");
        Schema schema = sqlFunction.apply(rs);
        assertEquals("schema123", schema.getName());
    }
}