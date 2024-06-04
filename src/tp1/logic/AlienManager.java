package tp1.logic;

import tp1.control.InitialConfiguration;
import tp1.logic.gameobjects.DestroyerAlien;
//import tp1.logic.gameobjects.DestroyerAlien;
import tp1.logic.gameobjects.RegularAlien;
import tp1.logic.gameobjects.ShipFactory;
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
		
		// Instanciaci�n de la lista 
		raList = new RegularAlienList(level.getNumRegularAliens());
		
		// Se a�aden los aliens a la lista con sus correspondientes posiciones
		for (int i = 0; i < level.getNumRegularAliens(); i++) {
			
			// Valores para la segunda fila de regular aliens
			if (i == 4) {
				row = 2;
				col = 2;
			}
			ra = new RegularAlien(new Position(col, row), game, this);
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
		
		// Instanciaci�n de la lista 
		raList = new DestroyerAlienList(level.getNumDestroyerAliens());
		
		// Si el nivel no es el EASY la fila de naves destructoras sera la 3
		if (!level.equals(Level.EASY))
			row = 3;
		
		// Creamos, posicionamos y a�adimos las naves a la lista
		for (int i = 0; i < level.getNumDestroyerAliens(); i++) {
			col = i + 3;
			if (level.equals(Level.INSANE))
				col --;
			ra = new DestroyerAlien(new Position(col, row), game, this);
			raList.add(ra, i);
		}
		
		return raList;
	}


	// CONTROL METHODS
	
	
	// M�todo que decrementa el numero de naves a descender al ser llamado desde la lista
	public void readyToDescend() {
		shipsOnBorder --;
	}
	
	// M�todo que marca las naves al llegar al borde
	public void shipOnBorder() {
		
		if(!onBorder) {
			onBorder = true;
			shipsOnBorder = remainingAliens;
		}
		
	}

	// Funci�n boolean respecto a si todas las naves han salido del borde
	public boolean onBorder() {
		
		boolean on = false;
		
		if (shipsOnBorder > 0)
			on = true;
		else 
			onBorder = false;
		
		return on;
		
	}

	// M�todo que llama a la eliminaci�n de un regularAlien
	public void regularAlienDead() {
		
		remainingRAliens --;
		remainingAliens --;
		game.recievePoints(RegularAlien.POINTS);
		
	}

	// M�todo que llama a la eliminaci�n de un destroyerAlien
	public void destroyerAlienDead() {
		
		remainingDAliens --;
		remainingAliens --;
		game.recievePoints(DestroyerAlien.POINTS);
		
	}
	
	// M�todo que elimina al jugador y finaliza el juego al haber llegado los aliens al final
	public void finalRowReached() {
		
		squadInFinalRow = true;
		game.playerDead();
		game.exit();
		
	}

	// Funci�n booleana respecto a la muerte de todos los aliens
	public boolean allAlienDead() {	
		return remainingAliens == 0;
	}

	// Funci�n que devuelve el n�mero de aliens restantes
	public int getRemainingAliens() {
		return remainingAliens;
	}

	public GameObjectContainer initialize(InitialConfiguration conf) {
		
		// Variables
		GameObjectContainer container;
		RegularAlien ra;
		DestroyerAlien da;
		int row = 1;
		int col = 2;
		
		// Instanciaci�n de la lista 
		container = new GameObjectContainer();
		
		if (conf.equals(InitialConfiguration.NONE)) {

			remainingRAliens = level.getNumRegularAliens();
			remainingDAliens = level.getNumDestroyerAliens();
			remainingAliens = remainingRAliens + remainingDAliens;
			
			// Regular aliens
			
			// Se a�aden los aliens a la lista con sus correspondientes posiciones
			for (int i = 0; i < level.getNumRegularAliens(); i++) {
				
				// Valores para la segunda fila de regular aliens
				if (i == 4) {
					row = 2;
					col = 2;
				}
				
				ra = new RegularAlien(new Position(col, row), game, this);
				container.add(ra);
				col ++;
			}
			
			// Destroyer Aliens 
			row = 2;
			col = 0;
			
			// Si el nivel no es el EASY la fila de naves destructoras sera la 3
			if (!level.equals(Level.EASY))
				row = 3;
			
			// Creamos, posicionamos y a�adimos las naves a la lista
			for (int i = 0; i < level.getNumDestroyerAliens(); i++) {
				col = i + 3;
				if (level.equals(Level.INSANE))
					col --;
				da = new DestroyerAlien(new Position(col, row), game, this);
				container.add(da);
			}
		} else {
			
			int numRA = 0;
			int numDA = 0;
			
			for (String description : conf.getShipDescription()) {
	            String[] parts = description.split(" ");
	            char symbol = parts[0].charAt(0);
	            int x = Integer.parseInt(parts[1]);
	            int y = Integer.parseInt(parts[2]);
	            container.add(ShipFactory.spawnAlienShip(symbol + "", game, new Position(x, y), this));
	            if (symbol == 'R')
	            	numRA++;
	            else if (symbol == 'D')
	            	numDA++;
	        }
			

			remainingRAliens = numRA;
			remainingDAliens = numDA;
			remainingAliens = numRA + numDA;
			
		}
		return container;
		
	}
	
	

}
