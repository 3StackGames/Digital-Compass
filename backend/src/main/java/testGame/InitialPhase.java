package testGame;

import com.three_stack.digital_compass.backend.GameState;
import com.three_stack.digital_compass.backend.Phase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InitialPhase extends Phase {

    @Override
	public GameState processAction(JSONObject action, GameState state) {

        /*
         * player -> some action
         * InitialPhase: David -> joined
         * InstructionPhase: Screen -> listened
         * QuestionPhase: Screen -> listened
         * AnswerPhase: David -> casper;
         * VotePhase: David -> Arjun
         * ScorePhase: David -> 20 points
         */

        try {
            JSONArray playerArray = action.getJSONArray("players");
            for(int i = 0; i < playerArray.length(); i++){
                JSONObject obj = playerArray.getJSONObject(i);
                obj.getString("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        state.setCurrentPhase(new InstructionPhase());
        return state;
	}

}
