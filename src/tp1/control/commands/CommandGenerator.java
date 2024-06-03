package tp1.control.commands;

import java.util.Arrays;
import java.util.List;

public class CommandGenerator {

	private static final List<Command> availableCommands = Arrays.asList(
		new HelpCommand(),
		new MoveCommand(),
		new ExitCommand(),
		new NoneCommand(),
		new ListCommand(),
		new ResetCommand(),
		new ShootCommand(),
		new ShockWaveCommand()
	);

	public static Command parse(String[] commandWords) {	
		
		Command command = null;
		
		for (Command c: availableCommands) {
			if (c.matchCommandName(commandWords[0])) {
				
				command = c.parse(commandWords);
				// Se parsea el comando por si tiene varios parametros
				// Si devolviese un nulo seria un comando sin parametros
				if (command == null) {
					// Se guarda el comando sin parametros
					command = c;
				}
			}
		}
		
		// Para tener en cuenta el none cuando no escriben nada
		if (command == null) {
			if (commandWords[0] == "")
				command = availableCommands.get(3);
		}
		
		return command;
	}
		
	public static String commandHelp() {
		StringBuilder commands = new StringBuilder();	
		for (Command c: availableCommands) {
			commands.append(c.getHelp());
		}
		return commands.toString();
	}

}
