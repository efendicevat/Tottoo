package com.ege.tottoo.tester;

import com.ege.tottoo.GameState;
import com.ege.tottoo.Tottoo;
import com.ege.tottoo.helper.TottooHelper;

public class Tester {

	public static Tottoo t;
	
	public static int currentLevel = 0;
	
	public static int currentTurn = 1;
	
	public static void main(String[] args) {
		tryIt();

		while(true) {
			play(currentLevel,currentTurn);
		}
	}
	
	private static void findSpeedup(String levelx,int _currentLevel,int _currentTurn,GameState gameState) {
		String[] temp = levelx.split(",");
		int levelTurn = 0;
		boolean isSpeedupFound = false;
		int speedUpSpan = 0;
		boolean isSpeedupExpired = true;
		for (int i = 0; i < temp.length-1; i++) {
			String speedup = temp[i];
			levelTurn = Integer.valueOf(speedup.split("-")[1]);
			if(levelTurn==_currentTurn) {
				isSpeedupFound = true;
				speedUpSpan = Integer.valueOf(speedup.split("-")[0].split("X")[1]);
				isSpeedupExpired = false;
				break;
			} else if(_currentTurn<levelTurn) {
				isSpeedupExpired = false;
			}
		}
		if(isSpeedupFound) {
			boolean isFinalStateFound = false;
			for (int i = 0; i < speedUpSpan; i++) {
				_currentTurn++;
				String finalState = temp[temp.length-1];
				levelTurn = Integer.valueOf(finalState.split("-")[1]);
				if(levelTurn==_currentTurn) {
					isFinalStateFound = true;
					setGameState(levelx,gameState,_currentLevel,_currentTurn);
					break;
				}
			}
			if(!isFinalStateFound) {
				gameState.setState("SPEEDUPX"+speedUpSpan);
				currentTurn = _currentTurn;
			}
		} else if(isSpeedupExpired) {
			String finalState = temp[temp.length-1];
			levelTurn = Integer.valueOf(finalState.split("-")[1]);
			if(levelTurn==_currentTurn) {
				setGameState(levelx,gameState,_currentLevel,_currentTurn);
			} else {
				_currentTurn++;
				currentTurn = _currentTurn;
			}
		} else {
			gameState.setState("TRYAGAIN"); //defensive
			_currentTurn++;
			currentTurn = _currentTurn;
		}
	}
	
	private static void play(int _currentLevel,int _currentTurn) {
		String levelx = TottooHelper.getCurrentTottooLevel(t, _currentLevel);
		GameState gameState = new GameState();
		String[] temp = null;
		int levelTurn = 0;
		if(levelx.contains("speedupX"))
		{
			findSpeedup(levelx, _currentLevel, _currentTurn, gameState);
		}
		else {
			temp = levelx.split("-");
			levelTurn = Integer.valueOf(temp[1]);
			
			if(levelTurn==_currentTurn) {
				setGameState(levelx,gameState,_currentLevel,_currentTurn);
			}
			else {
				gameState.setState("TRYAGAIN");
				_currentTurn++;
				currentTurn = _currentTurn;
			}
		}
		
	}
	
	private static void setGameState(String levelx,GameState gameState,int _currentLevel,int _currentTurn) {
		if(levelx.contains("levelup")) {
			if(_currentLevel==9) {
				gameState.setState("WIN");
			} else {
				gameState.setState("PASSLEVEL");
				_currentLevel++;
				_currentTurn = 1;
			}
		}
		else if(levelx.contains("bonus")) {
			if(_currentLevel==9) {
				gameState.setState("WIN");
			} else {
				gameState.setState("2XPASSLEVEL");
				_currentLevel+=2;
				_currentTurn = 1;
			}
		}
		else if(levelx.contains("backstep")) {
			if(_currentLevel==0) {
				gameState.setState("GAMEOVER");
				_currentLevel = 0;
				t = new Tottoo();
				TottooHelper.generateLevelByMinLevel(t,_currentLevel);
				//user.setTottooList(tottoo);
				_currentTurn = 1;
			} else {
				gameState.setState("BACKLEVEL");
				_currentLevel--;
				t = new Tottoo();
				TottooHelper.generateLevelByMinLevel(t,_currentLevel);
				//user.setTottooList(tottoo);
				_currentTurn = 1;
			}
		}
		else if(levelx.contains("smalltrap")) {
			if(_currentLevel==0) {
				gameState.setState("GAMEOVER");
				_currentLevel = 0;
				t = new Tottoo();
				TottooHelper.generateLevelByMinLevel(t,_currentLevel);
				//user.setTottooList(tottoo);
				_currentTurn = 1;
			} else {
				gameState.setState("2XBACKLEVEL");
				_currentLevel-=2;
				t = new Tottoo();
				TottooHelper.generateLevelByMinLevel(t,_currentLevel);
				//user.setTottooList(tottoo);
				_currentTurn = 1;
			}
		}
		else if(levelx.contains("bigtrap")) {
			if(_currentLevel==0) {
				gameState.setState("GAMEOVER");
				_currentLevel = 0;
				t = new Tottoo();
				TottooHelper.generateLevelByMinLevel(t,_currentLevel);
				//user.setTottooList(tottoo);
				_currentTurn = 1;
			} else {
				gameState.setState("3XBACKLEVEL");
				_currentLevel-=3;
				t = new Tottoo();
				TottooHelper.generateLevelByMinLevel(t,_currentLevel);
				//user.setTottooList(tottoo);
				_currentTurn = 1;
			}
		}
		else {
			gameState.setState("TRYAGAIN"); //defensive
			_currentTurn++;
		}
		currentLevel = _currentLevel;
		currentTurn = _currentTurn;
	}
	
	private static void tryIt () {
		t = new Tottoo();
		TottooHelper.generateAllLevels(t);
	}

	private static int calculateTotalTryCount(Tottoo t) {
		int l0 = Integer.valueOf(t.getLevel0().split("-")[1]);
		int l1 = Integer.valueOf(t.getLevel1().split("-")[1]);
		int l2 = Integer.valueOf(t.getLevel2().split("-")[1]);
		int l3 = Integer.valueOf(t.getLevel3().split("-")[1]);
		int l4 = Integer.valueOf(t.getLevel4().split("-")[1]);
		int l5 = Integer.valueOf(t.getLevel5().split("-")[1]);
		int l6 = Integer.valueOf(t.getLevel6().split("-")[1]);
		int l7 = Integer.valueOf(t.getLevel7().split("-")[1]);
		int l8 = Integer.valueOf(t.getLevel8().split("-")[1]);
		int l9 = Integer.valueOf(t.getLevel9().split("-")[1]);
		
		int total = l0+l1+l2+l3+l4+l5+l6+l7+l8+l9;
		
		return total;
	}

}
