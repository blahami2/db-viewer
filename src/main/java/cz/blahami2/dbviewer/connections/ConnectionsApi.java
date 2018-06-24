package cz.blahami2.dbviewer.connections;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "/connections", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConnectionsApi {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Connection>> getAllConnections(){
        return ResponseEntity.ok(Arrays.asList(new Connection("empty-connection")));
    }

    @Data
    @AllArgsConstructor
    private static class Connection {
        private String content;
    }
}
