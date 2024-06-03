package tp1.logic.gameobjects;

import java.util.Random;

import tp1.logic.Level;

public interface GameWorld {

	public void addObject(UCMLaser laser);
	public void addObject(Bomb bomb);
	public void performAttack(UCMLaser laser);
	public boolean performAttack(EnemyWeapon bomb);
	public void recievePoints(int p);
	public void playerDead();
	public void enableShockWave();
	void enableLaser();
	public Random getRandom();
	public Level getLevel();
}
