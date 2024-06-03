package tp1.logic.lists;

import tp1.logic.Position;
import tp1.logic.gameobjects.DestroyerAlien;
import tp1.logic.gameobjects.ShockWave;
import tp1.logic.gameobjects.UCMLaser;
import tp1.view.Messages;

/**
 * Container of destroyers aliens, implemented as an array with a counter
 * It is in charge of forwarding petitions from the game to each destroyer alien
 * 
 */
public class DestroyerAlienList {

	// Atributos
	private DestroyerAlien[] objects;
	private int num;
	private int deadAliens;
	
	// Constructor
	public DestroyerAlienList(int num) {
		objects = new DestroyerAlien[num];
		deadAliens = 0;
		this.num = num;
	}
	
	// Método que añade un alien a la lista de aliens
	public void add (DestroyerAlien da, int i) {
		objects[i] = da;
	}
	
	// Función que devuelve el tamaño de la lista
	public int size() {
		return num;
	}

	// Función que devuelve los caracteres de un destroyer alien para la posición dada
	public String toString(Position pos) {
		
		// Variables
		String str = " ";
		DestroyerAlien da;
		
		// Se busca el alien en la posición, y si no lo encuentra se recibe un null
		da = getAlien(pos);
		
		// Si lo ha encontrado se devuelve el simbolo del alien más su vida
		if (da != null)
			str += Messages.DESTROYER_ALIEN_SYMBOL + "[" + String.format("%02d", da.getLife()) + "]";
		
		return str;
		
	}

	// Función que devuelve el RegularAlien que tenga la misma posición que la dada
	private DestroyerAlien getAlien(Position pos) {
		
		// Variables
		DestroyerAlien da = null;
		boolean encontrado = false;
		int i = 0;
		
		// Busqueda en la lista
		while (i < size() && !encontrado) {
			
			// Se comparan posiciones para buscarlo
			if (objects[i].getPos().equals(pos)) {
				da = objects[i];
				encontrado = true;
			}
			i ++;
		}
		
		return da;
	}

	// Método que llama a los automaticMove de cada alien de la lista
	public void automaticMoves() {
					
		for (DestroyerAlien da: objects)
			da.automaticMove();
			
	}

	// Método que comprueba para cada alien si recibió un impacto del laser
	public void checkAttacks(UCMLaser laser) {
		
		for (DestroyerAlien da: objects) {
			
			// Se llama a la propia funcion booleana del laser para ver si impactó
			if(laser.performAttack(da)) {
				da.receiveAttack(laser);
				laser.die();
				if (!da.isAlive())
					deadAliens++;
					
			}
				
		}
		
	}

	// Método que elimina el alien que este muerto (su vida = 0)
	public void removeDead() {
	
		// Variables
		DestroyerAlien[] newDaArray;
		int j = 0;
		
		if (deadAliens > 0) {
			newDaArray = new DestroyerAlien[num-deadAliens];
			// Método que elimina el alien que este muerto (su vida = 0)
			for (DestroyerAlien da: objects) {
			
				if(da.getLife() == 0) {
					deadAliens--;
					num --;
				}else {
					// Los aliens que estén vivos se añaden al nuevo array con el iterador j
					newDaArray[j] = da;
					j ++;
				}
			}
			
			// Método que recibe para cada alien el ataque del ShockWave
			objects = newDaArray;
		}
	}


	// Método que recibe para cada alien el ataque del ShockWave
	public void checkAttacks(ShockWave sw) {
		
		for (DestroyerAlien da: objects) {
			if (sw.performAttack(da))
				da.receiveAttack(sw);
			if (!da.isAlive())
				deadAliens++;
		}
		
	}
	
}
