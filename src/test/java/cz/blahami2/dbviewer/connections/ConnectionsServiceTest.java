package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.connections.mapping.ConnectionMapper;
import cz.blahami2.dbviewer.data.entity.ConnectionEntity;
import cz.blahami2.dbviewer.data.repository.ConnectionsRepository;
import cz.blahami2.dbviewer.model.Connection;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectionsServiceTest {

    @Mock
    private ConnectionsRepository repository;
    @Mock
    private ConnectionMapper connectionMapper;

    private ConnectionsService service;

    @BeforeEach
    void setUp() {
        service = new ConnectionsService(repository, connectionMapper);
    }

    @Test
    void getAllReturnsAllConnections() {
        // given
        var connectionEntities = Arrays.asList(mock(ConnectionEntity.class), mock(ConnectionEntity.class));
        var expected = Arrays.asList(mock(Connection.class), mock(Connection.class));
        for (int i = 0; i < connectionEntities.size(); i++) {
            willReturn(expected.get(i)).given(connectionMapper).toConnection(connectionEntities.get(i));
        }
        given(repository.findAll()).willReturn(connectionEntities);
        // when
        var actual = service.getAll();
        // then
        assertIterableEquals(expected, actual);
    }

    @Test
    void getConnectionReturnsConnectionFromRepository() {
        // given
        var id = 123L;
        var connectionEntity = mock(ConnectionEntity.class);
        var expected = mock(Connection.class);
        given(connectionMapper.toConnection(connectionEntity)).willReturn(expected);
        given(repository.findById(id)).willReturn(Optional.of(connectionEntity));
        // when
        var actual = service.get(id);
        // then
        assertSame(expected, actual);
    }

    @Test
    void addConnectionAddsConnectionToRepositoryAndReturnsNewInstanceFromRepository() {
        // given
        var newConnection = mock(Connection.class);
        var newConnectionEntity = mock(ConnectionEntity.class);
        given(connectionMapper.toConnectionEntity(newConnection)).willReturn(newConnectionEntity);
        var persistedConnectionEntity = mock(ConnectionEntity.class);
        var persistedConnection = mock(Connection.class);
        given(connectionMapper.toConnection(persistedConnectionEntity)).willReturn(persistedConnection);
        given(repository.save(newConnectionEntity)).willReturn(persistedConnectionEntity);
        // when
        var actual = service.add(newConnection);
        // then
        assertEquals(persistedConnection, actual);
    }

    @Test
    void updateConnectionPersistsExistingConnectionWithNewData() {
        // given
        var oldConnectionEntity = mock(ConnectionEntity.class);
        var updatedConnection = mock(Connection.class);
        var savedConnection = mock(Connection.class);
        given(updatedConnection.getId()).willReturn(123L);
        given(updatedConnection.getName()).willReturn("newName");
        given(updatedConnection.getHostName()).willReturn("hostName");
        given(repository.findById(123L)).willReturn(Optional.of(oldConnectionEntity));
        given(repository.save(oldConnectionEntity)).willReturn(oldConnectionEntity);
        given(connectionMapper.toConnection(oldConnectionEntity)).willReturn(savedConnection);
        // when
        var actual = service.update(updatedConnection);
        // then
        assertSame(savedConnection, actual);
        then(oldConnectionEntity).should().setName("newName");
        then(oldConnectionEntity).should().setHostName("hostName");
        then(oldConnectionEntity).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteConnectionRemovesConnectionFromRepository() {
        // given
        var id = 123L;
        var deletedConnectionEntity = mock(ConnectionEntity.class);
        var deletedConnection = mock(Connection.class);
        given(repository.findById(id)).willReturn(Optional.of(deletedConnectionEntity));
        given(connectionMapper.toConnection(deletedConnectionEntity)).willReturn(deletedConnection);
        // when
        var actual = service.delete(123L);
        // then
        assertSame(deletedConnection, actual);
        then(repository).should().deleteById(id);
    }
}
