# Digital-Compass
> Game Framework for Web-Based Party Games

Digital-Compass allows you to write your game logic in Java and handles networking, so all you need to do is focus on your game.


[Demo Game] Subtle Scheme
=========
Requirements
----
  * [MongoDB](https://www.mongodb.org/)
  * [Java 1.7+](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
  * Java IDE ([Eclipse](https://eclipse.org/) or [IntelliJ](https://www.jetbrains.com/idea/))
  * [NodeJS](https://nodejs.org/en/)

Setup
-----
  * Each time you run the game, run each component in this order

### 1 - MongoDB ###
1.  In terminal of your choice, go to the database directory: `cd database`
2.  Import the packs: `mongoimport --db SubtleScheme --collection Packs --file packs.json`
3.  Import the questions: `mongoimport --db SubtleScheme --collection Questions --file questions.json`

### 2 - Bridge Setup ###
 1. Go to the bridge directory: `cd bridge`
 2. Install dependencies: `npm install`
 3. Run the bridge: `node bridge`

### 3 - Game Engine Setup ###
1. Open your Java IDE
2. Import Maven Project, select the pom.xml in the backend directory
3. Run main.java
  * The bridge should output "Backend Connected" when it successfully connects
  * **Note**: Restarting the game engine doesn't require restarting the bridge

### 4 - Client Setup ###
* The client can be written in any language with [Socket.io](http://socket.io/) support
* One basic client that works with Subtle Scheme is a [Testing Dashboard](https://github.com/3StackGames/digital-compass-client)
