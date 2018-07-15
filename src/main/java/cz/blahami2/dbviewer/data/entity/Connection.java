package cz.blahami2.dbviewer.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

// TODO split host and port
// TODO map to internal immutable model if overused
// TODO finish validations
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Connection extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotEmpty
    @Size(min = 1, max = 256)
    private String name;

    @Column(name = "host_name", nullable = false)
    @NotEmpty
    @Size(min = 1, max = 256)
    private String hostName;

    @Column(name = "database_name", nullable = false)
    @NotEmpty
    @Size(min = 1, max = 128)
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "must contain only alpha-numeric characters, under-scores or hyphens")
    private String databaseName;

    @Column(name = "user_name", nullable = false)
    @NotEmpty
    @Size(min = 1, max = 128)
    private String userName;

    @Column(name = "password", nullable = false)
    @NotEmpty
    @Size(min = 6, max = 128)
    private String password;
}
