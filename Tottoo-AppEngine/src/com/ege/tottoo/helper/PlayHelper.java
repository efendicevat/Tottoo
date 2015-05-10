package com.ege.tottoo.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ege.tottoo.Interaction;
import com.ege.tottoo.User;
import com.ege.tottoo.UserEndpoint;

public class PlayHelper {
	
	private static final Logger log = Logger.getLogger(PlayHelper.class.getName());
	
	public static boolean isPlayable(User user,String identifier,int currentLevel,int currentTurn,int currentCoin) {
		if(currentCoin==0)
			return false;
		int coinOnCloud = user.getRemainCoin();
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
		int maxcoin = user.getMaxCoin();
		int needcoin = maxcoin-coin;
		if(needcoin==0)
			return UserEndpoint.MAX_COIN;
		Calendar cal = Calendar.getInstance();
		long now = cal.getTime().getTime();
		int size = user.getInteractions().size();
		if(size==0)
			return UserEndpoint.MAX_COIN;
		Interaction action = user.getInteractions().get(size-needcoin);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(cal.getTime());
		log.warning("now_date : " + str);
		str = sdf.format(action.getPlayTime().getTime());
		log.warning("last_interaction_date : " + str);
		long usageTime = action.getPlayTime().getTime();
		log.warning("now_long : " + now);
		log.warning("last_interaction_long : " + usageTime);
		long diff = now-usageTime;
		diff = diff/60000; //in minutes
		int earnedCoin = (int)(diff/UserEndpoint.COIN_RELOAD_MINUTE);
		coin +=earnedCoin;
		if(coin>UserEndpoint.MAX_COIN)
			coin = UserEndpoint.MAX_COIN;
		return coin;
	}
}
