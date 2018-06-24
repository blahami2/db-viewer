package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.data.repository.ConnectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/connections", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConnectionsApi {
    @Autowired
    private ConnectionRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Connection>> getAllConnections() {
        List<Connection> connections = repository.findAll();
        log.debug("requested connections: {}", connections);
        return ResponseEntity.ok(connections);
    }
}
