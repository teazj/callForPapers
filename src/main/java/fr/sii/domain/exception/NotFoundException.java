package fr.sii.domain.exception;

/**
 * Created by tmaugin on 22/05/2015.
 */
public class NotFoundException  extends CustomException {
    public NotFoundException() {
        super();
    }

    //Constructor that accepts a message
    public NotFoundException(String message)
    {
        super(message);
    }
}
