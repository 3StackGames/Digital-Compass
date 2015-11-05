# Getting Started: Hello Party

## [UNDER CONSTRUCTION]##

This quick tutorial will show you how to make a backend for a game where each round players vote which player should lose. Then, a random player is chosen based on the votes and is displayed on the screen.


Main.java

```java
import com.three_stack.digital_compass.backend.PhaseManager;

public class Main {
    public static void main(String args[]){
        //Initialize the phase manager
        PhaseManager phaseManager = new PhaseManager();
        //Connect to our bridge
        String bridgeUrl = "http://localhost:3333";
        //Initialize a factory, which will generate new game states for whenever a new game starts
        GameStateFactory gameStateFactory = new GameStateFactory();
        phaseManager.initialize(bridgeUrl, gameStateFactory);
    }
}
```
