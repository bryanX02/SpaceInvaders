package tp1.control;

import static tp1.view.Messages.debug;

import java.util.Scanner;

import tp1.control.commands.Command;
import tp1.control.commands.CommandGenerator;
import tp1.control.exceptions.CommandExecuteException;
import tp1.control.exceptions.CommandParseException;
import tp1.logic.Game;
import tp1.view.GamePrinter;
import tp1.view.GameStatus;
import tp1.view.Messages;

/**
 *  Accepts user input and coordinates the game execution logic
 */
public class Controller {

	private GameModel game;
	private GameStatus status;
	private Scanner scanner;
	private GamePrinter printer;

	public Controller(Game game, Scanner scanner) {
		this.game = game;
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

		while (!game.isFinished()) {
			
			try {
                String[] parameters = prompt();
                Command command = CommandGenerator.parse(parameters);

                // Ejecutamos el comando y verificamos si se debe redibujar el juego
                ExecutionResult result;	
				result = command.execute(game);
				 if (result.success()) { 
		            if (result.draw()) 
		                printGame();
				 }else {
					 System.out.println(result.errorMessage());
				 }

            } catch (CommandParseException | CommandExecuteException e) {
                // Mostramos el mensaje de error al usuario
                System.out.println(e.getMessage());
                Throwable cause = e.getCause();
                if (cause != null) {
                    System.out.println(cause.getMessage());
                }
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
