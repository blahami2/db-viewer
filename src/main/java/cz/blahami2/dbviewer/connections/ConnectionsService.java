package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.data.repository.ConnectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectionsService {

    private final ConnectionsRepository repository;

    @Autowired
    public ConnectionsService(ConnectionsRepository repository) {
        this.repository = repository;
    }

    public List<Connection> getAll() {
        return repository.findAll();
    }

    public Connection add(Connection newConnection) {
        return repository.save(newConnection);
    }

    public Connection get(long id) {
        return findOrError(id);
    }

    public Connection update(Connection updatedConnection) {
        Connection originalConnection = findOrError(updatedConnection.getId());
        Optional.ofNullable(updatedConnection.getName()).ifPresent(originalConnection::setName);
        Optional.ofNullable(updatedConnection.getHostName()).ifPresent(originalConnection::setHostName);
        Optional.ofNullable(updatedConnection.getDatabaseName()).ifPresent(originalConnection::setDatabaseName);
        Optional.ofNullable(updatedConnection.getUserName()).ifPresent(originalConnection::setUserName);
        Optional.ofNullable(updatedConnection.getPassword()).ifPresent(originalConnection::setPassword);
        return repository.save(originalConnection);
    }

    public Connection delete(long id) {
        Connection connection = findOrError(id);
        repository.deleteById(id);
        return connection;
    }

    private Connection findOrError(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "Non-existing connection should have been handled during validation phase"
                ));
    }
}
