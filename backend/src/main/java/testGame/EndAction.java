package testGame;

import com.three_stack.digital_compass.backend.BasicAction;

/**
 * Created by Hyunbin on 9/25/15.
 */
public class EndAction extends BasicAction {
    private boolean exit;

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }
}
