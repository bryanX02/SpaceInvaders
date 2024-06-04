package tp1.control.exceptions;

public class NotAllowedMoveException extends GameModelException {
    public NotAllowedMoveException(String message) {
        super(message);
    }
}