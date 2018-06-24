package cz.blahami2.dbviewer.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

// TODO map to internal immutable model if overused
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Connection extends BaseEntity {

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "host_name")
    @NotEmpty
    private String hostName;

    @Column(name = "database_name")
    @NotEmpty
    private String databaseName;

    @Column(name = "user_name")
    @NotEmpty
    private String userName;

    @Column(name = "password")
    @NotEmpty
    private String password;
}
