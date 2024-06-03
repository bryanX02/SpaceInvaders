package tp1.logic;

import tp1.logic.gameobjects.DestroyerAlien;
//import tp1.logic.gameobjects.DestroyerAlien;
import tp1.logic.gameobjects.RegularAlien;
import tp1.logic.lists.DestroyerAlienList;
//import tp1.logic.lists.DestroyerAlienList;
import tp1.logic.lists.RegularAlienList;

/**
 * 
 * Manages alien initialization and
 * used by aliens to coordinate movement
 *
 */
public class AlienManager {
	
	// Atributos
	private Level level;
	private Game game;
	private int remainingAliens;
	private int remainingRAliens;
	private int remainingDAliens;
	private boolean squadInFinalRow;
	private int shipsOnBorder;
	private boolean onBorder;

	// Constructor
	public AlienManager(Game game, Level level) {
		this.level = level;
		this.game = game;
		remainingRAliens = level.getNumRegularAliens();
		remainingDAliens = level.getNumDestroyerAliens();
		remainingAliens = remainingRAliens + remainingDAliens;
		onBorder = false;
	}
		
	// INITIALIZER METHODS
	
	/**
	 * Initializes the list of regular aliens
	 * @return the initial list of regular aliens according to the current level
	 */
	protected RegularAlienList initializeRegularAliens() {
		
		// Variables
		RegularAlienList raList;
		RegularAlien ra;
		int row = 1;
		int col = 2;
		
		// Instanciación de la lista 
		raList = new RegularAlienList(level.getNumRegularAliens());
		
		// Se añaden los aliens a la lista con sus correspondientes posiciones
		for (int i = 0; i < level.getNumRegularAliens(); i++) {
			
			// Valores para la segunda fila de regular aliens
			if (i == 4) {
				row = 2;
				col = 2;
			}
			ra = new RegularAlien(new Position(col, row), game, level.getNumCyclesToMoveOneCell(), Move.LEFT, this);
			raList.add(ra, i);
			col ++;
		}
		return raList;
	}

	/**
	 * Initializes the list of destroyer aliens
	 * @return the initial list of destroyer aliens according to the current level
	 */
	protected  DestroyerAlienList initializeDestroyerAliens() {
		
		// Variables
		DestroyerAlienList raList;
		DestroyerAlien ra;
		int row = 2;
		int col = 0;
		
		// Instanciación de la lista 
		raList = new DestroyerAlienList(level.getNumDestroyerAliens());
		
		// Si el nivel no es el EASY la fila de naves destructoras sera la 3
		if (!level.equals(Level.EASY))
			row = 3;
		
		// Creamos, posicionamos y añadimos las naves a la lista
		for (int i = 0; i < level.getNumDestroyerAliens(); i++) {
			col = i + 3;
			if (level.equals(Level.INSANE))
				col --;
			ra = new DestroyerAlien(new Position(col, row), game, level.getNumCyclesToMoveOneCell(), Move.LEFT, this);
			raList.add(ra, i);
		}
		
		return raList;
	}


	// CONTROL METHODS
	
	
	// Método que decrementa el numero de naves a descender al ser llamado desde la lista
	public void readyToDescend() {
		shipsOnBorder --;
	}
	
	// Método que marca las naves al llegar al borde
	public void shipOnBorder() {
		
		if(!onBorder) {
			onBorder = true;
			shipsOnBorder = remainingAliens;
		}
		
	}

	// Función boolean respecto a si todas las naves han salido del borde
	public boolean onBorder() {
		
		boolean on = false;
		
		if (shipsOnBorder > 0)
			on = true;
		else 
			onBorder = false;
		
		return on;
		
	}

	// Método que llama a la eliminación de un regularAlien
	public void regularAlienDead() {
		
		remainingRAliens --;
		remainingAliens --;
		game.recievePoints(RegularAlien.POINTS);
		
	}

	// Método que llama a la eliminación de un destroyerAlien
	public void destroyerAlienDead() {
		
		remainingDAliens --;
		remainingAliens --;
		game.recievePoints(DestroyerAlien.POINTS);
		
	}
	
	// Método que elimina al jugador y finaliza el juego al haber llegado los aliens al final
	public void finalRowReached() {
		
		squadInFinalRow = true;
		game.playerDead();
		game.exit();
		
	}

	// Función booleana respecto a la muerte de todos los aliens
	public boolean allAlienDead() {	
		return remainingAliens == 0;
	}

	// Función que devuelve el número de aliens restantes
	public int getRemainingAliens() {
		return remainingAliens;
	}

	public GameObjectContainer initialize() {
		
		// Variables
		GameObjectContainer container;
		RegularAlien ra;
		DestroyerAlien da;
		int row = 1;
		int col = 2;
		
		// Instanciación de la lista 
		container = new GameObjectContainer();
		
		// Regular aliens
		
		// Se añaden los aliens a la lista con sus correspondientes posiciones
		for (int i = 0; i < level.getNumRegularAliens(); i++) {
			
			// Valores para la segunda fila de regular aliens
			if (i == 4) {
				row = 2;
				col = 2;
			}
			
			ra = new RegularAlien(new Position(col, row), game, level.getNumCyclesToMoveOneCell(), Move.LEFT, this);
			container.add(ra);
			col ++;
		}
		
		// Destroyer Aliens 
		row = 2;
		col = 0;
		
		// Si el nivel no es el EASY la fila de naves destructoras sera la 3
		if (!level.equals(Level.EASY))
			row = 3;
		
		// Creamos, posicionamos y añadimos las naves a la lista
		for (int i = 0; i < level.getNumDestroyerAliens(); i++) {
			col = i + 3;
			if (level.equals(Level.INSANE))
				col --;
			da = new DestroyerAlien(new Position(col, row), game, level.getNumCyclesToMoveOneCell(), Move.LEFT, this);
			container.add(da);
		}
		
		return container;
		
	}
	
	

}
