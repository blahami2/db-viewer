package cz.blahami2.dbviewer.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

// TODO handle more exceptions
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(new ApiError(
                "Validation has failed",
                ex.getConstraintViolations().stream()
                        .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                        .collect(Collectors.toList())
        ));
    }

    @ResponseStatus(
            value = HttpStatus.INTERNAL_SERVER_ERROR,
            reason = "Unknown error has occurred, please contact support at fake@fake.com"
    )
    @ExceptionHandler(Exception.class)
    public void handleGeneralException(Exception ex) {
        log.error("Uncaught exception", ex);
    }
}
