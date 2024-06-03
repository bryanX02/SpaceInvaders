package tp1.logic.gameobjects;

import tp1.logic.Position;

public class Ship extends GameObject {

	public Ship(GameWorld game, Position pos, int life) {
		super(game, pos, life);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getSymbol() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getDamage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getArmour() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onDelete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void automaticMove() {
		// TODO Auto-generated method stub

	}

}
