package com.ege.tottoo.helper;

import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ege.tottoo.GameState;
import com.ege.tottoo.Interaction;
import com.ege.tottoo.Reload;
import com.ege.tottoo.Tottoo;
import com.ege.tottoo.User;
import com.ege.tottoo.UserEndpoint;
import com.ege.tottoo.exceptions.NotDefinedBonusException;
import com.ege.tottoo.exceptions.NotPlayableException;
import com.ege.tottoo.exceptions.TottooException;
import com.ege.tottoo.mobile.MobilePlayPlan;

public class PlayHelper {
	
	private static final Logger log = Logger.getLogger(PlayHelper.class.getName());
	
	private static final String TRYAGAIN = "TRYAGAIN";
	
	private static final String SPEEDUPX = "SPEEDUPX";
	
	private static final String WIN = "WIN";
	
	private static final String BACKLEVEL = "BACKLEVEL";
	
	private static final String BACKLEVELX2 = "2XBACKLEVEL";
	
	private static final String BACKLEVELX3 = "3XBACKLEVEL";
	
	private static final String PASSLEVEL = "PASSLEVEL";
	
	private static final String PASSLEVELX2 = "2XPASSLEVEL";
	
	private static final String GAMEOVER = "GAMEOVER";
	
	public static boolean isPlayable(User user,MobilePlayPlan mobilePlayPlan) {
		log.warning("PlayHelper.isPlayable");
		if(mobilePlayPlan.getCurrentCoin()==0)
			return false;
		int coinOnCloud = user.getRemainCoin();
		if(coinOnCloud==0)
			return false;
		boolean isPlayable = false;
		if(user.getIdentifier().equalsIgnoreCase(mobilePlayPlan.getIdentifier()))
		{
			if(user.getCurrentLevel()==mobilePlayPlan.getCurrentLevel())
			{
				if(user.getCurrentTurn()==mobilePlayPlan.getCurrentTurn()) {
					if(coinOnCloud==mobilePlayPlan.getCurrentCoin()) {
						isPlayable = true;
					}
				}
			}
		}
		log.log(Level.INFO,"isPlayable : "+isPlayable);
		return isPlayable;
	}
	
	public static GameState playTottoo(User user,MobilePlayPlan mobilePlayPlan,boolean isSpeedUp,boolean isSpeedUpFirstTurn) 
			throws TottooException {
		Interaction action = new Interaction();
		GameState gameState = new GameState();
		Calendar now = Calendar.getInstance();
		int coin = user.getRemainCoin();
		int speedupCount = user.getTotalSpeedupCount();
		if(user.getReload()==null) {
			user.setReload(ReloadHelper.initializeReload(now));
		}
		if(isSpeedUp) {
			speedupCount--;
			user.setTotalSpeedupCount(speedupCount);
		}
		action.setPlayTime(Calendar.getInstance().getTime());
		if(mobilePlayPlan.getCurrentCoin()>user.getRemainCoin()) {
			coin = ReloadHelper.calculateCoinOnCloud(user,now);
			user.setRemainCoin(coin);
		}
		boolean isPlayable = PlayHelper.isPlayable(user, mobilePlayPlan);
		if(isPlayable) {
			if(isSpeedUp)
			{
				if(isSpeedUpFirstTurn)
					coin--;
			}
			else
				coin--;
			
			user.setRemainCoin(coin);
			user.setCoinUsageTime(now.getTime());
			
			Tottoo tottooOnCloud = user.getTottooList();
			int playTurn = user.getCurrentTurn();
			int playLevel = user.getCurrentLevel();
			String currentLevelOnCloud = TottooHelper.getCurrentTottooLevel(tottooOnCloud, playLevel);
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
				
				if(speedupStrTurn==playTurn) {
					if(state.contains("speedup")) {
						String[] tmp3 = speedupStr.split("X");
						int span = Integer.valueOf(tmp3[1]);
						speedupCount +=span;
						user.setTotalSpeedupCount(speedupCount);
						TottooHelper.setCurrentTottooLevel(tottooOnCloud, playLevel, others);
						user.setTottooList(tottooOnCloud);
						gameState.setState(SPEEDUPX+span);
						playTurn++;
						user.setCurrentTurn(playTurn);
					} else {
						throw new NotDefinedBonusException("Not Defined Bonus. Option is forbidden!..");
					}
				} else {
					gameState.setState(TRYAGAIN);
					playTurn++;
					user.setCurrentTurn(playTurn);
				}
			} else { //NO BONUS
				String[] tmp = currentLevelOnCloud.split("-");
				String levelx = tmp[0];
				int currentTurnOnCloud = Integer.valueOf(tmp[1]);
				if(currentTurnOnCloud==playTurn) {
					setGameState(user,levelx,gameState,playLevel,playTurn);
				} else {
					gameState.setState(TRYAGAIN);
					playTurn++;
					user.setCurrentTurn(playTurn);
				}
			}
		} else {
			throw new NotPlayableException("Play option is forbidden!..");
		}
		action.setGameState(gameState);
		List<Interaction> interactions = user.getInteractions();
		interactions.add(action);
		user.setInteractions(interactions);
		
		return gameState;
	}
	
	private static void setGameState(User user,String levelx,GameState gameState,int currentLevel,int currentTurn) {
		if(levelx.contains("levelup")) {
			if(currentLevel==9) {
				gameState.setState(WIN);
				user.setTotalSpeedupCount(0);
			} else {
				gameState.setState(PASSLEVEL);
				currentLevel++;
				currentTurn = 1;
			}
		}
		else if(levelx.contains("bonus")) {
			if(currentLevel==9) {
				gameState.setState(WIN);
				user.setTotalSpeedupCount(0);
			} else {
				gameState.setState(PASSLEVELX2);
				currentLevel+=2;
				currentTurn = 1;
			}
		}
		else if(levelx.contains("backstep")) {
			if(currentLevel==0) {
				gameState.setState(GAMEOVER);
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
				user.setTotalSpeedupCount(0);
			} else {
				gameState.setState(BACKLEVEL);
				currentLevel--;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else if(levelx.contains("smalltrap")) {
			if(currentLevel==0) {
				gameState.setState(GAMEOVER);
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
				user.setTotalSpeedupCount(0);
			} else {
				gameState.setState(BACKLEVELX2);
				currentLevel-=2;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else if(levelx.contains("bigtrap")) {
			if(currentLevel==0) {
				gameState.setState(GAMEOVER);
				currentLevel = 0;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
				user.setTotalSpeedupCount(0);
			} else {
				gameState.setState(BACKLEVELX3);
				currentLevel-=3;
				Tottoo tottoo = new Tottoo();
				TottooHelper.generateLevelByMinLevel(tottoo,currentLevel);
				user.setTottooList(tottoo);
				currentTurn = 1;
			}
		}
		else {
			gameState.setState(TRYAGAIN); //defensive
			currentTurn++;
		}
		user.setCurrentLevel(currentLevel);
		user.setCurrentTurn(currentTurn);
	}
}
