package tp1.logic;

/**
 * Represents the allowed levels in the game
 *
 */
public enum Level {

	// Tipos
	EASY(4, 1, 2, 0.1, 3, 0.5), HARD(8, 2, 2, 0.3, 2, 0.2), INSANE(8, 2, 4, 0.5, 1, 0.1);

	// Atributos
	private int numRegularAliens;
	private int numRowsRegularAliens;
	private int numDestroyerAliens;
	private double shootFrequency;
	private int numCyclesToMoveOneCell;
	private double ufoFrequency;

	// Contructor
	private Level(int numRegularAliens, int numRowsRegularAliens, 
			      int numDestroyerAliens, 
			      double shootFrequency, int numCyclesToMoveOneCell,
			      double ufoFrequency) {
		this.numRegularAliens = numRegularAliens;
		this.numRowsRegularAliens = numRowsRegularAliens;
		this.numDestroyerAliens = numDestroyerAliens;
		this.shootFrequency = shootFrequency;
		this.numCyclesToMoveOneCell = numCyclesToMoveOneCell;
		this.ufoFrequency = ufoFrequency;
	}

	// Getters
	public int getNumRegularAliens() {
		return numRegularAliens;
	}


	public int getNumRowsRegularAliens() {
		return numRowsRegularAliens;
	}


	public int getNumDestroyerAliens() {
		return numDestroyerAliens;
	}


	public int getNumCyclesToMoveOneCell() {
		return numCyclesToMoveOneCell;
	}


	public double getShootFrequency() {
		return shootFrequency;
	}


	/**
	 * Returns UFO frequency corresponding the Level
	 * @return UFO Frequency
	 */
	public double getUfoFrequency() {
		return ufoFrequency;
	}
	

	/**
	 * Parse a string and return any matching level
	 * 
	 * @param param string to parse
	 * @return the parsed {@link Level} or <code>null</code> if none match.
	 */
	public static Level valueOfIgnoreCase(String param) {
		for (Level level : Level.values())
			if (level.name().equalsIgnoreCase(param))
				return level;
		return null;
	}

	/**
	 * Returns a string representation of all the levels joined with
	 * <code>separator</code>
	 * 
	 * @param separator String used as separator
	 * @return the string resulted from joining all levels using
	 *         <code>separator</code>
	 */
	public static String all(String separator) {
		StringBuilder sb = new StringBuilder();
		for (Level level : Level.values())
			sb.append(level.name() + separator);
		String allLevels = sb.toString();
		return allLevels.substring(0, allLevels.length() - separator.length());
	}
}
