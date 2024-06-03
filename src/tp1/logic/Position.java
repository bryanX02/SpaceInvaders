package tp1.logic;

/**
 * 
 * Immutable class to encapsulate and manipulate positions in the game board
 * 
 */
public class Position {

	// Atributos
	private int col;
	private int row;

	// Constructor
	public Position(int col, int row) {
		this.col = col;
		this.row = row;
	}

	// Getters
	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	// Setters
	public void setRow(int row) {
		this.row = row;
	}

	
	// Método que ejecuta el movimiento pasado por parametro
	public Position move(Move move) {
		return new Position(col + move.getX(), row + move.getY());
	}

	// Función booleana respecto a si la posición esta en el borde del tablero
	public boolean isOnBorder() {
		
		boolean is = false;
		
		if (col == 0 || col == 8)
			is = true;
		
		return is;
	}
	
	// Función booleana respecto a la equivalencia con otra posición pasada por parametro
	public boolean equals(Position pos) {

		// Variablees
		boolean result = false;
		
		// Si tienen la misma columna y fila son equivalentes
		if (col == pos.col && row == pos.row)
			result = true;
		return result;
	}

	// Función booleana respecto a si la posición esta fuera del tablero
	public boolean isOut() {

		// Variablees
		boolean result = false;
		
		// Si tienen la misma columna y fila son equivalentes
		if (col < 0 || col > 8 || row < 0 || row > 7)
			result = true;
		return result;
	}

	
}
