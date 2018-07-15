package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.data.DatabaseConnection;
import cz.blahami2.dbviewer.data.entity.Connection;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ConnectionsConfiguration {

    private static final String DRIVER = "postgresql";

    @Bean
    @Qualifier("factory.database.connection.postgres")
    public Function<Connection, DatabaseConnection> getPostgresDatabaseConnectionFactory() {
        return connection -> new DatabaseConnection(
                DRIVER,
                connection.getHostName(),
                connection.getUserName(),
                connection.getPassword(),
                connection.getDatabaseName()
        );
    }
}
