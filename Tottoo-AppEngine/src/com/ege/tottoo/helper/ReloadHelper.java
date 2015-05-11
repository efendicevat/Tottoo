package com.ege.tottoo.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import com.ege.tottoo.Reload;
import com.ege.tottoo.User;
import com.ege.tottoo.UserEndpoint;

public class ReloadHelper {
	
	private static final Logger log = Logger.getLogger(ReloadHelper.class.getName());
	
	public static int calculateCoinOnCloud(User user,Calendar now) {
		log.warning("PlayHelper.calculateCoinOnCloud");
		Reload reload = user.getReload();
		int earnedCoin = 0 ;
		if(now.getTime().after(reload.getReload10())) {
			earnedCoin = UserEndpoint.MAX_COIN;
		} else if(now.getTime().after(reload.getReload9())) {
			earnedCoin = UserEndpoint.MAX_COIN-1;
		} else if(now.getTime().after(reload.getReload8())) {
			earnedCoin = UserEndpoint.MAX_COIN-2;
		} else if(now.getTime().after(reload.getReload7())) {
			earnedCoin = UserEndpoint.MAX_COIN-3;
		} else if(now.getTime().after(reload.getReload6())) {
			earnedCoin = UserEndpoint.MAX_COIN-4;
		} else if(now.getTime().after(reload.getReload5())) {
			earnedCoin = UserEndpoint.MAX_COIN-5;
		} else if(now.getTime().after(reload.getReload4())) {
			earnedCoin = UserEndpoint.MAX_COIN-6;
		} else if(now.getTime().after(reload.getReload3())) {
			earnedCoin = UserEndpoint.MAX_COIN-7;
		} else if(now.getTime().after(reload.getReload2())) {
			earnedCoin = UserEndpoint.MAX_COIN-8;
		} else if(now.getTime().after(reload.getReload1())) {
			earnedCoin = UserEndpoint.MAX_COIN-9;
		}
		
		int coin = user.getRemainCoin();
		coin +=earnedCoin;
		if(coin>UserEndpoint.MAX_COIN)
			coin = UserEndpoint.MAX_COIN;
		
		if(coin==UserEndpoint.MAX_COIN)
			user.setReload(ReloadHelper.initializeReload(now));
		else
			ReloadHelper.updateReload(user,earnedCoin);
		return coin;
	}
	
	private static void updateReload(User user,int earnedCoin) {
		log.warning("ReloadHelper.updateReload");
		Reload reload = user.getReload();
		Date baseTime = reload.getReload1();
		if(earnedCoin==1) {
			baseTime = reload.getReload1();
		} else if(earnedCoin==2) {
			baseTime = reload.getReload2();
		} else if(earnedCoin==3) {
			baseTime = reload.getReload3();
		} else if(earnedCoin==4) {
			baseTime = reload.getReload4();
		} else if(earnedCoin==5) {
			baseTime = reload.getReload5();
		} else if(earnedCoin==6) {
			baseTime = reload.getReload6();
		} else if(earnedCoin==7) {
			baseTime = reload.getReload7();
		} else if(earnedCoin==8) {
			baseTime = reload.getReload8();
		} else if(earnedCoin==9) {
			baseTime = reload.getReload9();
		} else if(earnedCoin==10) {
			baseTime = reload.getReload10();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseTime);
		cal.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload1(cal.getTime());
		cal.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload2(cal.getTime());
		cal.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload3(cal.getTime());
		cal.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload4(cal.getTime());
		cal.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload5(cal.getTime());
		cal.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload6(cal.getTime());
		cal.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload7(cal.getTime());
		cal.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload8(cal.getTime());
		cal.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload9(cal.getTime());
		cal.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload10(cal.getTime());
		
		user.setReload(reload);
	}

	public static Reload initializeReload(Calendar now) {
		log.warning("ReloadHelper.initializeReload");
		Reload reload = new Reload();
		now.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload1(now.getTime());
		now.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload2(now.getTime());
		now.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload3(now.getTime());
		now.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload4(now.getTime());
		now.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload5(now.getTime());
		now.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload6(now.getTime());
		now.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload7(now.getTime());
		now.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload8(now.getTime());
		now.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload9(now.getTime());
		now.add(Calendar.MINUTE, UserEndpoint.COIN_RELOAD_MINUTE);
		reload.setReload10(now.getTime());
		return reload;
	}
}
