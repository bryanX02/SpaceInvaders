package tp1.logic.gameobjects;

import tp1.logic.AlienManager;
import tp1.logic.Move;
import tp1.logic.Position;
import tp1.view.Messages;

public class DestroyerAlien extends AlienShip{

		// CONSTANTES
		public static final int ARMOR = 1;
		public static final int POINTS = 10;
		
		// HAY ALGO RARO EN LOS CYCLETOMOVE
		
		// Atributos
		private int cyclesToMove;
		private int speed;
		private Move dir;
		private AlienManager alienManager;
		private boolean canShootBomb;

		public DestroyerAlien() {
			super(null, null, 0);
		}

		// Constructor
		public DestroyerAlien(Position pos, GameWorld game, AlienManager alienManager) {
			super(game, pos, ARMOR);
			this.speed = game.getLevel().getNumCyclesToMoveOneCell();
			this.dir = Move.LEFT;
			this.alienManager = alienManager;
			cyclesToMove = speed;
			canShootBomb = true;
		}
		
		// Funci�n que devuelve los puntos que otorga la eliminaci�n del alien
		@Override
		public int getPoints() {
			// TODO Auto-generated method stub
			return POINTS;
		}
		
		// Funci�n que devuelve el da�o que provoca el alien
		@Override
		public int getDamage() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public boolean isOnPosition(Position pos) {
			// TODO Auto-generated method stub
			return super.isOnPosition(pos);
		}

		@Override
		protected String getSymbol() {
			// TODO Auto-generated method stub
			return Messages.DESTROYER_ALIEN_SYMBOL;
		}

		@Override
		protected int getArmour() {
			// TODO Auto-generated method stub
			return ARMOR;
		}

		// M�todo que simula la acci�n del alien
		@Override
		public void computerAction() {
			
			// Si puede disparar y el random lo permite
			if (canShootBomb && canGenerateRandomBomb()) {
				
				// Se crea y se a�ade la bomba al juego
				game.addObject(new Bomb(this, game));
				canShootBomb = false;
				
			}
				
		}
		
		// Funci�n booleana respecto a un Random para decidir si se lanza una bomba
		private boolean canGenerateRandomBomb () {
			double random;
			random = game.getRandom().nextDouble();
			return  cyclesToMove != 0 && random < game.getLevel().getShootFrequency();
		}

		/**
		 *  Implements the automatic movement of the regular alien	
		 */
		@Override
		public void automaticMove() {
				
			// Si ya es hora de moverse
			if (cyclesToMove == 0) {
		
				// Se mueve
				performMovement(dir);
			
				// Si se encuentra en el borde avisamos al manager
				if(pos.isOnBorder())
					alienManager.shipOnBorder();
			
				// Reinicimaos la espera del movimiento
				cyclesToMove = speed;
			
			} else {
				
				// Si estaba en el borde descendemos
				if (alienManager.onBorder()) {

					// Avisamos que de que ya va a descender (alien por alien)
					alienManager.readyToDescend();
					descent();
					
				} else
					cyclesToMove --; // Si no es hora de moverse ni tengo que descender, entonces solo avanzamos el ciclo
				
			}
			
		}

		// M�todo que desciende un alien y cambia su direcci�n
		private void descent() {
			
			performMovement(Move.DOWN);
			dir = dir.flip();
		}

		// M�todo que realiza el movimiento pasado por parametro
		private void performMovement(Move dir) {
			
			pos = pos.move(dir);
			
			// Ya de paso analizamos si llego al final para terminar el juego en ese caso
			if (isInFinalRow()) {
				alienManager.finalRowReached();
			}
			
		}

		// M�todo que avisa al manager de que el alien murio
		@Override
		public void die() {
			alienManager.destroyerAlienDead();	
		}

		// Funci�n boolena respecto a si el alien esta en la �ltima fila
		private boolean isInFinalRow() {
			return pos.getRow() == 7;			
		}

		@Override
		public String toString() {
			return " " + getSymbol() + "[" + String.format("%02d", life) + "]";
		}

		// M�todo que habilita el disparo de la bomba
		public void enableBomb() {
			
			canShootBomb = true;
			
		}

		public boolean isAlive() {
			return life > 0;
		}

		@Override
		protected AlienShip copy(GameWorld game, Position pos, AlienManager am) {
			return new DestroyerAlien(pos, game, am);
		}	
		
	}