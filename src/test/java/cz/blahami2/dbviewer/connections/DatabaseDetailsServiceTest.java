package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.data.DatabaseConnection;
import cz.blahami2.dbviewer.model.*;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseDetailsServiceTest {

    private static final String SCHEMA_NAME = "schema123";
    private static final String TABLE_NAME = "table123";

    @Mock
    private Function<Connection, DatabaseConnection> databaseConnectionFactory;
    @Mock
    private DatabaseConnection databaseConnection;
    @Mock
    private Connection connection;
    @Mock
    private ResultSet resultSet;

    private DatabaseDetailsService service;

    @BeforeEach
    void setUp() {
        given(databaseConnectionFactory.apply(connection)).willReturn(databaseConnection);
        this.service = new DatabaseDetailsService(databaseConnectionFactory, 5);
    }

    @Test
    void getSchemasReturnsSchemasInDatabase() throws SQLException {
        // given
        var expected = mock(List.class);
        var lambdaCaptor = ArgumentCaptor.forClass(DatabaseConnection.SqlFunction.class);
        // - NOTE: SQL correctness tested via IT test
        given(databaseConnection.getList(lambdaCaptor.capture(), anyString())).willReturn(expected);
        // when
        var actual = service.getSchemas(connection);
        // then
        assertSame(expected, actual);
        // - verify correct lambda function
        DatabaseConnection.SqlFunction<ResultSet, Schema> sqlFunction = lambdaCaptor.getValue();
        given(resultSet.getString("nspname")).willReturn(SCHEMA_NAME);
        Schema schema = sqlFunction.apply(resultSet);
        assertEquals(SCHEMA_NAME, schema.getName());
    }

    @Test
    void getTablesReturnsTablesForGivenConnectionAndSchema() throws SQLException {
        // given
        var expected = mock(List.class);
        var lambdaCaptor = ArgumentCaptor.forClass(DatabaseConnection.SqlFunction.class);
        // - NOTE: SQL correctness tested via IT test
        given(databaseConnection.getList(lambdaCaptor.capture(), anyString(), eq(SCHEMA_NAME))).willReturn(expected);
        // when
        var actual = service.getTables(connection, SCHEMA_NAME);
        // then
        assertSame(expected, actual);
        // - verify correct lambda function
        DatabaseConnection.SqlFunction<ResultSet, Table> sqlFunction = lambdaCaptor.getValue();
        willReturn(TABLE_NAME).given(resultSet).getString("tablename");
        willReturn("owner123").given(resultSet).getString("tableowner");
        Table table = sqlFunction.apply(resultSet);
        assertEquals(TABLE_NAME, table.getName());
        assertEquals("owner123", table.getOwner());
    }

    @Test
    void getColumnsReturnsColumnsForGivenConnectionAndSchemaAndTable() throws SQLException {
        // given
        var expected = mock(List.class);
        var lambdaCaptor = ArgumentCaptor.forClass(DatabaseConnection.SqlFunction.class);
        // - NOTE: SQL correctness tested via IT test
        given(databaseConnection.getList(lambdaCaptor.capture(), anyString(), eq(SCHEMA_NAME), eq(TABLE_NAME))).willReturn(expected);
        // when
        var actual = service.getColumns(connection, SCHEMA_NAME, TABLE_NAME);
        // then
        assertSame(expected, actual);
        // - verify correct lambda function
        DatabaseConnection.SqlFunction<ResultSet, Column> sqlFunction = lambdaCaptor.getValue();
        willReturn("name123").given(resultSet).getString("column_name");
        willReturn("type123").given(resultSet).getString("data_type");
        Column column = sqlFunction.apply(resultSet);
        assertEquals("name123", column.getName());
        assertEquals("type123", column.getType());
    }

    @Test
    void getPreviewReturnsPreviewForGivenConnectionAndSchemaAndTable() throws SQLException {
        // given
        var columns = Arrays.asList(mockColumn("col1"), mockColumn("col2"));
        service = spy(service);
        willReturn(columns).given(service).getColumns(connection, SCHEMA_NAME, TABLE_NAME);
        var list = mock(List.class);
        var lambdaCaptor = ArgumentCaptor.forClass(DatabaseConnection.SqlFunction.class);
        // - NOTE: SQL correctness tested via IT test
        given(databaseConnection.getList(lambdaCaptor.capture(), contains(SCHEMA_NAME + "." + TABLE_NAME))).willReturn(list);
        var expected = new Preview(Arrays.asList("col1","col2"), list);
        // when
        var actual = service.getPreview(connection, SCHEMA_NAME, TABLE_NAME);
        // then
        assertEquals(expected, actual);
        // - verify correct lambda function
        DatabaseConnection.SqlFunction<ResultSet, List<String>> sqlFunction = lambdaCaptor.getValue();
        willReturn("value1").given(resultSet).getString(1);
        willReturn("value2").given(resultSet).getString(2);
        List<String> values = sqlFunction.apply(resultSet);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    private Column mockColumn(String name){
        return new Column(name, "type123"); // class is final
    }
}