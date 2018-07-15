package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.model.Column;
import cz.blahami2.dbviewer.model.Schema;
import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.model.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

// TODO add validation service
@Slf4j
@RestController
@RequestMapping(path = "/connections", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConnectionsApi {

    private final ConnectionsService connectionsService;
    private final DatabaseDetailsService databaseDetailsService;

    @Autowired
    public ConnectionsApi(ConnectionsService connectionsService, DatabaseDetailsService databaseDetailsService) {
        this.connectionsService = connectionsService;
        this.databaseDetailsService = databaseDetailsService;
    }

    @GetMapping
    public ResponseEntity<List<Connection>> getAllConnections() {
        log.debug("requesting all connections");
        List<Connection> connections = connectionsService.getAll();
        return ResponseEntity.ok(connections);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Connection> getConnection(@PathVariable("id") Long id) {
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

    @GetMapping(path = "/{id}/schema")
    public ResponseEntity<List<Schema>> getSchemas(@PathVariable("id") Long id) throws SQLException {
        log.debug("getting schemas: {}", id);
        Connection connection = connectionsService.get(id);
        List<Schema> schemas = databaseDetailsService.getSchemas(connection);
        return ResponseEntity.ok(schemas);
    }

    @GetMapping(path = "/{id}/schema/{schemaName}/table")
    public ResponseEntity<List<Table>> getTables(@PathVariable("id") Long id, @PathVariable("schemaName") String schemaName) throws SQLException {
        log.debug("getting table for connection {} and schema {}", id, schemaName);
        Connection connection = connectionsService.get(id);
        List<Table> tables = databaseDetailsService.getTables(connection, schemaName);
        return ResponseEntity.ok(tables);
    }

    @GetMapping(path = "/{id}/schema/{schemaName}/table/{tableName}/column")
    public ResponseEntity<List<Column>> getColumns(
            @PathVariable("id") Long id,
            @PathVariable("schemaName") String schemaName,
            @PathVariable("tableName") String tableName
    ) throws SQLException {
        log.debug("getting columns for connection {}, schema {} and table {}", id, schemaName, tableName);
        Connection connection = connectionsService.get(id);
        List<Column> columns = databaseDetailsService.getColumns(connection, schemaName, tableName);
        return ResponseEntity.ok(columns);
    }

    private URI getNewResourceLocation(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }
}
