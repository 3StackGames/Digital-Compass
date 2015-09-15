package testGame;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.Player;

public class AGameState extends GameState {

    @Expose
    private String gameCode;
    @Expose
    private List<Player> players = new ArrayList<Player>();

    /**
     *
     * @return
     * The gameCode
     */
    public String getGameCode() {
        return gameCode;
    }

    /**
     *
     * @param gameCode
     * The gameCode
     */
    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    /**
     *
     * @return
     * The players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     *
     * @param players
     * The players
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}