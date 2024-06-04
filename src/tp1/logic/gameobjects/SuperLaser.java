package tp1.logic.gameobjects;

import tp1.logic.Position;
import tp1.view.Messages;

public class SuperLaser extends UCMLaser{

	public static final int DAMAGE2 = 2;
	
	public SuperLaser(Position pos, GameWorld game, int life) {
		super(pos, game, life);
	}

	@Override
	public void computerAction() {
		game.performAttack(this);
	}
	
	@Override
	public int getDamage() {
		return DAMAGE2;
	}
	
	@Override
	public String toString() {
		return Messages.SUPERLASER_SYMBOL;
	}
}
