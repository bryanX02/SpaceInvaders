package tp1.control.commands;

public abstract class NoParamsCommand extends Command {

	// Sobrecarga de la función para los NoParamCommands
	@Override
	public Command parse(String[] commandWords) {
		
		Command comando = null;
		
		if (this.matchCommandName(commandWords[0]))
			comando = this;
		
		return comando;
	}
	
}
