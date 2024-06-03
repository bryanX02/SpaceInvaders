package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Move;
import tp1.logic.Position;

// Clase con la que se crearan y manejaran los UFO's
public class Ufo {

	public static final int POINTS = 25;
	public static final int ARMOR = 1;
	
	// Atributos
	private Position pos;
	private boolean enabled;
	private Game game;
	private int life;
	
	// Constructor
	public Ufo(Game game, boolean enabled) {

		this.enabled = enabled;
		this.game = game;
		life = ARMOR;
	}

	public void computerAction() {
		if(!enabled && canGenerateRandomUfo()) {
			enable();
		}
	}
	
	private void enable() {
		enabled = true;
		pos = new Position(9, 0);
	}

	public void onDelete() {
		
		game.enableShockWave();
		
	}
	
	public boolean isAlive() {
		return life > 0;
	}
	
	public void automaticMove() {
		performMovement();
	}
	
	public void performMovement() {
		
		pos = pos.move(Move.LEFT);
		if (isOut()) {
			pos = null;
			enabled = false;
		}
		
	}

	public void die() {
		
		pos = null;
		
	}

	public boolean isOut () {
		return pos.getCol() < 0;
	}
	/**
	 * Checks if the game should generate an ufo.
	 * 
	 * @return <code>true</code> if an ufo should be generated.
	 */
	private boolean canGenerateRandomUfo(){
		return game.getRandom().nextDouble() < game.getLevel().getUfoFrequency();
	}

	public boolean isOnPosition(Position box) {
		return pos.equals(box);
	}

	public int getLife() {
		// TODO Auto-generated method stub
		return life;
	}

	public void receiveDamage(int damage) {
		
		receiveAttack(damage);
		
	}

	private void receiveAttack(int damage) {
		
		life -= damage;
		if (life == 0) {
			die();
		}
	}

	public Position getPosition() {
		// TODO Auto-generated method stub
		return pos;
	}
	
}
