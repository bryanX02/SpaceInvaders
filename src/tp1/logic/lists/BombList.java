package tp1.logic.lists;


import tp1.logic.Position;
import tp1.logic.gameobjects.Bomb;
import tp1.logic.gameobjects.DestroyerAlien;
import tp1.logic.gameobjects.ShockWave;
import tp1.logic.gameobjects.UCMLaser;
import tp1.view.Messages;


public class BombList {

	// Atributos
	private Bomb[] objects;
	private int deadBombs;
	private int num;
	
	// Constructor
	public BombList() {
		num = 0;
		deadBombs = 0;
	}
	
	// M�todo que a�ade una bomba a la lista
	public void add (Bomb bomb) {
		
		// Variables
		Bomb[] newList = new Bomb[num + 1];
		num = 0;
		
		// Se a�aden las bombas existentes
		if (objects != null) {
			
			for (Bomb b: objects)
				newList[num++] = b;
			
		}
		
		// Se a�ade la bomba nueva
		newList[num++] = bomb;
		objects = newList;
		
	}
	
	// Funci�n que devuelve el numero de bombas (o tama�o de la lista)
	public int size() {
		return num;
	}

	// Funci�n que devuelve el simbolo de la bomba si su posicion es la misma a la pasada por parametros
	public String toString(Position pos) {
		
		// Variables
		String str = " ";
		Bomb bomb;
		
		// Llama a la funci�n que busca una bomba con la misma posici�n
		bomb = getBomb(pos);
		
		// Si no hubiese encontrado una bomba, bomb seria null
		if (bomb != null)
			str = Messages.BOMB_SYMBOL;
		
		return str;
		
	}

	// Funci�n que busca una bomba por su posici�n y la devuelve si la encuentra
	private Bomb getBomb(Position pos) {
		
		// Variables
		Bomb bomb = null;
		boolean encontrado = false;
		int i = 0;
		
		// Busqueda (con bucle while para dejar de buscar si se encontr�)
		while (i < size() && !encontrado) {
			
			if (objects[i].getPos().equals(pos)) {
				bomb = objects[i];
				encontrado = true;
			}
			i ++;
		}
		
		return bomb;
		
	}
	
	// M�todo que reliza los movimientos de cada bomba
	public void automaticMoves() {
			
		// Para cada bomba
		for (Bomb bomb: objects) {
			
			// Se realiza el movimiento y se comprueba si se salio del tablero
			bomb.automaticMove();
			if (bomb.isOut()) {
				bomb.die();
				bomb.onDelete();
				deadBombs++;
			}
			
		}
		
	}

	// Si alguna bomba esta muerta, se elimina creando otro array sin ella
	public void removeDead() {
	
		// Variables
		Bomb[] newBombArray;
		int j = 0;
		
		// La variable deadBombs guarda la cantidad de bombas muertas en el ciclo actual
		if (deadBombs > 0) {
			
			// Si solo hubiese una bomba es la muerta y el array sera nulo
			if (num == 1) {
				objects = null;
				num = 0;
				deadBombs --;
			}
			else {
				
				// Instanciamos el nuevo array con el tama�o que tendr� sin las bombas muertas
				newBombArray = new Bomb[num-deadBombs];
				
				// Para cada bomba
				for (Bomb bomb: objects) {
					
					// Si esta muerta actualizamos las variables y el onDelete avisar� a su nave
					if(!bomb.isAlive()) {
						num --;
						deadBombs --;
						bomb.onDelete();
					}
					else {
						// Si no es la bomba muerta se a�ade al nuevo array
						newBombArray[j] = bomb;
						j ++;
					}
				}
				
				// Se actualiza el array sin las bombas muertas
				objects = newBombArray;
			}
		}
		
	}
	
	
	// M�todo que comprueba los ataques hacia las bombas
	public void recivesShockWave() {
		
		// Se comprueba que haya bombas
		if (objects != null) {
			
			// Para cada bomba
			for (Bomb bomb: objects) {
				bomb.die();
				bomb.onDelete();
				deadBombs++;
			}
			
		}
		
	}

	public void checkAttacks(UCMLaser laser) {
		
		// Se comprueba que haya bombas
		if (objects != null) {
			
			// Para cada bomba
			for (Bomb bomb: objects) {
				if (laser.isAlive())
					if (laser.receiveAttack(bomb)) {
						deadBombs++;
						bomb.die();
					}
			}
			
		}
		
	}
}
