package com.ege.tottoo.tester;

import com.ege.tottoo.GameState;
import com.ege.tottoo.Tottoo;
import com.ege.tottoo.User;
import com.ege.tottoo.exceptions.NotDefinedBonusException;
import com.ege.tottoo.helper.TottooHelper;

public class Tester {

	public static User user;
	
	public static void main(String[] args) {
		
		user = new User();
		
		tryIt();

		while(true) {
			play(user.getCurrentLevel(),user.getCurrentTurn());
		}
	}
	
	
	private static void play(int _currentLevel,int _currentTurn) {
		GameState gameState = new GameState();
		int speedupCount = 0;
		
		String currentLevelOnCloud = TottooHelper.getCurrentTottooLevel(user.getTottooList(), _currentLevel);
		
		if(currentLevelOnCloud.contains(",")) { //HAS BONUS
			String[] tmp = currentLevelOnCloud.split(",");
			String state = tmp[0];
			String[] tmp2 = state.split("-");
			String speedupStr = tmp2[0];
			int speedupStrTurn = Integer.valueOf(tmp2[1]);
			String others = "";
			for (int i = 1; i < tmp.length; i++) {
				others += tmp[i]+",";
			}
			if(others.endsWith(",")) {
				others = others.substring(0, others.length()-1);
			}
			if(speedupStrTurn==_currentTurn) {
				if(state.contains("speedup")) {
					String[] tmp3 = speedupStr.split("X");
					int span = Integer.valueOf(tmp3[1]);
					speedupCount +=span;
					user.setTotalSpeedupCount(speedupCount);
					TottooHelper.setCurrentTottooLevel(user.getTottooList(), _currentLevel, others);
					user.setTottooList(user.getTottooList());
					gameState.setState("SPEEDUPX"+span);
					_currentTurn++;
					user.setCurrentTurn(_currentTurn);
				} else {
					//throw new NotDefinedBonusException("Not Defined Bonus. Option is forbidden!..");
				}
			} else {
				gameState.setState("TRYAGAIN");
				_currentTurn++;
				user.setCurrentTurn(_currentTurn);
			}
		} else { //NO BONUS
			String[] tmp = currentLevelOnCloud.split("-");
			String levelx = tmp[0];
			int currentTurnOnCloud = Integer.valueOf(tmp[1]);
			if(currentTurnOnCloud==_currentTurn) {
				setGameState(user,levelx,gameState,_currentLevel,_currentTurn);
			} else {
				gameState.setState("TRYAGAIN");
				_currentTurn++;
				user.setCurrentTurn(_currentTurn);
			}
		}
	}
	
	
	private static void setGameState(User user,String levelx,GameState gameState,int currentLevel,int currentTurn) {
		if(levelx.contains("levelup")) {
			if(currentLevel==9) {
				gameState.setState("WIN");
			} else {
				gameState.setState("PASSLEVEL");
				currentLevel++;
				currentTurn = 1;
			}
		}
		else if(levelx.contains("bonus")) {
			if(currentLevel==9) {
				gameState.setState("WIN");
			} else {
				gameState.setState("2XPASSLEVEL");
				currentLevel+=2;
				currentTurn = 1;
			}
		}
		else if(levelx.contains("backstep")) {
			if(currentLevel==0) {
				gameState.setState("GAMEOVER");
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			} else {
				gameState.setState("BACKLEVEL");
				currentLevel--;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else if(levelx.contains("smalltrap")) {
			if(currentLevel==0) {
				gameState.setState("GAMEOVER");
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			} else {
				gameState.setState("2XBACKLEVEL");
				currentLevel-=2;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else if(levelx.contains("bigtrap")) {
			if(currentLevel==0) {
				gameState.setState("GAMEOVER");
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			} else {
				gameState.setState("3XBACKLEVEL");
				currentLevel-=3;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else {
			gameState.setState("TRYAGAIN"); //defensive
			currentTurn++;
		}
		user.setCurrentLevel(currentLevel);
		user.setCurrentTurn(currentTurn);
	}
	
	private static void tryIt () {
		Tottoo t = new Tottoo();
		TottooHelper.generateAllLevels(t);
		user.setTottooList(t);
		user.setCurrentLevel(0);
		user.setCurrentTurn(1);
		user.setTotalSpeedupCount(0);
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
