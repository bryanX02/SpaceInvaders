package tp1.logic.gameobjects;

import tp1.logic.Position;

public class EnemyShip extends Ship {

	public EnemyShip(GameWorld game, Position pos, int life) {
		super(game, pos, life);
	}
	
	public int getPoints() {
		return 0;
	}

}
