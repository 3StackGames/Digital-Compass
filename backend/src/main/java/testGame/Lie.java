package testGame;

import java.util.ArrayList;

public class Lie {
	private String lie;
	private String liar;
	private ArrayList<String> believers;
	
	public Lie(String lie, String liar) {
		this.lie = lie;
		this.liar = liar;
	}

	public String getLie() {
		return lie;
	}

	public void setLie(String lie) {
		this.lie = lie;
	}

	public String getLiar() {
		return liar;
	}

	public void setLiar(String liar) {
		this.liar = liar;
	}

	public ArrayList<String> getBelievers() {
		return believers;
	}

	public void setBelievers(ArrayList<String> believers) {
		this.believers = believers;
	}
}
