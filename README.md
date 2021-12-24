# Checkers
A Checkers game application (of [Russian version](https://en.wikipedia.org/wiki/Russian_draughts)) 
developed in Java [Swing](https://en.wikipedia.org/wiki/Swing_(Java)) framework. It provides 4 game modes; 
- **2 Player Mode** - 2 players can play against each other taking turns
- **Player vs Bot** - randomized bot responds to player's moves
- **2 Bot Mode** - 2 randomized bots play a game sequence
- **Player vs [CEngine](https://github.com/ArastunM/CEngine)** - a smarter bot (engine) responds to player's moves

Which can be played under different checkerboard layouts.
The game also provides game downloads and uploads using 
[PDN](PDN_Specification.pdf).

<img src="images/whitePiece.png" width = 60></img>
<img src="images/blackPiece.png" width = 60></img>
<img src="images/whiteKing.png" width = 60></img>
<img src="images/blackKing.png" width = 60></img>

## Prerequisites
[Java version 15.0.1](https://www.oracle.com/java/technologies/javase/jdk15-archive-downloads.html) 
was used for development

## Installation
The project utilizes in-built Java package [Swing](https://en.wikipedia.org/wiki/Swing_(Java)) for 
the GUI; hence, no additional installations are required

## Getting Started
The Checkers game can be played by downloading, unzipping [Checkers.zip](Checkers.zip) and running the Checkers.jar.
For CEngine support include **CEngine.dll** within **Checkers.jar's** directory:
```
+-- Checkers
|   +-- Checkers.jar
|   +-- CEngine.dll
```

## Details
- Author - Arastun Mammadli
- License - [MIT](LICENSE)
