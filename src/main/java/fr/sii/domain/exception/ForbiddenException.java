package fr.sii.domain.exception;

/**
 * Created by tmaugin on 22/05/2015.
 */
public class ForbiddenException extends CustomException {
    public ForbiddenException() {
        super();
    }

    //Constructor that accepts a message
    public ForbiddenException(String message) {
        super(message);
    }
}
