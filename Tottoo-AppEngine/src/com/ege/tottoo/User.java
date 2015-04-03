package com.ege.tottoo;

import javax.jdo.annotations.Persistent;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.google.appengine.api.datastore.Key;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	private String	identifier;
	
	private String email;
	
	private String name;
	
	private String currentLevel;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Tottoo tottooList;

	@OneToMany(cascade = CascadeType.ALL)
	private Interaction interaction;
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Tottoo getTottooList() {
		return tottooList;
	}

	public void setTottooList(Tottoo tottooList) {
		this.tottooList = tottooList;
	}

	public String getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(String currentLevel) {
		this.currentLevel = currentLevel;
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}
	
}
