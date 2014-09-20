package com.example.myapp;


public class Player {
	public static enum symbols {XX, OO};

	private String name;
	private symbols sym;
	private boolean isRobot = false;
	
	Player(symbols sym) {
		this.sym = sym;
		if (sym == symbols.OO) {
			name = "OO";
		}
		else {
			name = "XX";
		}
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public symbols getSym() {
		return sym;
	}
	public void setSym(symbols sym) {
		this.sym = sym;
	}

	public boolean isRobot() {
		return isRobot;
	}

	public void setRobot(boolean isRobot) {
		this.isRobot = isRobot;
	}
}
