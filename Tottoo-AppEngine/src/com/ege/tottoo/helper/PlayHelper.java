package com.ege.tottoo.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ege.tottoo.User;

public class PlayHelper {
	
	private static final Logger log = Logger.getLogger(PlayHelper.class.getName());
	
	public static boolean isPlayable(User user,String identifier,int currentLevel,int currentTurn) {
		boolean isPlayable = false;
		if(user.getIdentifier().equalsIgnoreCase(identifier))
		{
			if(user.getCurrentLevel()==currentLevel)
			{
				if(user.getCurrentTurn()==currentTurn) {
					isPlayable = true;
				}
			}
		}
		log.log(Level.INFO,"isPlayable : "+isPlayable);
		return isPlayable;
	}
}
