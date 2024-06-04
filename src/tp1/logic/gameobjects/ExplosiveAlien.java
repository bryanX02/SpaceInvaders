package tp1.logic.gameobjects;

import tp1.view.Messages;

public class ExplosiveAlien extends RegularAlien {

	@Override
	protected String getSymbol() {
		// TODO Auto-generated method stub
		return Messages.EXPLOSIVE_ALIEN_SYMBOL;
	}

	
	@Override
	public void onDelete() {
		game.explotar(pos);
	}

	@Override
	public String toString() {
		return Messages.EXPLOSIVE_ALIEN_SYMBOL;
	}
	
	

}
