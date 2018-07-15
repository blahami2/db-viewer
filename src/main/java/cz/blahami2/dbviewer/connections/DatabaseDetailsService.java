package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.data.DatabaseConnection;
import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.model.Column;
import cz.blahami2.dbviewer.model.Schema;
import cz.blahami2.dbviewer.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

@Service
public class DatabaseDetailsService {

    private static final String DRIVER = "postgresql";

    private final Function<Connection, DatabaseConnection> postgresDatabaseConnectionFactory;

    @Autowired
    public DatabaseDetailsService(@Qualifier("factory.database.connection.postgres") Function<Connection, DatabaseConnection> postgresDatabaseConnectionFactory) {
        this.postgresDatabaseConnectionFactory = postgresDatabaseConnectionFactory;
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

}
