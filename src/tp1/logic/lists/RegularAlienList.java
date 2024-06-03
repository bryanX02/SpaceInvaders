package tp1.logic.lists;

import tp1.logic.Position;
import tp1.logic.gameobjects.RegularAlien;
import tp1.logic.gameobjects.ShockWave;
import tp1.logic.gameobjects.UCMLaser;
import tp1.view.Messages;

/**
 * Container of regular aliens, implemented as an array with a counter
 * It is in charge of forwarding petitions from the game to each regular alien
 * 
 */
public class RegularAlienList {

	// Atributos
	private RegularAlien[] objects;
	private int num;
	private int deadAliens;
	
	// Constructor
	public RegularAlienList(int num) {
		objects = new RegularAlien[num]; 
		deadAliens = 0;
		this.num = num;
	}
	
	// M�todo que a�ade un alien a la lista
	public void add (RegularAlien ra, int i) {
		objects[i] = ra;
	}
	
	// Funci�n que devuelve el tama�o de la lista
	public int size() {
		return num;
	}

	// Funci�n que devuelve los caracteres de un regular alien para la posici�n dada
	public String toString(Position pos) {
		
		// Variables
		String str = " ";
		RegularAlien ra;
		
		// Se busca el alien en la posici�n, y si no lo encuentra se recibe un null
		ra = getAlien(pos);
		
		// Si lo ha encontrado se devuelve el simbolo del alien m�s su vida
		if (ra != null)
			str += Messages.REGULAR_ALIEN_SYMBOL + "[" + String.format("%02d", ra.getLife()) + "]";
		
		return str;
		
	}

	// Funci�n que devuelve el RegularAlien que tenga la misma posici�n que la dada 
	private RegularAlien getAlien(Position pos) {
		
		// Variables
		RegularAlien ra = null;
		boolean encontrado = false;
		int i = 0;
		
		// Busqueda en la lista
		while (i < size() && !encontrado) {
			
			// Se comparan posiciones para buscarlo
			if (objects[i].getPos().equals(pos)) {
				ra = objects[i];
				encontrado = true;
			}
			i ++;
		}
		
		return ra;
	}

	// M�todo que llama a los automaticMove de cada alien de la lista
	public void automaticMoves() {
					
		for (RegularAlien ra: objects)
			ra.automaticMove();
			
	}

	// M�todo que comprueba para cada alien si recibi� un impacto del laser
	public void checkAttacks(UCMLaser laser) {
		
		for (RegularAlien ra: objects) {
			
			// Se llama a la propia funcion booleana del laser para ver si impact�
			if(laser.performAttack(ra)) {
				ra.receiveAttack(laser);
				laser.die();
				if (!ra.isAlive())
					deadAliens++;
			}
				
		}
		
	}

	// M�todo que elimina el alien que este muerto (su vida = 0)
	public void removeDead() {
	
		// Variables
		RegularAlien[] newRaArray;
		int j = 0;
		
		if (deadAliens > 0) {
			newRaArray = new RegularAlien[num - deadAliens];
			for (RegularAlien ra: objects) {
			
				if(ra.getLife() == 0) {
					deadAliens --;
					num --;
				} else {
					// Los aliens que est�n vivos se a�aden al nuevo array con el iterador j
					newRaArray[j] = ra;
					j ++;
				}
			}
			
			// El array de la lista se atualiza
			objects = newRaArray;
		}
	}

	// M�todo que recibe para cada alien el ataque del ShockWave
	public void checkAttacks(ShockWave sw) {
		for (RegularAlien ra: objects) {
			if (sw.performAttack(ra))
				ra.receiveAttack(sw);
			if (!ra.isAlive())
				deadAliens--;
		}
			
	}
}
