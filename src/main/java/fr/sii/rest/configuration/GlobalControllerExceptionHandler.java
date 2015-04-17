package fr.sii.rest.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by tmaugin on 09/04/2015.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        ErrorResponse resp = new ErrorResponse(e);
        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        resp.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<Object>(resp, HttpStatus.BAD_REQUEST);
    }
}