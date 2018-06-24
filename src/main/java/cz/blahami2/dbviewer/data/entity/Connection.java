package cz.blahami2.dbviewer.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.validation.constraints.NotEmpty;

// TODO map to internal immutable model if overused
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Connection extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotEmpty
    private String name;

    @Column(name = "host_name", nullable = false)
    @NotEmpty
    private String hostName;

    @Column(name = "database_name", nullable = false)
    @NotEmpty
    private String databaseName;

    @Column(name = "user_name", nullable = false)
    @NotEmpty
    private String userName;

    @Column(name = "password", nullable = false)
    @NotEmpty
    private String password;
}
