package fr.sii.domain.exception;

/**
 * Created by tmaugin on 20/05/2015.
 */
public class NotVerifiedException extends CustomException {
    public NotVerifiedException() {
        super();
    }

    //Constructor that accepts a message
    public NotVerifiedException(String message)
    {
        super(message);
    }
}
