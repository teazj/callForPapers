package fr.sii.domain.exception;

/**
 * Created by tmaugin on 24/07/2015.
 */
public class BadRequestException  extends CustomException {
    public BadRequestException() {
        super();
    }

    //Constructor that accepts a message
    public BadRequestException(String message)
    {
        super(message);
    }
}
