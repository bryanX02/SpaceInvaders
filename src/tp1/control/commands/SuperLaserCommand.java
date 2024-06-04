package tp1.control.commands;

import tp1.control.ExecutionResult;
import tp1.control.GameModel;
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
		
		boolean result = game.shootSuperLaser();
		ExecutionResult exeResult;
		
		if (result) {
			game.update();
			exeResult = new ExecutionResult(result);
		} else
			exeResult = new ExecutionResult(Messages.SUPERLASER_ERROR);

		return exeResult;
	}

}
