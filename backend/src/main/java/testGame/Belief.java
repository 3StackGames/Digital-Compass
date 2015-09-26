package testGame;

import java.util.ArrayList;

public abstract class Belief {
	protected ArrayList<String> believers;
	
	public Belief() {
		believers = new ArrayList<>();
	}
	public ArrayList<String> getBelievers() {
		return believers;
	}

	public void setBelievers(ArrayList<String> believers) {
		this.believers = believers;
	}
}
