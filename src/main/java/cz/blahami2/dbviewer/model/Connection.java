package cz.blahami2.dbviewer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Connection {

    private Long id;

    @NotEmpty
    @Size(min = 1, max = 256)
    private String name;

    @NotEmpty
    @Size(min = 1, max = 256)
    private String hostName;

    @NotEmpty
    @Size(min = 1, max = 128)
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "must contain only alpha-numeric characters, under-scores or hyphens")
    private String databaseName;

    @NotEmpty
    @Size(min = 1, max = 128)
    private String userName;

    @NotEmpty
    @Size(min = 6, max = 128)
    private String password;
}
