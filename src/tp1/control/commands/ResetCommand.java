package tp1.control.commands;

import tp1.control.ExecutionResult;
import tp1.control.GameModel;
import tp1.view.Messages;

public class ResetCommand extends NoParamsCommand{
		  		
		@Override
		public ExecutionResult execute(GameModel game) {
			game.reset();
			return new ExecutionResult(true);
		}

		@Override
		protected String getName() {
			return Messages.COMMAND_EXIT_NAME;
		}

		@Override
		protected String getShortcut() {
			return Messages.COMMAND_EXIT_SHORTCUT;
		}

		@Override
		protected String getDetails() {
			return Messages.COMMAND_EXIT_DETAILS;
		}

		@Override
		protected String getHelp() {
			return Messages.COMMAND_EXIT_HELP;
		}
		
	}
