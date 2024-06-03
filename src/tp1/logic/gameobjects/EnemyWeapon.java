package tp1.logic.gameobjects;

import tp1.logic.Move;
import tp1.logic.Position;

public class EnemyWeapon extends Weapon{

	private Move dir;
	
	public EnemyWeapon(GameWorld game, Position pos, int life) {
		super(game, pos, life);
		dir = Move.DOWN;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void automaticMove() {
			
		// Se ejecuta su movimiento que siempre es el mismo
		performMovement(dir);
		game.performAttack(this);
	
	}

	// Método que ejecuta el movimiento
	private void performMovement(Move dir) {	
		
		pos = pos.move(dir);
	}

	
}
