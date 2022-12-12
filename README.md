BattleBots 

DEPENDENCIES:

Java (tested on as old as 17.0.1)

Java FX (supported by installed java verison with paths correctly set)

We highly suggest installing Zulu version 19.0.1. As this version is supported by the release and Zulu includes JavaFX

INSTRUCTIONS:
Playing from the release:

Installing Zulu: (with JavaFX bundled)
It’s recommended that the Zulu version (19.0.1) is downloaded. Once downloaded and installed a restart will be needed for it to work correctly.
Download link: https://www.azul.com/downloads/?package=jdk#download-openjdk 

Running Battle Bots

The fully compiled version of the Battle Bots game is located in the releases tab. Download the newest version and place it somewhere easy to access. The game download directory will need to be opened through the terminal. Lastly, to run the game enter the command “java -jar BattleBots.jar”. 

And BAM YOU DONE.

INSTRUCTIONS: Building from source

Installing Zulu:
It’s recommended that the Zulu version (19.0.1) is downloaded, but any version lower may work. Once downloaded and installed a restart will be needed for it to work correctly.
Download link: https://www.azul.com/downloads/?package=jdk#download-openjdk 

Building Battle Bots with Zulu

Get a clone from github. Navigate to that download location inside a terminal. Once you're inside the game directory run "javac BattleBots.java". Then run "java BattleBots.java" and the game will launch.
Afterwards when you want to play navigate back to the directory and run java BattleBots.java

NOTE if you don't use the Zulu version of java you can still compile the game using the information here (guide to compile and run when javaFX isn't bundled into the java install)

https://openjfx.io/openjfx-docs/#install-javafx



And BAM YOU DONE.

GAME STRUCTURE:

Understanding the games structure. 
BattleBots is a relatively simple game with made of simple components. We HIGHLY suggest looking at our Design document https://github.com/NMSU-CS-Cook/cs371-fa2022-project-battle-bots/wiki/Design-document

The Design document goes over all of the java files 

Directories are explained below

sounds - this directory holds all of the .wav files included in the game.

Images - Holds all of the images besides robot sprites.

robot_sprites - Holds the robot sprites used in animations.

robot_sprites-GUI - Holds the sprites used specifically by the GUI in robot creation.

robots - Holds all of the robot files in the game including ones created by the user.

rosters - Holds all the roster files in the games including the ones creaeted by the user.

Docs - Holds the brochure. 

We hope you enjoy BattleBots!


