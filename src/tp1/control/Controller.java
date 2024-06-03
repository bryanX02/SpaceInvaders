package tp1.control;

import static tp1.view.Messages.debug;

import java.util.Scanner;

import tp1.control.commands.Command;
import tp1.control.commands.CommandGenerator;
import tp1.control.commands.ListCommand;
import tp1.logic.Game;
import tp1.logic.Move;
import tp1.view.GamePrinter;
import tp1.view.GameStatus;
import tp1.view.Messages;

/**
 *  Accepts user input and coordinates the game execution logic
 */
public class Controller {

	private GameModel model;
	private GameStatus status;
	private Scanner scanner;
	private GamePrinter printer;

	public Controller(Game game, Scanner scanner) {
		this.model = game;
		this.status = game;
		this.scanner = scanner;
		printer = new GamePrinter(status);
	}

	/**
	 * Show prompt and request command.
	 *
	 * @return the player command as words
	 */
	private String[] prompt() {
		System.out.print(Messages.PROMPT);
		String line = scanner.nextLine();
		String[] words = line.toLowerCase().trim().split("\\s+");

		System.out.println(debug(line));

		return words;
	}

	/**
	 * Runs the game logic
	 */
	public void run() {

		printGame();

		while (!model.isFinished()) {
			String[] parameters = prompt();

			Command command = CommandGenerator.parse(parameters);
			
			if (command != null) {
				ListCommand list = new ListCommand();
				ExecutionResult result;
				
				if (command.equals(list))
					result = list.execute(status);
				else
					result = command.execute(model);
				
				 if (result.success()) { 
		            if (result.draw()) 
		                printGame();
				 } else
			            System.out.println(result.errorMessage());
			
			} else {
				System.out.println(Messages.UNKNOWN_COMMAND);
			}
		}

		printEndMessage();
	}

	/**
	 * Draw / paint the game
	 */
	private void printGame() {
		System.out.println(printer);
	}
	
	/**
	 * Prints the final message once the game is finished
	 */
	public void printEndMessage() {
		System.out.println(printer.endMessage());
	}
	
}
