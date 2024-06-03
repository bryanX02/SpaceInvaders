<!-- TOC start -->
- [Práctica 2 (Parte II): Space Invaders Extended](#práctica-2-parte-ii-space-invaders-extended)
- [Factoría de naves](#factoría-de-naves)
- [Configuraciones iniciales](#configuraciones-iniciales)
- [Nuevo comando: superláser](#nuevo-comando-superláser)
- [Nueva nave alienígena: nave explosiva](#nueva-nave-alienígena-nave-explosiva)
<!-- TOC end -->
<!-- TOC --><a name="práctica-2-parte-ii-space-invaders-extended"></a>
# Práctica 2 (Parte II): Space Invaders Extended

**Entrega: Semana del 4 de diciembre**

**Objetivos:** Herencia, polimorfismo, clases abstractas e interfaces.

**Preguntas Frecuentes**: Como es habitual que tengáis dudas (es normal) las iremos recopilando en este [documento de preguntas frecuentes](../faq.md). Para saber los últimos cambios que se han introducido [puedes consultar la historia del documento](https://github.com/informaticaucm-TPI/2324-SpaceInvaders/commits/main/enunciados/faq.md).

En esta práctica vamos a extender el código con nuevas funcionalidades. 

Antes de comenzar, tened en cuenta la **advertencia**:

> La falta de encapsulación, el uso de métodos que devuelvan listas, y el uso de `instanceof` o `getClass()` tiene como consecuencia un **suspenso directo** en la práctica. Es incluso peor implementar un `instanceof` casero, por ejemplo así: cada subclase de la clase `GameObject` contiene un conjunto de métodos `esX`, uno por cada subclase X de `GameObject`; el método `esX` de la clase X devuelve `true` y los demás métodos `esX` de la clase X devuelven `false`.

<!-- TOC --><a name="factoría-de-naves"></a>
# Factoría de naves

Lo primero que vamos a hacer va a ser añadir una ***factoría*** de naves a nuestra práctica.
Una factoría nos permite separar la lógica de la creación de un objeto del lugar en el que se crea. Dicha factoría va a venir dada por una clase `ShipFactory` en el paquete `gameobjects` con un método 

```java
public static AlienShip spawnAlienShip(String input, GameWorld game, Position pos, AlienManager am)
```

Usando la factoría, para crear una nave cuyo tipo viene dado en un string `input`, basta hacer

```java
AlienShip ship = ShipFactory.spawnAlienShip(input, game, position, am);
```

Fíjate que al crear la nave alienígena no conocemos cuál es su tipo.
La implementación de este método seguirá la misma estructura que el método `parse` de `CommandGenerator`. 
`ShipFactory` mantendrá una lista de naves alienígenas disponibles, al igual que `CommandGenerator` mantiene una lista de comandos disponibles. 

```java
private static final List<AlienShip> AVAILABLE_ALIEN_SHIPS = Arrays.asList(
	new RegularAlien(),
	new DestroyerAlien()
);
```

El método `spawnAlienShip` recorrerá esa lista preguntando si el símbolo de cada nave coincide con el determinado por `input`. Si es así, se solicita a la nave una copia, inicializada con el resto de argumentos (`game`, `pos` y `am`). Para ello añadiremos en
`AlienShip` un método

```java
protected abstract AlienShip copy(GameWorld game, Position pos, AlienManager am);
```

que implementaremos de forma muy sencilla en cada una de las naves alienígenas. Por ejemplo, en `RegularAlien` su implementación se limita a devolver un `RegularAlien`

```java
@Override
protected AlienShip copy(GameWorld game, Position pos, AlienManager am){
	return new RegularAlien(game, pos, am);
}
```

Observa que para crear las naves en la lista `AVAILABLE_ALIEN_SHIPS` necesitamos que las naves tengan un constructor sin argumentos.

<!-- TOC --><a name="configuraciones-iniciales"></a>
# Configuraciones iniciales

Vamos a usar la factoría de naves alienígenas para poder cambiar de forma cómoda la configuración inicial de la partida. Para ello, modificaremos el comando `ResetCommand` para que admita un parámetro opcional.

```
Command > h

[DEBUG] Executing: h

Available commands: 
[h]elp : shows this help
[m]ove <left|lleft|right|rright> : moves the UCMShip in the indicated direction
[n]one : user does not perform any action
[s]hoot : player shoots a laser
shock[w]ave : player shoots a shockwave
[l]ist : prints the list of current ships
[e]xit : exits the game
[r]eset [<NONE|CONF_1|CONF_2|CONF_3>] : resets the game

Command > reset conf_2

[DEBUG] Executing: reset conf_2

Number of cycles: 0
Life: 3
Points: 0
ShockWave: OFF
Remaining aliens: 4
 ───────────────────────────────────────────────────────────────
|      |      |      |      |      |      |      |      |      |
 ───────────────────────────────────────────────────────────────
|      |      |      |      |      |      |      |      |      |
 ───────────────────────────────────────────────────────────────
|      |      |      |      |      |      |      |      |      |
 ───────────────────────────────────────────────────────────────
|      |      | R[02]| R[02]| D[01]| R[02]|      |      |      |
 ───────────────────────────────────────────────────────────────
|      |      |      |      |      |      |      |      |      |
 ───────────────────────────────────────────────────────────────
|      |      |      |      |      |      |      |      |      |
 ───────────────────────────────────────────────────────────────
|      |      |      |      |      |      |      |      |      |
 ───────────────────────────────────────────────────────────────
|      |      |      |      | ^__^ |      |      |      |      |
 ───────────────────────────────────────────────────────────────

```

En la configuración `conf_2` solo hay tres naves comunes y una destructora.

Para representar configuraciones iniciales, vamos a usar en esta práctica un
enumerado `InitialConfiguration` en el paquete `control`. Cada valor del enumerado tiene asociada una lista de descripciones de naves, que son simplemente strings de la forma `S X Y` (nave con símbolo S en la posición (X, Y)). Por ejemplo, `R 2 2` describe una 
nave alienígena común en la posición (2, 2) y `D 3 3` una destructora en (3, 3).

Modifica la clase `ResetCommand` para que admita un parámetro que represente una
configuración inicial y para que su método `execute` invoque a un método `game.reset(conf)`
de `GameModel` que recibe como parámetro la configuración inicial dada por el usuario. 

Modifica también la clase `AlienManager` para que su método `initialize` reciba una
configuración inicial. Si la configuración inicial recibida es `none`, la inicialización
será la estándar (la que depende de `level`). De lo contrario, se utilizará la factoría
de naves para inicializar el contenedor

```java
container.add(ShipFactory.spawnAlienShip(words[0], game,
				new Position(Integer.valueOf(words[1]), Integer.valueOf(words[2])), this));
```

Podemos suponer que los datos obtenidos de la configuración son siempre correctos.

<!-- TOC --><a name="nuevo-comando-superláser"></a>
# Nuevo comando: superláser

Vamos a añadir un nuevo comando al juego que permite al jugador lanzar un superláser.
Un superláser es igual que un láser excepto porque produce un daño de dos vidas, en vez de solo una vida, y se muestra en el tablero con el símbolo `ǁǁ`.
Para poder lanzar un superláser el jugador tendrá que gastar 5 puntos (y, por lo tanto, no podrá dispararlo si no los tiene).

```
Command > h

[DEBUG] Executing: h

Available commands: 
[h]elp : shows this help
[m]ove <left|lleft|right|rright> : moves the UCMShip in the indicated direction
[n]one : user does not perform any action
[s]hoot : player shoots a laser
shock[w]ave : player shoots a shockwave
[l]ist : prints the list of current ships
[e]xit : exits the game
[r]eset [<NONE|CONF_1|CONF_2|CONF_3>] : resets the game
[s]uper[L]aser : shoots a super laser when player has enough points

Command > sl

[DEBUG] Executing: sl

Super laser cannot be shot
```


<!-- TOC --><a name="nueva-nave-alienígena-nave-explosiva"></a>
# Nueva nave alienígena: nave explosiva

Ahora que tenemos nuestra factoría de naves y hemos utilizado herencia para generalizar los objetos de juego, resulta muy sencillo extender el juego con nuevas naves. 
Vamos a añadir un nuevo tipo de nave: la nave explosiva. Una nave explosiva se comporta como una nave común, pero al morir produce una explosión que quita una vida a todos los objetos adyacentes (también en diagonal). Las naves explosivas se muestran con el símbolo `E` y se obtienen 12 puntos al matarlas.

```
Command > l

[DEBUG] Executing: l

[U]CM Ship: damage='1', endurance='3'
[R]egular Alien: points='5', damage='0', endurance='2'
[D]estroyer Alien: points='10', damage='1', endurance='1'
U[f]o: points='25', damage='0', endurance='1'
[E]xplosive Alien: points='12', damage='0', endurance='2'
```





