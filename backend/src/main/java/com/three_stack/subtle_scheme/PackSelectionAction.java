package com.three_stack.subtle_scheme;

import com.three_stack.digital_compass.backend.BasicAction;

import java.util.List;

public class PackSelectionAction extends BasicAction{
    private List<String> packs;

    public List<String> getPacks() {
        return packs;
    }

    public void setPacks(List<String> packs) {
        this.packs = packs;
    }
}
