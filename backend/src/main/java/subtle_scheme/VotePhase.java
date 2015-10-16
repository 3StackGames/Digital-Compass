package subtle_scheme;

import com.three_stack.digital_compass.backend.BasicAction;
import com.three_stack.digital_compass.backend.BasicGameState;
import com.three_stack.digital_compass.backend.BasicPhase;
import com.three_stack.digital_compass.backend.BasicPlayer;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class VotePhase extends BasicPhase {
	public VotePhase() {
		super();
		setAction(VoteAction.class);
	}
	public BasicGameState processAction(BasicAction action, BasicGameState state) {
		GameState gameState = (GameState) state;
		VoteAction lieAction = (VoteAction) action;

		// add vote
		String votedAnswer = lieAction.getAnswer();
		String believer = lieAction.getPlayer();

		Question currentQuestion = gameState.getCurrentQuestion();
		String answer = currentQuestion.getAnswer();

		if (votedAnswer.equals(answer)) {
			currentQuestion.getBelievers().add(believer);
			gameState.incrementVoteCount();
		} else {
			//loop through all lies to find the match
			for (Lie lie : gameState.getLies()) {
				if (lie.getLie().equals(votedAnswer)) {
					lie.getBelievers().add(believer);
					gameState.incrementVoteCount();
					// stop looking for a match
					break;
				}
			}
		}
		// check if done
		if (gameState.getVoteCount() == gameState.getPlayers().size()) {
			tallyScores(gameState);
			gameState.transitionPhase(new RevealPhase());
		}
		return state;
	}

	private void tallyScores(GameState gameState) {
		final int pointsForCorrect = gameState.getCurrentInstruction().getCorrectAnswerPointValue();
		final int pointsForTrick = gameState.getCurrentInstruction().getTrickBonusPointValue();
		//give correct points
		for(String player : gameState.getCurrentQuestion().getBelievers()) {
			givePlayerPoint(gameState, player, pointsForCorrect);
		}
		//give lie points
		for(Lie lie : gameState.getLies()) {
			int points = lie.getBelievers().size() * pointsForTrick;
			givePlayerPoint(gameState, lie.getLiar(), points);
		}
	}
	
	private void givePlayerPoint(GameState gameState, String displayName, int value) {
		for(BasicPlayer player : gameState.getPlayers()) {
			if(player.getDisplayName().equals(displayName)) {
				int newScore = player.getScore() + value;
				player.setScore(newScore);
				//found so we can return
				return;
			}
		}
	}

}
