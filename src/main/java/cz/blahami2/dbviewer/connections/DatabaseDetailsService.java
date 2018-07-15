package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.data.DatabaseConnection;
import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.model.Schema;
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
                        "select nspname from pg_catalog.pg_namespace;",
                        rs -> new Schema(rs.getString("nspname"))
                );
    }
}
