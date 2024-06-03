package tp1.control.commands;

import java.util.Arrays;
import java.util.List;

public class CommandGenerator {

	private static final List<Command> AVAILABLE_COMMANDS = Arrays.asList(
		new HelpCommand(),
		new MoveCommand(),
		new ExitCommand(),
		new UpdateCommand(),
		new ListCommand(),
		new ResetCommand(),
		new ShootCommand(),
		new ShockWaveCommand()
	);

	public static Command parse(String[] commandWords) {	
		
		Command command = null;
		int i = 0;
		
		while (i < AVAILABLE_COMMANDS.size() && command == null) {
			
			// Primero se instancia al comando que comprobaremos
			command = AVAILABLE_COMMANDS.get(i++);
			
			// Y ahora se emplea su parse para avergiar si realmente corresponde al de los paremetros
			// A su vez, este comando estará instanciado correctamente o volverá a ser null si no era el comando buscado
			command = command.parse(commandWords);
			
		}
		
		// Para tener en cuenta el none cuando no escriben nada
		if (command == null) {
			if (commandWords[0] == "")
				command = AVAILABLE_COMMANDS.get(3);
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
