package cz.blahami2.dbviewer.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

// TODO split host and port
// TODO map to internal immutable model if overused
// TODO finish validations
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class ConnectionEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "host_name", nullable = false)
    private String hostName;

    @Column(name = "database_name", nullable = false)
    private String databaseName;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;
}
