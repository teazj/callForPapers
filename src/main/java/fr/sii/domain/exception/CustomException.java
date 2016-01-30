package fr.sii.domain.exception;

/**
 * Created by tmaugin on 13/05/2015.
 */
public class CustomException extends Exception {
    public CustomException() {
    }

    //Constructor that accepts a message
    public CustomException(String message) {
        super(message);
    }
}
