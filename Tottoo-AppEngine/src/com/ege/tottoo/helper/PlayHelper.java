package com.ege.tottoo.helper;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ege.tottoo.User;
import com.ege.tottoo.UserEndpoint;

public class PlayHelper {
	
	private static final Logger log = Logger.getLogger(PlayHelper.class.getName());
	
	public static boolean isPlayable(User user,String identifier,int currentLevel,int currentTurn,int currentCoin) {
		if(currentCoin==0)
			return false;
		int coinOnCloud = calculateCoinOnCloud(user);
		if(coinOnCloud==0)
			return false;
		boolean isPlayable = false;
		if(user.getIdentifier().equalsIgnoreCase(identifier))
		{
			if(user.getCurrentLevel()==currentLevel)
			{
				if(user.getCurrentTurn()==currentTurn) {
					if(coinOnCloud==currentCoin) {
						isPlayable = true;
					}
				}
			}
		}
		log.log(Level.INFO,"isPlayable : "+isPlayable);
		return isPlayable;
	}
	
	public static int calculateCoinOnCloud(User user) {
		int coin = 0;
		coin = user.getRemainCoin();
		Calendar cal = Calendar.getInstance();
		long now = cal.getTime().getTime();
		if(user.getCoinUsageTime()==null)
			return UserEndpoint.MAX_COIN;
		long usageTime = user.getCoinUsageTime().getTime();
		long diff = now-usageTime;
		diff = diff/60000; //in minutes
		int earnedCoin = (int)(diff/UserEndpoint.MAX_COIN);
		coin +=earnedCoin;
		if(coin>10)
			coin = 10;
		return coin;
	}
}
