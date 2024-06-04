package tp1.control.commands;

import tp1.control.exceptions.CommandParseException;
import tp1.view.Messages;

public abstract class NoParamsCommand extends Command {

	// Sobrecarga de la función para los NoParamCommands
	@Override
	public Command parse(String[] commandWords) throws CommandParseException {
		
		Command comando = null;
		
		if (matchCommandName(commandWords[0])) {
            if (commandWords.length == 1) {
                comando = this;
            } else {
                throw new CommandParseException(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
            }
        } 
		
		return comando;
	}
	
}
