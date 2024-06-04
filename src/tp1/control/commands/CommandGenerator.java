package tp1.control.commands;

import java.util.Arrays;
import java.util.List;

import tp1.control.InitialConfiguration;
import tp1.control.exceptions.CommandParseException;
import tp1.view.Messages;

public class CommandGenerator {

	private static final List<Command> AVAILABLE_COMMANDS = Arrays.asList(
		new HelpCommand(),
		new MoveCommand(),
		new UpdateCommand(),
		new ShootCommand(),
		new ShockWaveCommand(),
		new ListCommand(),
		new ExitCommand(),
		new ResetCommand(InitialConfiguration.NONE),
		new SuperLaserCommand()
	);

	public static Command parse(String[] commandWords) throws CommandParseException {	
		
		Command command = null;
		int i = 0;

		// Para tener en cuenta el none cuando no escriben nada
		if (commandWords[0] == "")
			command = AVAILABLE_COMMANDS.get(2);
		else {
			
			while (i < AVAILABLE_COMMANDS.size() && command == null) {
				
				// Primero se instancia al comando que comprobaremos
				command = AVAILABLE_COMMANDS.get(i++);
				
				// Y ahora se emplea su parse para avergiar si realmente corresponde al de los paremetros
				// A su vez, este comando estará instanciado correctamente o volverá a ser null si no era el comando buscado
				command = command.parse(commandWords);
				
			}
		
		}
		if (command == null) {
            throw new CommandParseException(Messages.UNKNOWN_COMMAND);
        }
		
		return command;
	}
		
	public static String commandHelp() {
		StringBuilder commands = new StringBuilder();	
		for (Command c: AVAILABLE_COMMANDS) {
			commands.append(c.helpText());
		}
		return commands.toString();
	}

}
