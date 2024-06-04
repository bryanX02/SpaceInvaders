package tp1.logic.gameobjects;

import tp1.logic.AlienManager;
import tp1.logic.Game;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

/**
 * 
 * Class representing a regular alien
 *
 */
public class RegularAlien extends AlienShip{

	// CONSTANTES
	public static final int ARMOR = 2;
	public static final int POINTS = 5;
	
	// Atribuos
	private int cyclesToMove;
	private int speed;
	private Move dir;
	private AlienManager alienManager;

	
	
	public RegularAlien() {
		super(null, null, 0);
	}


	// Contructor
	public RegularAlien(Position pos, GameWorld game, AlienManager alienManager) {
		super(game, pos, ARMOR);
		this.speed = game.getLevel().getNumCyclesToMoveOneCell();
		this.dir = Move.LEFT;
		this.alienManager = alienManager;
		cyclesToMove = speed;
	}
	

	// Funci�n que devuelve los puntos que otorga la eliminaci�n del alien
	@Override
	public int getPoints() {
		return POINTS;
	}

	@Override
	public boolean isOnPosition(Position pos) {
		// TODO Auto-generated method stub
		return super.isOnPosition(pos);
	}


	@Override
	protected String getSymbol() {
		// TODO Auto-generated method stub
		return Messages.REGULAR_ALIEN_SYMBOL;
	}


	@Override
	protected int getArmour() {
		// TODO Auto-generated method stub
		return super.getArmour();
	}


	@Override
	public void onDelete() {
		// TODO Auto-generated method stub
		super.onDelete();
	}


	/**
	 *  Implements the automatic movement of the regular alien	
	 */
	@Override
	public void automaticMove() {
			
		// Si ya es hora de moverse
		if (cyclesToMove == 0) {
	
			// Se mueve
			performMovement(dir);
		
			// Si se encuentra en el borde avisamos al manager
			if(pos.isOnBorder())
				alienManager.shipOnBorder();
		
			// Reinicimaos la espera del movimiento
			cyclesToMove = speed;
		
		} else {
			
			// Si estaba en el borde descendemos
			if (alienManager.onBorder()) {

				// Avisamos que de que ya va a descender (alien por alien)
				alienManager.readyToDescend();
				descent();
				
			} else
				cyclesToMove --;
			
		}
		
	}

	// M�todo que desciende un alien y cambia su direcci�n
	private void descent() {
		
		performMovement(Move.DOWN);
		dir = dir.flip();
	}

	// M�todo que realiza el movimiento pasado por parametro
	private void performMovement(Move dir) {
		
		pos = pos.move(dir);
		
		// Ya de paso analizamos si llego al final para terminar el juego en ese caso
		if (isInFinalRow()) {
			alienManager.finalRowReached();
		}
		
	}

	// M�todo que avisa al manager de que el alien murio
	@Override
	public void die() {
		alienManager.regularAlienDead();
	}
	
	// Funci�n boolena respecto a si el alien esta en la �ltima fila
	private boolean isInFinalRow() {
		
		return pos.getRow() == 7;
				
	}

	@Override
	public String toString() {
		return " " + getSymbol() + "[" + String.format("%02d", life) + "]";
	}


	@Override
	protected AlienShip copy(GameWorld game, Position pos, AlienManager am) {
		return new RegularAlien(pos, game, am);
	}

	
	
}