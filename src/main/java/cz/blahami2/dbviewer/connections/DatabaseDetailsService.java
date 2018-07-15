package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.data.DatabaseConnection;
import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.model.Column;
import cz.blahami2.dbviewer.model.Preview;
import cz.blahami2.dbviewer.model.Schema;
import cz.blahami2.dbviewer.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DatabaseDetailsService {

    private final Function<Connection, DatabaseConnection> postgresDatabaseConnectionFactory;
    private final int previewSize;

    @Autowired
    public DatabaseDetailsService(@Qualifier("factory.database.connection.postgres") Function<Connection, DatabaseConnection> postgresDatabaseConnectionFactory, @Value("${preview.size}") int previewSize) {
        this.postgresDatabaseConnectionFactory = postgresDatabaseConnectionFactory;
        this.previewSize = previewSize;
    }

    public List<Schema> getSchemas(Connection connection) throws SQLException {
        return postgresDatabaseConnectionFactory.apply(connection)
                .getList(
                        rs -> new Schema(rs.getString("nspname")),
                        "select nspname from pg_catalog.pg_namespace;"
                );
    }

    public List<Table> getTables(Connection connection, String schemaName) throws SQLException {
        return postgresDatabaseConnectionFactory.apply(connection)
                .getList(
                        rs -> new Table(rs.getString("tablename"), rs.getString("tableowner")),
                        "select tablename, tableowner from pg_catalog.pg_tables where schemaname = ?;",
                        schemaName
                );
    }

    public List<Column> getColumns(Connection connection, String schemaName, String tableName) throws SQLException {
        return postgresDatabaseConnectionFactory.apply(connection)
                .getList(
                        rs -> new Column(rs.getString("column_name"), rs.getString("data_type")),
                        "SELECT * FROM information_schema.columns WHERE table_schema = ? AND table_name = ?;",
                        schemaName, tableName
                );
    }

    public Preview getPreview(Connection connection, String schemaName, String tableName) throws SQLException {
        List<Column> columns = getColumns(connection, schemaName, tableName);
        List<List<String>> data = postgresDatabaseConnectionFactory.apply(connection)
                .getList(
                        rs -> {
                            // cannot be stream due to thrown SQLException
                            List<String> values = new ArrayList<>();
                            for (int i = 0; i < columns.size(); i++) {
                                values.add(rs.getString(i+1));
                            }
                            return values;
                        },
                        String.format("SELECT * from %s.%s LIMIT %d;", schemaName, tableName, previewSize)
                );
        return new Preview(
                columns.stream().map(column -> column.getName()).collect(Collectors.toList()),
                data
        );
    }

}
