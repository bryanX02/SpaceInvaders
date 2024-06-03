package tp1.logic.gameobjects;

import tp1.logic.Game;
import tp1.logic.Position;

public class ShockWave extends UCMWeapon{
	
	// Constantes
	public static final int DAMAGE = 1;
	
	// Constructor
	public ShockWave(Position pos, int life, GameWorld game) {
		super(game, pos, life);
	}

	@Override
	public boolean performAttack(GameItem obj) {
		boolean impact = true;
		
		/*if (!pos.equals(da.getPosition())) {
			impact = false;
		}*/
		
		return impact;
	}

	public boolean performAttack(RegularAlien ra) {
		boolean impact = true;
		
		/*
		if (pos.equals(ra.getPosition())) {
			impact = true;
		}*/
		
		return impact;
	}
	
	
	
}
