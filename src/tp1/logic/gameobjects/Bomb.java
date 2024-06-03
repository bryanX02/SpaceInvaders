package tp1.logic.gameobjects;

import tp1.logic.Position;
import tp1.view.Messages;

public class Bomb extends EnemyWeapon{

	// CONSTANTES
	public static final int ARMOUR = 1;
	public static final int DAMAGE = 1;
	
	// Atributos
	private DestroyerAlien owner;

	// Constructor
	public Bomb(DestroyerAlien owner, GameWorld game) {
		super(game, new Position(owner.getPos().getCol(), owner.getPos().getRow()), ARMOUR);
		this.owner = owner;
	}

	@Override
	protected String getSymbol() {
		return Messages.BOMB_SYMBOL;
	}

	@Override
	protected int getArmour() {
		return ARMOUR;
	}

	// Funci�n que devuelve el da�o que provoca la bomba
	@Override
	public int getDamage() {
		return DAMAGE;
	}
	
	
	// M�todo que habilita la bomba en su nave due�a
	@Override
	public void onDelete() {
		owner.enableBomb();
	}

	// Funci�n booleana respecto a si esta fuera del tablero
	public boolean isOut() {
		return pos.getRow() == 8;
	}
	
	@Override
	public String toString() {
		return getSymbol();
	}

	@Override
	public void computerAction() {
		game.performAttack(this);
	}
	
}