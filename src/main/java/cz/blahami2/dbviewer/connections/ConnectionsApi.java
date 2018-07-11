package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.data.entity.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// TODO add validation service
@Slf4j
@RestController
@RequestMapping(path = "/connections", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConnectionsApi {

    private final ConnectionsService connectionsService;

    @Autowired
    public ConnectionsApi(ConnectionsService connectionsService) {
        this.connectionsService = connectionsService;
    }

    @GetMapping
    public ResponseEntity<List<Connection>> getAllConnections() {
        log.debug("requesting all connections");
        List<Connection> connections = connectionsService.getAll();
        return ResponseEntity.ok(connections);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Connection> getConnection(@PathVariable("id") Long id){
        log.debug("obtaining connection: {}", id);
        Connection connection = connectionsService.get(id);
        return ResponseEntity.ok(connection);
    }

    @PostMapping
    public ResponseEntity<Connection> addConnection(@RequestBody Connection connection) {
        log.debug("adding connection: {}", connection);
        Connection savedConnection = connectionsService.add(connection);
        URI location = getNewResourceLocation(savedConnection.getId());
        return ResponseEntity.created(location).body(savedConnection);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Connection> putConnection(@PathVariable("id") Long id, @RequestBody Connection connection) {
        log.debug("updating connection: {}", connection);
        Connection updatedConnection = connectionsService.update(connection);
        return ResponseEntity.ok(updatedConnection);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Connection> deleteConnection(@PathVariable("id") Long id) {
        log.debug("deleting connection: {}", id);
        Connection deletedConnection = connectionsService.delete(id);
        return ResponseEntity.ok(deletedConnection);
    }

    private URI getNewResourceLocation(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
