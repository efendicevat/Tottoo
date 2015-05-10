package com.ege.tottoo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class Reload {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	private Date reload1;
	
	private Date reload2;
	
	private Date reload3;
	
	private Date reload4;
	
	private Date reload5;
	
	private Date reload6;
	
	private Date reload7;
	
	private Date reload8;
	
	private Date reload9;
	
	private Date reload10;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Date getReload1() {
		return reload1;
	}

	public void setReload1(Date reload1) {
		this.reload1 = reload1;
	}

	public Date getReload2() {
		return reload2;
	}

	public void setReload2(Date reload2) {
		this.reload2 = reload2;
	}

	public Date getReload3() {
		return reload3;
	}

	public void setReload3(Date reload3) {
		this.reload3 = reload3;
	}

	public Date getReload4() {
		return reload4;
	}

	public void setReload4(Date reload4) {
		this.reload4 = reload4;
	}

	public Date getReload5() {
		return reload5;
	}

	public void setReload5(Date reload5) {
		this.reload5 = reload5;
	}

	public Date getReload6() {
		return reload6;
	}

	public void setReload6(Date reload6) {
		this.reload6 = reload6;
	}

	public Date getReload7() {
		return reload7;
	}

	public void setReload7(Date reload7) {
		this.reload7 = reload7;
	}

	public Date getReload8() {
		return reload8;
	}

	public void setReload8(Date reload8) {
		this.reload8 = reload8;
	}

	public Date getReload9() {
		return reload9;
	}

	public void setReload9(Date reload9) {
		this.reload9 = reload9;
	}

	public Date getReload10() {
		return reload10;
	}

	public void setReload10(Date reload10) {
		this.reload10 = reload10;
	}
}
