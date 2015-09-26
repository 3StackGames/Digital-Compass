package testGame;

import java.util.List;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class VotePhase extends BasicPhase {

    public BasicGameState processAction(BasicAction action, BasicGameState state) {
        GameState gameState = (GameState) state;
        VoteAction lieAction = (VoteAction) action;
        
        //add vote
        String votedLie = lieAction.getLie();
        String believer = lieAction.getPlayer().getDisplayName();
        List<Lie> lies = gameState.getLies();
        for(Lie lie : lies) {
        	if(lie.getLie().equals(votedLie)) {
        		lie.getBelievers().add(believer);
        		
        		//check if done
        		if(gameState.getVoteCount() == gameState.getPlayers().size()) {
        			gameState.setCurrentPhase(new RevealPhase());
        		}
        		//stop looking for a match
        		break;
        	}
        }

        return state;
    }


}
