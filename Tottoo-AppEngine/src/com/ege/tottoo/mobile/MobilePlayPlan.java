package com.ege.tottoo.mobile;

public class MobilePlayPlan {

	private String identifier;
	private int currentLevel;
	private int currentTurn;
	private int currentCoin;
	
	public MobilePlayPlan() {
		
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public int getCurrentCoin() {
		return currentCoin;
	}

	public void setCurrentCoin(int currentCoin) {
		this.currentCoin = currentCoin;
	}

}
