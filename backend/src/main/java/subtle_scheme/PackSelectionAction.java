package subtle_scheme;

import com.three_stack.digital_compass.backend.BasicAction;

import java.util.List;

/**
 * Created by Jason on 10/10/2015.
 */
public class PackSelectionAction extends BasicAction{
    private List<String> packs;

    public List<String> getPacks() {
        return packs;
    }

    public void setPacks(List<String> packs) {
        this.packs = packs;
    }
}
