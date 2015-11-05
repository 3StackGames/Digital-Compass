# Digital-Compass
> Game Framework for Web-Based Party Games

Digital-Compass makes it easy to write party games in Java. The framework handles the boilerplate logic and networking so you can focus on writing exciting game logic.

## What is a Party Game?

This framework is specifically designed with party games in mind. In particular, party games throw away the traditional concepts of controllers, consoles, and waiting for your turn. Any device with a web-browser can act as a controller and any device with a web-browser can be your "Television."

One of the underlying principles of the party game is that nobody should have to wait for their turn. Everyone takes their turn at the same time, every phase of the game.

## Game Stack

There are three major components to the game stack. As a developer, you only need to develop the Game Engine and the Client for your game. The bridge serves primarily to relay messages between the clients and the game engine and is already written for you.

1. [Backend](#backend)
  * Requirements:
    * Java JDK
  * Recommended:
    * Maven
2. [Bridge](#bridge)
  * Requirements:
    * NodeJS
3. [Client](#client)
  * Requirements:
    * TBD


## Backend

The Backend, or Game Engine, is where all the logic happens. Digital-Compass was designed with an authoritative server in mind, which means that all the logic updates to the game state happen here. The only thing the clients do is send inputs for the backend to process.

### Installation

#### Option 1: Maven

Add the following lines to your `pom.xml`

```xml
<project>

  <repositories>
      <repository>
          <id>digital-compass</id>
          <name>Digital Compass</name>
          <url>http://rushpoll.com:8081/nexus/content/repositories/digital-compass</url>
      </repository>
  </repositories>

  <dependencies>
    <dependency>
        <groupId>com.three-stack.digital-compass</groupId>
        <artifactId>backend</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
  </dependencies>

</project>
```
#### Option 2: Build Jar

You can compile the jar from the source code in the `backend` directory. From there you can include the jar as a dependency in your project.

## Bridge

The bridge, which is already complete, has 2 purposes.

1. Managing game lobbies before the game starts
2. Passing actions between the clients and the backend

### Running the Bridge

1. Go to the bridge directory `cd bridge`
2. Install dependencies `npm install`
3. Run the bridge `node bridge.js`

## Client

Clients can be written in any language with Socket.IO support. However, we'd recommend using our JavaScript library when possible: [here](https://github.com/3StackGames/digital-compass-client).


## Getting Started

We recommend starting with a quick game: [Hello Party](docs/hello-party.md)
