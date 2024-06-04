package tp1.control.exceptions;

public class CommandExecuteException extends Exception {
	
    public CommandExecuteException(String message) {
        super(message);
    }

    public CommandExecuteException(String message, Throwable cause) {
        super(message, cause);
    }
}
