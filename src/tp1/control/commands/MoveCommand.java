package tp1.control.commands;

import tp1.control.ExecutionResult;
import tp1.control.GameModel;
import tp1.control.exceptions.NotAllowedMoveException;
import tp1.control.exceptions.OffWorldException;
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
		
		ExecutionResult result = null;
		
		if (move == null) {
	        if (paramError)
	            result = new ExecutionResult(Messages.COMMAND_INCORRECT_PARAMETER_NUMBER);
	        else
	            result = new ExecutionResult(Messages.DIRECTION_ERROR + directionError);
	    } else {
	        try {
	            game.move(move);
	            result = new ExecutionResult(true);
	            game.update();
	        } catch (OffWorldException owe) {
	            result = new ExecutionResult(owe.getMessage());
	        } catch (NotAllowedMoveException name) {
	            result = new ExecutionResult(name.getMessage());
	        }
	    }
		return result;
	}

	@Override
	public Command parse(String[] commandWords) {
		
		Command comando = null;
		
		
		if (this.matchCommandName(commandWords[0])){
			
			if (commandWords.length > 1) {
				paramError = false;
				directionError = commandWords[1];
				// Se emplea un try catch para tener en cuenta un error del valueOf
				try {
		
					// Se obtiene la direcci�n del movimiento mediante Move.parse,
					// y con el se crea el comando completo
					comando = new MoveCommand(Move.parse(commandWords[1]));
	
				// Control del error que pueda dar no econtrar enum en el valueOf que usa Move.parse
				} catch (IllegalArgumentException ex) {
					directionError = commandWords[1];
					comando = this;
				}
			} else {
				paramError = true;
				comando = this;
			}
		}
		return comando;
	
		
	}

}
