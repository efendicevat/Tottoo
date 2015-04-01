package com.ege.tottoo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	private String	identifier;
	
	private String email;
	
	private String name;

	private String nextTottoo;
	
	private Tottoo tottooList;

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

	public String getNextTottoo() {
		return nextTottoo;
	}

	public void setNextTottoo(String nextTottoo) {
		this.nextTottoo = nextTottoo;
	}

	public Tottoo getTottooList() {
		return tottooList;
	}

	public void setTottooList(Tottoo tottooList) {
		this.tottooList = tottooList;
	}
	
	
}
