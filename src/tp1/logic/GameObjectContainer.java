package tp1.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.print.attribute.standard.Sides;

import tp1.logic.gameobjects.DestroyerAlien;
import tp1.logic.gameobjects.EnemyWeapon;
import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.ShockWave;
import tp1.logic.gameobjects.UCMWeapon;
import tp1.view.Messages;

public class GameObjectContainer {

	private List<GameObject> objects;
	private int deadObjects;

	public GameObjectContainer() {
		objects = new ArrayList<>();
		deadObjects = 0;
	}

	public void add(GameObject object) {
		objects.add(object);
	}

	public void remove(GameObject object) {
		objects.remove(object);
	}

	public void automaticMoves() {
		objects.forEach(obj -> {
			obj.automaticMove();
			if (obj.getPos().isOut()) {
				obj.die();
				obj.onDelete();
				deadObjects++;
			}
		});
		
	}

	public void computerActions() {
		
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).computerAction();
		}
	}

	// Función que devuelve el objeto de la lista que tenga la posición dada por parámetro
	public GameObject getObjFromPos(Position pos) {
		
		// Variables
		GameObject object = null;
		boolean encontrado = false;
		int i = 0;
		
		// Se busca el alien en la posición, y si no lo encuentra se recibe un null
		while (i < objects.size() && !encontrado) {
			
			if (objects.get(i).isOnPosition(pos))
				encontrado = true;
			else
				i++;
		}
		
		if (encontrado)
			object = objects.get(i);
		
		return object;
		
	}
	
	// Método que comprueba para cada alien o bomba si fue atacado por el laser o el shockWave
	public void checkAttacks(UCMWeapon weapon) {
		
		if (weapon.isAlive()) {	
			
			objects.forEach(obj -> {
				
				if (weapon.performAttack(obj)) {
					
					weapon.die();
					weapon.onDelete();
					deadObjects++;
					if (obj.receiveAttack(weapon)) {
						
						deadObjects++;
						obj.onDelete();
						
					}
				}
				
			});
			
		}
		
	}  
	
	// Método que comprueba para cada alien o bomba si fue atacado por el laser o el shockWave
	public void checkAttacks(EnemyWeapon weapon) {
		
		if (weapon.isAlive()) {	
			
			objects.forEach(obj -> {
				
				if (weapon.performAttack(obj)) {
					
					weapon.die();
					weapon.onDelete();
					deadObjects++;
					if (obj.receiveAttack(weapon)) {
						
						deadObjects++;
						obj.onDelete();
						
					}
				}
				
			});
			
		}
		
	}  
	
	// Método que elimina el alien que este muerto (su vida = 0)
	public void removeDead() {
	
		if (deadObjects > 0) {
			
			for (int i = 0; i < objects.size(); i++) {
				if (!objects.get(i).isAlive()) {
					remove(objects.get(i));
					deadObjects--;
				}
			}

		}
		System.out.println(objects.size());
	}
	
}
