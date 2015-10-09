package subtle_scheme;

import com.google.gson.annotations.Expose;
import com.three_stack.digital_compass.backend.BasicAction;

/**
 * Created by Hyunbin on 9/23/15.
 */
public class LieAction extends BasicAction {

    @Expose
    private String lie;

    public String getLie() {
        return lie;
    }

    public void setLie(String lie) {
        this.lie = lie;
    }
}
