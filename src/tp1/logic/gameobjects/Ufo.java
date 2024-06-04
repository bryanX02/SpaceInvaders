package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

// Clase con la que se crearan y manejaran los UFO's
public class Ufo extends EnemyShip{

	public static final int POINTS = 25;
	public static final int ARMOR = 1;
	
	// Atributos
	private boolean enabled;
	
	
	public Ufo(GameWorld game, Position pos, int life) {
		super(game, pos, life);
		enabled = false;
	}

	public void computerAction() {
		if(!enabled && canGenerateRandomUfo()) {
			enable();
		}
	}
	
	private void enable() {
		enabled = true;
		life = ARMOR;
		pos = new Position(8, 0);
	}

	public void onDelete() {
		
		enabled = false;
	}
	
	
	public void automaticMove() {
		performMovement();
	}
	
	public void performMovement() {
		
		pos = pos.move(Move.LEFT);
		
	}

	public void die() {

		life = 0;
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

        double randomValue = game.getRandom().nextDouble();
        double frequency = game.getLevel().getUfoFrequency();
		return randomValue < frequency;
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

	public void receiveAttack(int damage) {
		
		life -= damage;
		if (life == 0) {

			game.enableShockWave();
			die();
		}
	}

	@Override
	public String toString() {
		return " " + Messages.UFO_SYMBOL + "[" + String.format("%02d", life) + "]";
	}

	
	
	

	
}
