package cz.blahami2.dbviewer.connections.mapping;

import cz.blahami2.dbviewer.data.entity.ConnectionEntity;
import cz.blahami2.dbviewer.model.Connection;

public class ConnectionMapper {

    public Connection toConnection(ConnectionEntity connectionEntity) {
        return new Connection(
                connectionEntity.getId(),
                connectionEntity.getName(),
                connectionEntity.getHostName(),
                connectionEntity.getDatabaseName(),
                connectionEntity.getUserName(),
                connectionEntity.getPassword()
        );
    }

    public ConnectionEntity toConnectionEntity(Connection connection) {
        return new ConnectionEntity(
                connection.getName(),
                connection.getHostName(),
                connection.getDatabaseName(),
                connection.getUserName(),
                connection.getPassword()
        );
    }
}
