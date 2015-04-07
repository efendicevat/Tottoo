package com.ege.tottoo;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	
	private String phone;
	
	private String address;
	
	private int currentTurn;
	
	private int currentLevel;
	
	private int totalSpeedupCount;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Tottoo tottooList;

	@Basic(fetch = FetchType.EAGER)
	@OneToMany(cascade = CascadeType.ALL)
	private List<Interaction> interactions;
	
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public List<Interaction> getInteractions() {
		return interactions;
	}

	public void setInteractions(List<Interaction> interactions) {
		this.interactions = interactions;
	}

	public int getTotalSpeedupCount() {
		return totalSpeedupCount;
	}

	public void setTotalSpeedupCount(int totalSpeedupCount) {
		this.totalSpeedupCount = totalSpeedupCount;
	}
	
}
