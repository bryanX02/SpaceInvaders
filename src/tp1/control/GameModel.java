package tp1.control;

import java.util.Random;

import tp1.logic.Move;

public interface GameModel {
	
	public boolean move(Move move);
	public boolean shootLaser();
	public boolean shockWave();
	public boolean isFinished();
	public void update();
	public void reset();
	public void exit();
	public Random getRandom();
	
}
