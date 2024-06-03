package tp1.control.commands;

import tp1.control.ExecutionResult;
import tp1.control.GameModel;
import tp1.logic.Move;
import tp1.view.Messages;

public class MoveCommand extends Command {

	private Move move;
	private boolean paramError;
	private String directionError;

	public MoveCommand() {
		paramError = false;
	}

	protected MoveCommand(Move move) {
		this.move = move;
	}

	@Override
	protected String getName() {
		return Messages.COMMAND_MOVE_NAME;
	}

	@Override
	protected String getShortcut() {
		return Messages.COMMAND_MOVE_SHORTCUT;
	}

	@Override
	protected String getDetails() {
		return Messages.COMMAND_MOVE_DETAILS;
	}

	@Override
	protected String getHelp() {
		return Messages.COMMAND_MOVE_HELP;
	}

	@Override
	public ExecutionResult execute(GameModel game) {
		
		boolean pudoMoverse;
		ExecutionResult result;
		
		if (move == null)
			if (paramError)
				result = new ExecutionResult(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
			else
				result = new ExecutionResult(Messages.DIRECTION_ERROR + directionError);
		else {
			pudoMoverse = game.move(move);
			if (pudoMoverse) {
				result = new ExecutionResult(pudoMoverse);
				game.update();
			}
			else
				result = new ExecutionResult(Messages.MOVEMENT_ERROR);
		}
		
		return result;
	}

	@Override
	public Command parse(String[] commandWords) {
		
		Command comando;
		
		if (commandWords.length != 2) {
			paramError = true;
			comando = null;
		} else {
			paramError = false;
			directionError = null;
			// Se emplea un try catch para tener en cuenta un error del valueOf
			try {
	
				// Se obtiene la direcci�n del movimiento mediante Move.parse,
				// y con el se crea el comando completo
				comando = new MoveCommand(Move.parse(commandWords[1]));

			// Control del error que pueda dar no econtrar enum en el valueOf que usa Move.parse
			} catch (IllegalArgumentException ex) {
				directionError = commandWords[1];
				comando = null;
			}
		}
		return comando;
	
		
	}

}
