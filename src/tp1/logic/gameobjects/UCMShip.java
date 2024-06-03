package tp1.logic.gameobjects;

import tp1.control.GameModel;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

public class UCMShip extends Ship{

	// Constantes
	public static final int ARMOR = 3;
	public static final int DAMAGE = 1;
	
	// Atributos
	private int points;
	private boolean hasShockWave;
	private boolean canShoot;
	private ShockWave sw;
	
	// Constructor
	public UCMShip(GameWorld game) {
		
		super(game, new Position(4, 7), ARMOR);
		canShoot = true;
	}

	// Getters
	public Position getPos() {
		return pos;
	}
	
	public int getLife() {
		return life;
	}
	
	public GameWorld getGame() {
		return game;
	}
	
	public int getPoints() {
		return points;
	}
	
	// Método que ejucuta el movimiento pasado por parametro
	public void move(Move motion) {
		
		pos = pos.move(motion);
	}
	
	public void enableLaser() {
		
		canShoot = true;
		
	}
	
	public void shootLaser() {
		
		Position posLaser = new Position(pos.getCol(), pos.getRow());
		if (canShoot) {
			
			game.addObject(new UCMLaser (posLaser, game, 1));
			
		}
		
	}

	public void recievePoints(int p) {
		
		points += p;
		
	}

	public void die() {
		
		life = 0;
		
	}

	public boolean isAlive() {
		
		return life > 0;
		
	}
	
	public void recieveAttack(int damage2) {
		
		recieveDamage(damage2);
		
		
	}
	
	private void recieveDamage(int d) {
		
		life -= d;
		if (life == 0) {
			die();
		}
		
	}
	
	public void enableShockWave() {
		hasShockWave = true;
	}
	
	public void disableShockWave() {
		hasShockWave = false;
	}

	public ShockWave executeShockWave() {
	
		ShockWave sw = null;
	
		if (hasShockWave())
			sw = new ShockWave(pos, life, game);
		
		return sw;
		
	}

	@Override
	public String toString() {
		return Messages.UCMSHIP_SYMBOL;
	}

	public boolean hasShockWave() {
		return hasShockWave;
	}
	
	
	
}
