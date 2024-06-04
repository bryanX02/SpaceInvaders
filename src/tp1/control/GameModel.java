package tp1.control;

import java.util.Random;

import tp1.control.exceptions.InitializationException;
import tp1.control.exceptions.LaserInFlightException;
import tp1.control.exceptions.NoShockWaveException;
import tp1.control.exceptions.NotAllowedMoveException;
import tp1.control.exceptions.NotEnoughPointsException;
import tp1.control.exceptions.OffWorldException;
import tp1.logic.Move;

public interface GameModel {
	
	public void move(Move move) throws OffWorldException, NotAllowedMoveException;
	public void shootLaser() throws LaserInFlightException;
	public void shockWave() throws NoShockWaveException;
	public boolean isFinished();
	public void update();
	public void reset(InitialConfiguration conf) throws InitializationException;
	public void exit();
	public Random getRandom();
	public String infoToString();
	public void shootSuperLaser() throws NotEnoughPointsException;
	
}
