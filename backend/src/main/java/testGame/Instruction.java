package testGame;

/**
 * Created by Hyunbin on 9/16/15.
 */
public class Instruction {
    private int trickBonusPointValue;
    private int correctAnswerPointValue;
    private String instructionText;

    public Instruction(int trickBonusPointValue, int correctAnswerPointValue, String instructionText){
        this.trickBonusPointValue = trickBonusPointValue;
        this.correctAnswerPointValue = correctAnswerPointValue;
        this.instructionText = instructionText;
    }
}
