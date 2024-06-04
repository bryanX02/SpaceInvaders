package tp1.control.commands;

import tp1.control.ExecutionResult;
import tp1.control.GameModel;
import tp1.control.InitialConfiguration;
import tp1.control.exceptions.InitializationException;
import tp1.view.Messages;

public class ResetCommand extends Command{
		  		
	private InitialConfiguration conf;

	public ResetCommand(InitialConfiguration conf) {
		super();
		this.conf = conf;
	}

	@Override
    public ExecutionResult execute(GameModel game) {
        try {
            game.reset(conf);
            return new ExecutionResult(true);
        } catch (InitializationException e) {
            return new ExecutionResult(e.getMessage());
        }
    }

	@Override
	protected String getName() {
		return Messages.COMMAND_RESET_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_RESET_SHORTCUT;
	}

	@Override
	protected String getDetails() {
		return Messages.COMMAND_RESET_DETAILS;
	}

	@Override
	protected String getHelp() {
		return Messages.COMMAND_RESET_HELP;
	}

	@Override
	public Command parse(String[] commandWords) {
		Command command = null;
		if (this.matchCommandName(commandWords[0])) {
			command = this;
			if (commandWords.length > 1)
				command = new ResetCommand(InitialConfiguration.valueOfIgnoreCase(commandWords[1]));
		}
			return command;
	}
	
}
