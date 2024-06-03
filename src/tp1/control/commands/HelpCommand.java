package tp1.control.commands;

import tp1.control.ExecutionResult;
import tp1.control.GameModel;
import tp1.view.Messages;

public class HelpCommand extends NoParamsCommand {

	@Override
	protected String getName() {
		return Messages.COMMAND_HELP_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_HELP_SHORTCUT;
	}

	@Override
	protected String getDetails() {
		return Messages.COMMAND_HELP_DETAILS;
	}

	@Override
	protected String getHelp() {
		return Messages.COMMAND_HELP_HELP;
	}

	// Ejecuta la acci�n del comando, modificando el juego.
	@Override
	public ExecutionResult execute(GameModel game) {
		System.out.println(Messages.HELP_AVAILABLE_COMMANDS);
		System.out.println(CommandGenerator.commandHelp());
		return new ExecutionResult(false);
	}

}
