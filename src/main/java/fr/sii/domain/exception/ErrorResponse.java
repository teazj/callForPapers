package fr.sii.domain.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

/**
 * Created by tmaugin on 09/04/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private Date timestamp;
    private Integer status;
    private String error;
    private String exception;
    private String message;

    public ErrorResponse(Exception e) {
        this.exception = e.getClass().toString();
        this.message = e.toString();
        this.timestamp = new Date();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
