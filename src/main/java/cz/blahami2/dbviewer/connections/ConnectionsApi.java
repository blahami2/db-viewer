package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.data.repository.ConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/connections", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConnectionsApi {

    // TODO split to ConnectionsService and call repository from there, should the logic here become more complex
    @Autowired
    private ConnectionRepository repository;

    @GetMapping
    public ResponseEntity<List<Connection>> getAllConnections() {
        List<Connection> connections = repository.findAll();
        log.debug("requesting connections: {}", connections);
        return ResponseEntity.ok(connections);
    }

    @PostMapping
    public ResponseEntity<Connection> addConnection(@RequestBody Connection connection) {
        log.debug("adding connection: {}", connection);
        Connection savedConnection = repository.save(connection);
        URI location = getNewResourceLocation(savedConnection.getId());
        return ResponseEntity.created(location).body(savedConnection);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Connection> putConnection(@PathVariable("id") Long id, @RequestBody Connection connection){
        log.debug("updating connection: {}", connection);
        return repository.findById(id)
                .map(originalConnection -> {
                    Optional.ofNullable(connection.getName()).ifPresent(originalConnection::setName);
                    Optional.ofNullable(connection.getHostName()).ifPresent(originalConnection::setHostName);
                    Optional.ofNullable(connection.getDatabaseName()).ifPresent(originalConnection::setDatabaseName);
                    Optional.ofNullable(connection.getUserName()).ifPresent(originalConnection::setUserName);
                    Optional.ofNullable(connection.getPassword()).ifPresent(originalConnection::setPassword);
                    return repository.save(originalConnection);
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Connection> deleteConnection(@PathVariable("id") Long id){
        log.debug("deleting connection: {}", id);
        return repository.findById(id)
                .map(originalConnection -> {
                    repository.delete(originalConnection);
                    return ResponseEntity.ok(originalConnection);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private URI getNewResourceLocation(Object id){
        return  ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
