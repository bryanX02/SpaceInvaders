package tp1.logic;

import java.util.Random;

import tp1.control.GameModel;
import tp1.control.InitialConfiguration;
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
	
	// Funci�n que trate de realizar el movimiento de la nave, y devuelve si tuvo exito o no
	@Override
	public boolean move(Move motion) {

		// Variables
		boolean result = false;
		Position aux = new Position(player.getPos().getCol(), player.getPos().getRow());
		aux = aux.move(motion);
		// Si no se sale del borde puede moverse
		if (aux.getCol() >= 0 && aux.getCol() <= 8) {

			player.move(motion);
			result = true;
			
		}
		
		return result;
		
	}
	
	// Trata de disparar el laser
	@Override
	public boolean shootLaser() {
		
		boolean shot = false; 
		
		if (player.isCanShoot()) {
			
			player.shootLaser();
			shot = true;
		}
		
		return shot;
		
	}
	
	// Funci�n que trata de ejecutar el ShockWave
	@Override
	public boolean shockWave() {
		
		// Variables
		ShockWave sw = player.executeShockWave();
		boolean executed = false;
		
		if (sw != null) {
			executed = true;
			container.checkAttacks(sw);
			container.removeDead();
			disableShockWave();
		}
		return executed;
		
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
	public void reset(InitialConfiguration conf) {
		
		initGame(conf);
		
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
	public boolean shootSuperLaser() {
		Boolean disparado = false;
		if (player.getPoints() >= 5) {
			player.shootSuperLaser();
			disparado = true;
		}
		return disparado;
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
