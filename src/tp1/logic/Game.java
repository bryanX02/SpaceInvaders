package tp1.logic;

import java.util.Random;

import tp1.control.GameModel;
import tp1.control.InitialConfiguration;
import tp1.control.exceptions.InitializationException;
import tp1.control.exceptions.LaserInFlightException;
import tp1.control.exceptions.NoShockWaveException;
import tp1.control.exceptions.NotAllowedMoveException;
import tp1.control.exceptions.NotEnoughPointsException;
import tp1.control.exceptions.OffWorldException;
import tp1.logic.gameobjects.Bomb;
import tp1.logic.gameobjects.DestroyerAlien;
import tp1.logic.gameobjects.EnemyWeapon;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.GameWorld;
import tp1.logic.gameobjects.RegularAlien;
import tp1.logic.gameobjects.ShipFactory;
import tp1.logic.gameobjects.ShockWave;
import tp1.logic.gameobjects.SuperLaser;
import tp1.logic.gameobjects.UCMLaser;
import tp1.logic.gameobjects.UCMShip;
import tp1.logic.gameobjects.UCMWeapon;
import tp1.logic.gameobjects.Ufo;
import tp1.view.GameStatus;
import tp1.view.Messages;

// Clase encargada de manejar el juego
public class Game implements GameModel, GameStatus, GameWorld {

	// CONSTANTES
	public static final int DIM_X = 9;
	public static final int DIM_Y = 8;
	private static final String SPACE = " ";
	private static final String NEW_LINE = System.lineSeparator();
	private static final String LIFE = "Life:";
	private static final String POINTS = "Points:";
	private static final String SW = "ShockWave:";

	// Atributos
	private GameObjectContainer container;
	private Level level;
	private long seed;
	private UCMShip player;
	private int currentCycle;
	private AlienManager alienManager; 
	private Ufo ufo;
	private Random rand;
	private boolean doExit;

	// Constructor
	public Game(Level level, long seed) {
		this.level = level;
		this.seed = seed;
		initGame(InitialConfiguration.NONE);
	}

	// FALLA EL ATAQUE
	
	// M�todo que se encarga de inicializar los atributos de la clase.
	// Es decir, inicia el juego.
	private void initGame(InitialConfiguration conf) {
	
		rand = new Random(seed);
		doExit = false;
		currentCycle = 0;
		alienManager = new AlienManager(this, level);
		container = alienManager.initialize(conf);
		player = new UCMShip(this);
		ufo = new Ufo(this, null, 0);
		container.add(player);
	
	}
	
	// M�todos de GameModel
	
	@Override
    public void move(Move motion) throws OffWorldException, NotAllowedMoveException {
        Position aux = new Position(player.getPos().getCol(), player.getPos().getRow());
        aux = aux.move(motion);

        if (aux.getCol() < 0 || aux.getCol() > 8) {
            throw new OffWorldException(String.format(Messages.OFF_WORLD_MESSAGE, motion.toString().toLowerCase(), player.getPos()));
        }

        if (!player.isAllowedMove(motion)) {
            throw new NotAllowedMoveException(Messages.DIRECTION_ERROR + motion.toString() + "\nAllowed UCMShip moves: <" + UCMShip.allowedMoves("|") + ">");
        }

        player.move(motion);
    }
	
	// Trata de disparar el laser
	@Override
	public void shootLaser() throws LaserInFlightException {
		
		if (!player.isCanShoot()) {
            throw new LaserInFlightException(Messages.LASER_ERROR);
        }
        player.shootLaser();
		
	}
	
	// Funci�n que trata de ejecutar el ShockWave
	@Override
	public void shockWave() throws NoShockWaveException {
		
		// Variables
		ShockWave sw = player.executeShockWave();
		if (sw == null) {
            throw new NoShockWaveException(Messages.SHOCKWAVE_ERROR);
        }
        container.checkAttacks(sw);
        container.removeDead();
        disableShockWave();
		
	}
	
