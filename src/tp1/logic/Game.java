package tp1.logic;

import java.util.Random;

import tp1.control.GameModel;
import tp1.logic.gameobjects.Bomb;
import tp1.logic.gameobjects.DestroyerAlien;
import tp1.logic.gameobjects.EnemyWeapon;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.GameWorld;
import tp1.logic.gameobjects.RegularAlien;
import tp1.logic.gameobjects.ShockWave;
import tp1.logic.gameobjects.UCMLaser;
import tp1.logic.gameobjects.UCMShip;
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
	private UCMLaser laser;
	private int currentCycle;
	private AlienManager alienManager; 
	private Ufo ufo;
	private Random rand;
	private boolean doExit;

	// Constructor
	public Game(Level level, long seed) {
		this.level = level;
		this.seed = seed;
		initGame();
	}

	// FALLA EL ATAQUE
	
	// Método que se encarga de inicializar los atributos de la clase.
	// Es decir, inicia el juego.
	private void initGame() {
	
		rand = new Random(seed);
		doExit = false;
		currentCycle = 0;
		
		laser = new UCMLaser(null, this, 0);
		alienManager = new AlienManager(this, level);
		container = alienManager.initialize();
		player = new UCMShip(this);
		container.add(player);
		ufo = new Ufo(this, false);
		
	}
	
	// Métodos de GameModel
	
	// Función que trate de realizar el movimiento de la nave, y devuelve si tuvo exito o no
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
		
		if (!laser.isAlive()) {
			
			player.shootLaser();
			shot = true;
		}
		
		return shot;
		
	}
	
	// Función que trata de ejecutar el ShockWave
	@Override
	public boolean shockWave() {
		
		// Variables
		ShockWave sw = player.executeShockWave();
		boolean executed = false;
		
		if (sw != null) {
			executed = true;/*
			daList.checkAttacks(sw);
			raList.checkAttacks(sw);
			bombList.recivesShockWave();*/
			disableShockWave();
		}
		return executed;
		
	}
	
	// Función que actualiza lo valores dej juego
	@Override
	public void update() {
		
		// Siempre que se llame a esta función el juego aumentara de ciclo
		currentCycle ++;
		
		// COMPUTER ACTIONS
		/*daList.computerActions();
		ufo.computerAction();
		
		// MOVIMIENTOS
		// Se realizan los movimientos
		raList.automaticMoves();
		daList.automaticMoves();
			
		if (ufo.getPosition() != null)
			ufo.automaticMove();
		
		if (bombList.size() > 0) {
			bombList.automaticMoves();
			bombList.checkAttacks(laser);
		}
		
		if (laser.isAlive()) {
			laser.automaticMove();
			if (laser.getPos() != null)
				performAttack(laser);
		}
		
		// Los removeDeads se realizan en sus clases
		daList.removeDead();
		raList.removeDead();
		bombList.removeDead();*/
		container.automaticMoves();
		container.computerActions();
		container.removeDead();
		// Se comprueba si el jugador elimino a todos los aliens o el jugador murio
		if (alienManager.allAlienDead() || !player.isAlive())
			exit();
	}
	
	// Metodo que resetea el juego
	@Override
	public void reset() {
		
		initGame();
		
	}
	
	// Métodos de GameWorld


	// Función con sobrecarga que añade un objeto al juego
	@Override
	public void addObject(UCMLaser laser) {
		
		// En este caso añade un nuevo laser
		container.add(laser);
		
	}
	
	// Función con sobrecarga que añade un objeto al juego
	@Override
	public void addObject(Bomb bomb) {
		
		// En este caso añade un nuevo laser
		container.add(bomb);
		
	}
	
	// Comprueba si el laser impactó con algún elemento del juego
	@Override
	public void performAttack(UCMLaser laser) {
		
		container.checkAttacks(laser);
		/*if (ufo.getPosition() != null && laser.getPos() != null) {
			if (ufo.isOnPosition(laser.getPos())) {
				ufo.receiveDamage(UCMLaser.DAMAGE);
				laser.die();
				enableShockWave();
			}
		}*/
	
	}
	
	// Comprueba si el laser impactó con algún elemento del juego
	@Override
	public boolean performAttack(EnemyWeapon bomb) {
		
		boolean impact = false;
		if (bomb.getPos().equals(player.getPos())) {
			player.recieveAttack(bomb.getDamage());
			bomb.die();
			bomb.onDelete();
			impact = true;
		} else if (laser.getPos() != null && bomb.getPos() != null) {
			if (bomb.getPos().equals(laser.getPos()))
			{
				laser.die();
				bomb.die();
				bomb.onDelete();
				impact = true;
			}
		}
		
		return impact;
	
	}
	
	// Método que aumenta los puntos del jugador
	@Override
	public void recievePoints(int p) {
		player.recievePoints(p);	
	}
	
	// Método que elimina al jugador
	@Override
	public void playerDead() {		
		player.die();
	}

	// Método que habilita del ShockWave 
	@Override
	public void enableShockWave() {
		player.enableShockWave();
	}
	

	// Función que indica si el juego ha terminado
	@Override
	public boolean isFinished() {
		return doExit;
	}
	
	// Método que le indica al juego que debe acabar
	@Override
	public void exit() {
		doExit = true;	
	}

	@Override
	public void enableLaser() {
		player.enableLaser();
	}
	

	// Función que devuelve el atributo rand del game
	@Override
	public Random getRandom() {
		return rand;
	}
	

	// Función que devuelve el atributo level del game
	@Override
	public Level getLevel() {
		return level;
	}
	
	
	// Métodos de GameStatus
	
	// Función que recibe los parametros de una casilla del tablero, y busca y devuelve el valor correspondiente
	@Override
	public String positionToString(int col, int row) {
		
		// Variables
		String element = " ";
		Position box = new Position(col, row);
		GameObject obj;
		
		/*
		// Busqueda de los aliens comunes
		element = raList.toString(box);
		
		// Busqueda de los aliens destructores
		if (element == " ")
			element = daList.toString(box);
		
		// Busqueda del jugador
		if (player.getPos().equals(box) && element == " ") 
			if (player.isAlive())
				element = Messages.UCMSHIP_SYMBOL;
			else
				element = Messages.UCMSHIP_DEAD_SYMBOL;
		
		// Busqueda de la bombas
		if (bombList.size() > 0 && element == " ")
			element = bombList.toString(box);
		*/	
		
		
	
		obj = container.getObjFromPos(box);
		
		if (obj != null) {
			element = obj.toString();
		}
	
		// Busqueda del ufo
		/*if (ufo.getPosition() != null) {
			if (ufo.isOnPosition(box))
				
				element = Messages.UFO_SYMBOL + "[" + String.format("%02d", ufo.getLife()) + "]";;
		}*/
		// Si no hubiese coincidido con ninguna posición de los elementos del juego devolvería " "
		return element;
	}

	

	// Funcion que obtiene y formatea los datos de los elementos del juego
	@Override
	public String infoToString() {
		
		StringBuilder str = new StringBuilder();
		
		str.append(String.format(Messages.UCM_DESCRIPTION, Messages.UCMSHIP_DESCRIPTION, UCMShip.DAMAGE, UCMShip.ARMOR)).append(NEW_LINE);
		str.append(String.format(Messages.ALIEN_DESCRIPTION, Messages.REGULAR_ALIEN_DESCRIPTION, RegularAlien.POINTS, 0, RegularAlien.ARMOR)).append(NEW_LINE);
		str.append(String.format(Messages.ALIEN_DESCRIPTION, Messages.DESTROYER_ALIEN_DESCRIPTION, DestroyerAlien.POINTS, 1, DestroyerAlien.ARMOR)).append(NEW_LINE);
		str.append(String.format(Messages.ALIEN_DESCRIPTION, Messages.UFO_DESCRIPTION, Ufo.POINTS, 0, Ufo.ARMOR));
		
		return str.toString();
		
	}
	
	// Función que devuelve los datos del juego formateados en un String
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
	
	// Función que devuelve un booleano en función de si los aliens han ganado
	@Override
	public boolean aliensWin() {
		
		return !alienManager.allAlienDead() && !player.isAlive();
		
	}

	// Función que devuelve un booleano en función de si el jugador ha ganado
	@Override
	public boolean playerWin() {
		
		return alienManager.allAlienDead();
		
	}
	
	// Función que devuelve el ciclo actual
	@Override
	public int getCycle() {
		return currentCycle;
	}

	// Funnción que busca y devuelve el número de aliens presentes
	@Override
	public int getRemainingAliens() {
		//TODO fill your code
		return alienManager.getRemainingAliens();
	}


	// Otros métodos

	
	// Método que deshabilita del ShockWave 
	private void disableShockWave() {		
		player.disableShockWave();	
	}
	
}
