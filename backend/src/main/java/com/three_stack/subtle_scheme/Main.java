package com.three_stack.subtle_scheme;

import com.three_stack.digital_compass.backend.PhaseManager;

public class Main {
    public static void main(String args[]){
        System.out.println("Starting Subtle Scheme v1e-09");

        PhaseManager phaseManager = new PhaseManager();
        String bridgeAddressPort = Config.getProperty("bridge.address") + ":" + Config.getProperty("bridge.port");
        phaseManager.initialize(bridgeAddressPort, new GameStateFactory());
    }
}