	// Funci�n que actualiza lo valores dej juego
	@Override
	public void update() {
		
		// Siempre que se llame a esta funci�n el juego aumentara de ciclo
		currentCycle ++;
		
		container.automaticMoves();
		container.computerActions();
		container.removeDead();
		
		if (!ufo.isAlive()) {
			ufo.onDelete();
			ufo.computerAction();
			if (ufo.isAlive())
				container.add(ufo);
		}
			
		
		// Se comprueba si el jugador elimino a todos los aliens o el jugador murio
		if (alienManager.allAlienDead() || !player.isAlive())
			exit();
	}
	
	// Metodo que resetea el juego
	@Override
	public void reset(InitialConfiguration conf) throws InitializationException {
		
		try {
            initGame(conf);
        } catch (Exception e) {
            throw new InitializationException(Messages.INITIAL_CONFIGURATION_ERROR);
        }
		
	}
	
	// M�todos de GameWorld


	// Funci�n con sobrecarga que a�ade un objeto al juego
	@Override
	public void addObject(UCMWeapon laser) {
		
		// En este caso a�ade un nuevo laser
		container.add(laser);
		
	}
	
	// Funci�n con sobrecarga que a�ade un objeto al juego
	@Override
	public void addObject(Bomb bomb) {
		
		// En este caso a�ade un nuevo laser
		container.add(bomb);
		
	}
	
	// Comprueba si el laser impact� con alg�n elemento del juego
	@Override
	public void performAttack(UCMWeapon laser) {
		
		container.checkAttacks(laser);
		/*if (ufo.getPosition() != null && laser.getPos() != null) {
			if (ufo.isOnPosition(laser.getPos())) {
				ufo.receiveDamage(UCMLaser.DAMAGE);
				laser.die();
				enableShockWave();
			}
		}*/
	
	}
	
	// Comprueba una bomba impacto con la nave
	@Override
	public boolean performAttack(EnemyWeapon bomb) {
		
		boolean impact = false;
		
		if (bomb.getPos().equals(player.getPos())) {
			player.recieveAttack(bomb.getDamage());
			bomb.die();
			bomb.onDelete();
			impact = true;
		}
		
		return impact;
	
	}
	
	// M�todo que aumenta los puntos del jugador
	@Override
	public void recievePoints(int p) {
		player.recievePoints(p);	
	}
	
	// M�todo que elimina al jugador
	@Override
	public void playerDead() {		
		player.die();
	}

	// M�todo que habilita del ShockWave 
	@Override
	public void enableShockWave() {
		player.enableShockWave();
	}
	

	// Funci�n que indica si el juego ha terminado
	@Override
	public boolean isFinished() {
		return doExit;
	}
	
	// M�todo que le indica al juego que debe acabar
	@Override
	public void exit() {
		doExit = true;	
	}

	@Override
	public void enableLaser() {
		player.enableLaser();
	}
	

	// Funci�n que devuelve el atributo rand del game
	@Override
	public Random getRandom() {
		return rand;
	}
	

	// Funci�n que devuelve el atributo level del game
	@Override
	public Level getLevel() {
		return level;
	}
	
	
	// M�todos de GameStatus
	
	// Funci�n que recibe los parametros de una casilla del tablero, y busca y devuelve el valor correspondiente
	@Override
	public String positionToString(int col, int row) {
		
		// Variables
		String element = " ";
		Position box = new Position(col, row);
		GameObject obj;
		
		
	
		obj = container.getObjFromPos(box);
		
		if (obj != null) {
			element = obj.toString();
		}
	
		// Si no hubiese coincidido con ninguna posici�n de los elementos del juego devolver�a " "
		return element;
	}

	

