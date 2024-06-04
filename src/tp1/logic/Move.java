package tp1.logic;

/**
 * Represents the allowed movements in the game
 *
 */
public enum Move {
	
	// Tipos
	LEFT(-1,0), LLEFT(-2,0), RIGHT(1,0), RRIGHT(2,0), DOWN(0,1), UP(0,-1), NONE(0,0);
	
	// Atributos
	private int x;
	private int y;
	
	// Constructor
	private Move(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	// Getters
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	// Devuelve el Move del sentido contrario
	public Move flip() {
		Move moveFliped;
		
		if (this.equals(LEFT))
			moveFliped = RIGHT;
		else
			moveFliped = LEFT;
		
		return moveFliped;
	}
	
	// Devuelve el Move cuyo nombre es el param.
	public static Move parse(String param) {
		Move move;
		
		param = param.toUpperCase();
		move = valueOf(param.toUpperCase());
			
		return move;
	}
	
}
