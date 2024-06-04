package tp1.control.exceptions;

public class CommandParseException extends Exception {
	
    public CommandParseException(String message) {
        super(message);
    }

    public CommandParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
