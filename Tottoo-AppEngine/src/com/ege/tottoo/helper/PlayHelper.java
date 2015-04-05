package com.ege.tottoo.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ege.tottoo.User;

public class PlayHelper {
	
	private static final Logger log = Logger.getLogger(PlayHelper.class.getName());
	
	public static boolean isPlayable(User user,String identifier,int currentLevel,int currentTurn) {
		boolean isPlayable = false;
		log.log(Level.WARNING,"user.getIdentifier() : "+user.getIdentifier());
		log.log(Level.WARNING,"identifier : "+identifier);
		if(user.getIdentifier().equalsIgnoreCase(identifier))
		{
			log.log(Level.WARNING,"user.getCurrentLevel() : "+user.getCurrentLevel());
			log.log(Level.WARNING,"currentLevel : "+currentLevel);
			if(user.getCurrentLevel()==currentLevel)
			{
				log.log(Level.WARNING,"user.getCurrentTurn() : "+user.getCurrentTurn());
				log.log(Level.WARNING,"currentTurn : "+currentTurn);
				if(user.getCurrentTurn()==currentTurn) {
					isPlayable = true;
				}
			}
		}
		log.log(Level.WARNING,"isPlayable : "+isPlayable);
		return isPlayable;
	}
}
