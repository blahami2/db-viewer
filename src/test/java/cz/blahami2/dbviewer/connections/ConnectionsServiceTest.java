package cz.blahami2.dbviewer.connections;

import cz.blahami2.dbviewer.data.entity.Connection;
import cz.blahami2.dbviewer.data.repository.ConnectionsRepository;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConnectionsServiceTest {

    @Mock
    private ConnectionsRepository repository;

    private ConnectionsService service;

    @BeforeEach
    void setUp() {
        service = new ConnectionsService(repository);
    }

    @Test
    void getAllReturnsAllConnections() {
        // given
        var expected = Arrays.asList(mock(Connection.class), mock(Connection.class));
        given(repository.findAll()).willReturn(expected);
        // when
        List<Connection> actual = service.getAll();
        // then
        assertIterableEquals(expected, actual);
    }

    @Test
    void addConnectionAddsConnectionToRepositoryAndReturnsNewInstanceFromRepository() {
        // given
        Connection newConnection = mock(Connection.class);
        Connection persistedConnection = mock(Connection.class);
        given(repository.save(newConnection)).willReturn(persistedConnection);
        // when
        Connection actual = service.add(newConnection);
        // then
        assertEquals(persistedConnection, actual);
    }

    @Test
    void updateConnectionPersistsExistingConnectionWithNewData() {
        // given
        Connection oldConnection = mock(Connection.class);
        Connection updatedConnection = mock(Connection.class);
        given(updatedConnection.getId()).willReturn(123L);
        given(updatedConnection.getName()).willReturn("newName");
        given(updatedConnection.getHostName()).willReturn("hostName");
        given(repository.findById(123L)).willReturn(Optional.of(oldConnection));
        given(repository.save(oldConnection)).willReturn(oldConnection);
        // when
        Connection actual = service.update(updatedConnection);
        // then
        assertSame(oldConnection, actual);
        then(oldConnection).should().setName("newName");
        then(oldConnection).should().setHostName("hostName");
        then(oldConnection).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteConnectionRemovesConnectionFromRepository() {
        // given
        long id = 123L;
        Connection deletedConnection = mock(Connection.class);
        given(repository.findById(id)).willReturn(Optional.of(deletedConnection));
        // when
        Connection actual = service.delete(123L);
        // then
        assertSame(deletedConnection, actual);
        then(repository).should().deleteById(id);
    }
}