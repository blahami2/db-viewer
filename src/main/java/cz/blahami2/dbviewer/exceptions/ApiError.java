package cz.blahami2.dbviewer.exceptions;

import lombok.Value;

import java.util.List;

@Value
public class ApiError {
    private String message;
    private List<String> details;
}