	// Funcion que obtiene y formatea los datos de los elementos del juego
	@Override
	public String infoToString() {
		
		StringBuilder str = new StringBuilder();
		
		str.append(String.format(Messages.ALIEN_DESCRIPTION, Messages.REGULAR_ALIEN_DESCRIPTION, RegularAlien.POINTS, 0, RegularAlien.ARMOR)).append(NEW_LINE);
		str.append(String.format(Messages.ALIEN_DESCRIPTION, Messages.DESTROYER_ALIEN_DESCRIPTION, DestroyerAlien.POINTS, 1, DestroyerAlien.ARMOR)).append(NEW_LINE);
		str.append(String.format(Messages.ALIEN_DESCRIPTION, Messages.EXPLOSIVE_ALIEN_DESCRIPTION, 12, 1, 2)).append(NEW_LINE);
		str.append(String.format(Messages.UCM_DESCRIPTION, Messages.UCMSHIP_DESCRIPTION, UCMShip.DAMAGE, UCMShip.ARMOR)).append(NEW_LINE);
		str.append(String.format(Messages.ALIEN_DESCRIPTION, Messages.UFO_DESCRIPTION, Ufo.POINTS, 0, Ufo.ARMOR));
			
		return str.toString();
		
	}
	
	// Funci�n que devuelve los datos del juego formateados en un String
	@Override
	public String stateToString() {
		
		// Variables
		StringBuilder str = new StringBuilder();
		
		// Formateo de los datos
		str.append(LIFE).append(SPACE).append(player.getLife()).append(NEW_LINE)
		.append(POINTS).append(SPACE).append(player.getPoints()).append(NEW_LINE)
		.append(SW).append(SPACE);
		
		if (player.hasShockWave())
			str.append("ON");
		else
			str.append("OFF");
		
		return str.append(NEW_LINE).toString();
		
	}
	
	// Funci�n que devuelve un booleano en funci�n de si los aliens han ganado
	@Override
	public boolean aliensWin() {
		
		return !alienManager.allAlienDead() && !player.isAlive();
		
	}

	// Funci�n que devuelve un booleano en funci�n de si el jugador ha ganado
	@Override
	public boolean playerWin() {
		
		return alienManager.allAlienDead();
		
	}
	
	// Funci�n que devuelve el ciclo actual
	@Override
	public int getCycle() {
		return currentCycle;
	}

	// Funnci�n que busca y devuelve el n�mero de aliens presentes
	@Override
	public int getRemainingAliens() {
		//TODO fill your code
		return alienManager.getRemainingAliens();
	}


	// Otros m�todos

	
	// M�todo que deshabilita del ShockWave 
	private void disableShockWave() {		
		player.disableShockWave();	
	}

	@Override
	public void shootSuperLaser() throws NotEnoughPointsException {
		if (player.getPoints() < 5) {
            throw new NotEnoughPointsException(String.format(Messages.NOT_ENOUGH_POINTS_ERROR, player.getPoints(), 5));
        }
        player.shootSuperLaser();
	}

	@Override
	public void explotar(Position pos) {
		
		String element = " ";
		Position box;
		GameObject obj;
		
		// Primero la fila (recorremos columnas)
		for (int i = 0; i < DIM_X; i++) {
			if (i != pos.getCol()) {
				box = new Position(pos.getRow(), i);
				obj = container.getObjFromPos(box);
				obj.receiveAttack(1);
			}
		}
	
		// Recorremos las filas de la misma columna
	    for (int j = 0; j < DIM_Y; j++) {
	        if (j != pos.getRow()) {
	            box = new Position(j, pos.getCol());
	            obj = container.getObjFromPos(box);
	            if (obj != null) {
	                obj.receiveAttack(1);
	            }
	        }
	    }
	    
	    // Recorremos las diagonales
	    int[][] deltas = {
	        {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
	    };
	    
	    for (int[] delta : deltas) {
	        int newRow = pos.getRow() + delta[0];
	        int newCol = pos.getCol() + delta[1];
	        
	        if (newRow >= 0 && newRow < DIM_Y && newCol >= 0 && newCol < DIM_X) {
	            box = new Position(newRow, newCol);
	            obj = container.getObjFromPos(box);
	            if (obj != null) {
	                obj.receiveAttack(1);
	            }
	        }
	    }
		
	}

	
}
