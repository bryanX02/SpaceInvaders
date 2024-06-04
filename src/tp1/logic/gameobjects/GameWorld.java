package tp1.logic.gameobjects;

import java.util.Random;

import tp1.logic.Level;
import tp1.logic.Position;

public interface GameWorld {

	public void addObject(UCMWeapon laser);
	public void addObject(Bomb bomb);
	public void performAttack(UCMWeapon laser);
	public boolean performAttack(EnemyWeapon bomb);
	public void recievePoints(int p);
	public void playerDead();
	public void enableShockWave();
	void enableLaser();
	public Random getRandom();
	public Level getLevel();
	public void explotar(Position pos);
}
