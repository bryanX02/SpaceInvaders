package tp1.logic.gameobjects;

import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * 
 * Class that represents the laser fired by {@link UCMShip}
 *
 */
public class UCMLaser extends UCMWeapon{
	
	// CONSTANTES
	public static final int DAMAGE = 1;
	
	// Atributos
	private Move dir;

	// Constructor
	public UCMLaser(Position pos, GameWorld game, int life) {
		super(game, pos, life);
		dir = Move.UP;
	}
	
	// GETTERS
	public Position getPos() {
		return pos;
	}

	public int getLife() {
		return life;
	}

	public Move getDir() {
		return dir;
	}

	public GameWorld getGame() {
		return game;
	}
	
	/**
	 *  Method called when the laser disappears from the board
	 */
	public void onDelete() {
		game.enableLaser();
	}

	/**
	 *  Implements the automatic movement of the laser	
	 */
	@Override
	public void automaticMove () {
		performMovement(dir);
		if(pos.isOut())
			die();
	}

	@Override
	public void computerAction() {
		game.performAttack(this);
	}

	// Método que ejecuta el movimiento del laser
	private void performMovement(Move dir) {
		
		pos = pos.move(dir);
		
	}

	@Override
	public int getDamage() {
		return DAMAGE;
	}

	//ACTUAL ATTACK METHODS
	

	

	/**
	 * 
	 * @param other regular alien under attack by the laser
	 * @return always returns <code>true</code>
	 */
	private boolean weaponAttack(RegularAlien other) {
		return false;	
	}

	// Función booleana respecto a la vida del laser
	@Override
	public boolean isAlive() {
		return life > 0;
	}


	// RECEIVE ATTACK METHODS
	
	/**
	 * Method to implement the effect of bomb attack on a laser
	 * @param weapon the received bomb
	 * @return always returns <code>true</code>
	 */
	public boolean receiveAttack(Bomb weapon) {
		
		boolean received = false;
		
		if (weapon.getPos().equals(pos)) {
			receiveDamage(weapon.getDamage());
			received = true;
		}
		
		return received;
	}

	private void receiveDamage(int damage2) {
		die();
	}

	@Override
	public String toString() {
		return Messages.LASER_SYMBOL;
	}

	

}
