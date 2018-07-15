package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.connections.mapping.ConnectionMapper;
import cz.blahami2.dbviewer.data.entity.ConnectionEntity;
import cz.blahami2.dbviewer.data.repository.ConnectionsRepository;
import cz.blahami2.dbviewer.model.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConnectionsService {

    private final ConnectionsRepository repository;
    private final ConnectionMapper connectionMapper;

    @Autowired
    public ConnectionsService(ConnectionsRepository repository, ConnectionMapper connectionMapper) {
        this.repository = repository;
        this.connectionMapper = connectionMapper;
    }


    public List<Connection> getAll() {
        return repository.findAll().stream().map(connectionMapper::toConnection).collect(Collectors.toList());
    }

    public Connection add(Connection newConnection) {
        ConnectionEntity connectionEntity = connectionMapper.toConnectionEntity(newConnection);
        ConnectionEntity savedConnectionEntity = repository.save(connectionEntity);
        return connectionMapper.toConnection(savedConnectionEntity);
    }

    public Connection get(long id) {
        ConnectionEntity connectionEntity = findOrError(id);
        return connectionMapper.toConnection(connectionEntity);
    }

    public Connection update(Connection updatedConnection) {
        ConnectionEntity originalConnectionEntity = findOrError(updatedConnection.getId());
        Optional.ofNullable(updatedConnection.getName()).ifPresent(originalConnectionEntity::setName);
        Optional.ofNullable(updatedConnection.getHostName()).ifPresent(originalConnectionEntity::setHostName);
        Optional.ofNullable(updatedConnection.getDatabaseName()).ifPresent(originalConnectionEntity::setDatabaseName);
        Optional.ofNullable(updatedConnection.getUserName()).ifPresent(originalConnectionEntity::setUserName);
        Optional.ofNullable(updatedConnection.getPassword()).ifPresent(originalConnectionEntity::setPassword);
        ConnectionEntity savedOriginalConnectionEntity = repository.save(originalConnectionEntity);
        return connectionMapper.toConnection(savedOriginalConnectionEntity);
    }

    public Connection delete(long id) {
        ConnectionEntity connectionEntity = findOrError(id);
        Connection connection = connectionMapper.toConnection(connectionEntity);
        repository.deleteById(id);
        return connection;
    }

    private ConnectionEntity findOrError(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Non-existing connection should have been handled during validation phase"
                ));
    }
}
