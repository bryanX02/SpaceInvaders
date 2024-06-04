package tp1.logic.gameobjects;

import tp1.logic.AlienManager;
import tp1.logic.Position;

public abstract class AlienShip extends EnemyShip {

	public AlienShip(GameWorld game, Position pos, int life) {
		super(game, pos, life);
		// TODO Auto-generated constructor stub
	}

	protected abstract AlienShip copy(GameWorld game, Position pos, AlienManager am);
	
	public int getPoints() {
		return 0;
	}


	
	// M�todo sobrecargado que recibe el ataque de un laser
	@Override
	public boolean receiveAttack(UCMWeapon weapon) {	
		receiveDamage(weapon.getDamage());
		return false;
	}


	// Metodo que recibe da�o
	private void receiveDamage(int damage) {
		
		life -= damage;
		
		// Si lleg� a 0 el alien muere
		if (life == 0) {
			die();
		}
	}


}
