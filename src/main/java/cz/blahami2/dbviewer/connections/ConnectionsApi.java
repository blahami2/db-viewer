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
        log.debug("requested connections: {}", connections);
        return ResponseEntity.ok(connections);
    }

    @PostMapping
    public ResponseEntity<Connection> addConnection(@RequestBody Connection connection) {
        log.debug("added connection: {}", connection);
        Connection savedConnection = repository.save(connection);
        URI location = getNewResourceLocation(savedConnection.getId());
        return ResponseEntity.created(location).body(savedConnection);
    }

    private URI getNewResourceLocation(Object id){
        return  ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
