package com.ege.tottoo.helper;

import com.ege.tottoo.User;

public class PlayHelper {
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
		return isPlayable;
	}
}
