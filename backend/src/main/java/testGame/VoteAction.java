package testGame;

import com.google.gson.annotations.Expose;
import com.three_stack.digital_compass.backend.BasicAction;

/**
 * Created by Hyunbin on 9/25/15.
 */
public class VoteAction extends BasicAction {

    @Expose
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String lie) {
        this.answer = lie;
    }
}
