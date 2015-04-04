package com.ege.tottoo;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.google.appengine.api.datastore.Key;

@Entity
public class Interaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	private Date playTime;

	private Date nextPlayableTime;
	
	@OneToOne(cascade = CascadeType.ALL)
	private GameState gameState;
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Date getPlayTime() {
		return playTime;
	}

	public void setPlayTime(Date playTime) {
		this.playTime = playTime;
	}

	public Date getNextPlayableTime() {
		return nextPlayableTime;
	}

	public void setNextPlayableTime(Date nextPlayableTime) {
		this.nextPlayableTime = nextPlayableTime;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	
}
