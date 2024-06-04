package tp1.logic.gameobjects;

import java.util.Arrays;
import java.util.List;

import tp1.logic.AlienManager;
import tp1.logic.Game;
import tp1.logic.Position;

public class ShipFactory {

	private static final List<AlienShip> AVAILABLE_ALIEN_SHIPS = Arrays.asList(
			new RegularAlien(),
			new DestroyerAlien()
	);
	
	public static AlienShip spawnAlienShip(String input, GameWorld game, Position pos, AlienManager am) {
		
		AlienShip as = null;
		int i = 0;
		
		while (i < AVAILABLE_ALIEN_SHIPS.size() && as == null) {
			
			if (AVAILABLE_ALIEN_SHIPS.get(i++).getSymbol().equalsIgnoreCase(input))
				as = AVAILABLE_ALIEN_SHIPS.get(--i).copy(game, pos, am);
			
		}
		return as;
		
	}


	
}
