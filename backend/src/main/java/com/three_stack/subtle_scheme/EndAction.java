package com.three_stack.subtle_scheme;

import com.three_stack.digital_compass.backend.BasicAction;

/**
 * Created by Hyunbin on 9/25/15.
 */
public class EndAction extends BasicAction {
    private boolean restart;

    public boolean isRestart() {
        return restart;
    }

    public void setRestart(boolean exit) {
        this.restart = exit;
    }
}
