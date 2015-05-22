package fr.sii.config;

import fr.sii.domain.exception.ForbiddenException;
import fr.sii.domain.exception.NotFoundException;
import fr.sii.domain.exception.NotVerifiedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * Created by tmaugin on 09/04/2015.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        ErrorResponse resp = new ErrorResponse(e);
        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        resp.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<Object>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotVerifiedException.class)
    public ResponseEntity<Object> handleException(NotVerifiedException e) {
        ErrorResponse resp = new ErrorResponse(e);
        resp.setStatus(HttpStatus.FORBIDDEN.value());
        resp.setError(HttpStatus.FORBIDDEN.getReasonPhrase());
        resp.setMessage(e.getMessage());
        return new ResponseEntity<Object>(resp, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleException(NotFoundException e) {
        ErrorResponse resp = new ErrorResponse(e);
        resp.setStatus(HttpStatus.NOT_FOUND.value());
        resp.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        resp.setMessage(e.getMessage());
        return new ResponseEntity<Object>(resp, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleException(ForbiddenException e) {
        ErrorResponse resp = new ErrorResponse(e);
        resp.setStatus(HttpStatus.FORBIDDEN.value());
        resp.setError(HttpStatus.FORBIDDEN.getReasonPhrase());
        resp.setMessage(e.getMessage());
        return new ResponseEntity<Object>(resp, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException e) {
        ErrorResponse resp = new ErrorResponse(e);
        resp.setStatus(HttpStatus.BAD_REQUEST.value());
        resp.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        String errorMessage = "";
        String delim = "";
        for (FieldError fieldError : errors) {
            errorMessage+= delim + fieldError.getDefaultMessage();
            delim = ", ";
        }
        resp.setMessage(errorMessage);
        return new ResponseEntity<Object>(resp, HttpStatus.BAD_REQUEST);
    }
}