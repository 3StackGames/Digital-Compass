package testGame;

import com.google.gson.annotations.Expose;
import com.three_stack.digital_compass.backend.BasicAction;

public class RevealAction extends BasicAction {
	
	@Expose
	private boolean moveOn = true;

	public boolean isMoveOn() {
		return moveOn;
	}

	public void setMoveOn(boolean moveOn) {
		this.moveOn = moveOn;
	}
}
