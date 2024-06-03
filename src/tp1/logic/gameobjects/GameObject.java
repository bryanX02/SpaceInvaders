package tp1.logic.gameobjects;

import tp1.logic.Position;

public abstract class GameObject implements GameItem {

	// Atributos
	protected Position pos;
	protected int life;
	protected GameWorld game;
	
	// Constructor
	public GameObject(GameWorld game, Position pos, int life) {	
		this.pos = pos;
		this.game = game;
		this.life = life;
	}
	
	@Override
	public boolean isAlive() {
		return this.life > 0;
	}
	
	@Override
	public boolean isOnPosition(Position pos) {
		return this.pos.equals(pos);
	}
	
	public int getLife() {
		return this.life;
	}

	public Position getPos() {
		return pos;
	}

	public GameWorld getGame() {
		return game;
	}

	
	// Funciones y métodos que se heredaran o no
	protected abstract String getSymbol();
	protected abstract int getDamage();
	protected abstract int getArmour();
	
			
	public abstract void onDelete();
	public abstract void automaticMove();
	public void computerAction() {};
	
	@Override
	public void die() {life = 0;}
	
	
	@Override
	public boolean performAttack(GameItem other) {
		// Variables
		boolean impact = false;
		
		if (other != this) {
			// Comprobación de posiones
			if (pos != null) {
				if (other.isOnPosition(pos))
					impact = true;
			}
			
		}
		
		
		return impact;
	}
	
	@Override
	public boolean receiveAttack(EnemyWeapon weapon) {return false;}

	@Override
	public boolean receiveAttack(UCMWeapon weapon) {
		
		boolean objDie = false;
		
		receiveDamage(weapon.getDamage());
		if (!isAlive())
			objDie = true;
		
		return objDie;
		
	}
	
	// Método que le resta el daño a la vida del objeto
	private void receiveDamage(int damage) {
		life -= damage;
	}

}
