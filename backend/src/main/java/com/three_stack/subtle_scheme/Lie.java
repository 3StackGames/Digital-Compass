package com.three_stack.subtle_scheme;

public class Lie extends Belief {
	private String lie;
	private String liar;
	
	public Lie(String lie, String liar) {
		super();
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
}
