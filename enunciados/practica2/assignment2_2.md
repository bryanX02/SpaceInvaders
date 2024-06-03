<!-- TOC start -->
- [Assignment 2 (Part II): Space Invaders Extended](#práctica-2-parte-ii-space-invaders-extended)
- [Ship factory](#factoría-de-naves)
- [Initial configurations](#configuraciones-iniciales)
- [New command: Super laser](#nuevo-comando-superláser)
- [New Alien Ship: Explosive Ship](#nueva-nave-alienígena-nave-explosiva)
<!-- TOC end -->
<!-- TOC --><a name="práctica-2-parte-ii-space-invaders-extended"></a>
# Assignment 2 (Part II): Space Invaders Extended

**Submission: 7th December at 10:00hrs**

**Objective:** Inheritance, polymorphism, abstract classes, and interfaces.

**Frequently Asked Questions**: As it is usual that you have doubts (it is normal) we will compile them in this [Frequently Asked Questions document](../faq.md). To find out about the latest changes, you can [check the history of the document](https://github.com/informaticaucm-TPI/2324-SpaceInvaders/commits/main/enunciados/faq.md).

In this assignment, we are going to extend the code with new functionalities. 

We start with a **warning**:

> If you break encapsulation, use methods that return lists or make any use of the Java constructs `instanceof` or `getClass()` (with the exception of the possible use of `getClass()` in `equals` methods as shown in the slides) you will automatically fail the assignment. The same applies to the use of any *DIY getClass* (i.e. construction designed to reveal the concrete subclass of `GameObject`) such as a set of `isX` methods, one for each subclass `X` of `GameObject`, where in the particular subclass `Y`, the method `isY` returns true and the other methods return `false`.

**NOTE:** We recommend you read all of the problem statement before you start to implement your solution, since it explains all the functionality that you are required to implement before providing some indications about how you should implement it.

<!-- TOC --><a name="factoría-de-naves"></a>
# Ship Factory

The first thing we're going to do is add a  ship ***factory*** to our assignment. A factory
It allows you to separate the logic of the creation of an object from the place where it is created. Such a factory is going to be given by  a `ShipFactory`  class in the `gameobjects`  package with a method

```java
public static AlienShip spawnAlienShip(String input, GameWorld game, Position pos, AlienManager am)
```

Using the factory, to create a ship whose type is given in a string `input`, it is enough to do the following:

```java
AlienShip ship = ShipFactory.spawnAlienShip(input, game, position, am);
```

Notice that when we created the alien ship, we don't know what its type is. The implementation of this method will follow the same structure as the `parse` method of `CommandGenerator`. `ShipFactory` will maintain a list of available alien ships, just as `CommandGenerator` maintains a list of available commands. 

```java
private static final List<AlienShip> AVAILABLE_ALIEN_SHIPS = Arrays.asList(
	new RegularAlien(),
	new DestroyerAlien()
);
```
The `spawnAlienShip` method will traverse through that list asking if each ship's symbol matches the one determined by `input`. In this case, a copy is requested from the ship, initialized with the rest of the arguments (`game`, `pos` and `am`). To do this, a method in `AlienShip` will be included:

```java
protected abstract AlienShip copy(GameWorld game, Position pos, AlienManager am);
```
This will be implemented in a very simple way in each of the alien ships. For example, in
`RegularAlien` implementation is restricted to returning a `RegularAlien`  

```java
@Override
protected Ship copy(GameWorld game, Position pos, AlienManager am){
	return new RegularAlien(game, pos, am);
}
```
Notice that in order to create the ships in the `AVAILABLE_ALIEN_SHIPS`  list, we need the ships to have a constructor without arguments.

<!-- TOC --><a name="configuraciones-iniciales"></a>
# Initial configurations

We're going to use the Alien Ship Factory to be able to conveniently change the initial setup of the game. To do this, we'll modify the `ResetCommand` command to support an optional parameter.

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

In the `conf_2` configuration there are only three regular ships and one destroyer.

To represent initial configurations, we're going to use in this assignment an enumerated
`InitialConfiguration` in the `control` package. Each value in the list has a list of
descriptions of ships, which are simply strings of the form `S X Y` (ship with symbol S in the position (X, Y)). For example, `R 2 2` describes a common alien ship in position (2, 2) and `D 3 3` a destroyer in (3, 3).

You should modify the `ResetCommand` class to support a parameter that represents an initial configuration, and to have its `execute` method for the invocation of a `game.reset(conf)`  method of the `GameModel` that receives the initial user-given configuration as a parameter.

You should also modify the `AlienManager` class so that its `initialize` method receives an initial configuration. If the initial configuration received is none, the initialization will be the standard one (the one that depends on `level`). Otherwise, the factory will be used to initialize the container:

```java
container.add(ShipFactory.spawnAlienShip(words[0], game,
				new Position(Integer.valueOf(words[1]), Integer.valueOf(words[2])), this));
```

We can assume that the data obtained from the configuration is always correct.

<!-- TOC --><a name="nuevo-comando-superláser"></a>
# New command: Super laser

We're adding a new command to the game that allows the player to launch a super laser. A super laser is the same as a laser except that it deals damage to two lives, instead of just one life, and is displayed on the board with the symbol `ǁǁ`. To be able to throw a superlaser, the player will have to spend 5 points (and, therefore, so much, you won't be able to shoot it if they doesn't have them).

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
# New AlienShip: Explosive Ship

Now that we have our own ship factory and have used heritage to generalize game items, it's easy to extend the game with new ships. We're adding a new type of ship: the
explosive ship. An explosive ship behaves like an ordinary ship, but when it dies it produces an explosion that takes a life away from all adjacent objects (also diagonally). Explosive ships are shown with the `E` symbol and 12 points are earned for killing them.

```
Command > l

[DEBUG] Executing: l

[U]CM Ship: damage='1', endurance='3'
[R]egular Alien: points='5', damage='0', endurance='2'
[D]estroyer Alien: points='10', damage='1', endurance='1'
U[f]o: points='25', damage='0', endurance='1'
[E]xplosive Alien: points='12', damage='0', endurance='2'
```