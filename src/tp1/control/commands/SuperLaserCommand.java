package tp1.control.commands;

import tp1.control.ExecutionResult;
import tp1.control.GameModel;
import tp1.control.exceptions.NotEnoughPointsException;
import tp1.view.Messages;

public class SuperLaserCommand extends NoParamsCommand{

	@Override
	protected String getName() {
		return Messages.COMMAND_SUPERLASER_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_SUPERLASER_SHORTCUT;
	}

	@Override
	protected String getDetails() {
		return Messages.COMMAND_SUPERLASER_DETAILS;
	}

	@Override
	protected String getHelp() {
		return Messages.COMMAND_SUPERLASER_HELP;
	}

	@Override
	public ExecutionResult execute(GameModel game) {
		
		
		try {
			game.shootSuperLaser();
			game.update();
			return new ExecutionResult(true);
		} catch (NotEnoughPointsException e) {
            return new ExecutionResult(e.getMessage());
        }
	}

}
