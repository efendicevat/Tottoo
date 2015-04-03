package com.ege.tottoo;

import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ege.tottoo.helper.TottooHelper;
import com.google.appengine.api.datastore.Key;

@Entity
public class Tottoo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;
	
	private String level0 = "";
	
	private String level1 = "";

	private String level2 = "";
	
	private String level3 = "";
	
	private String level4 = "";
	
	private String level5 = "";
	
	private String level6 = "";
	
	private String level7 = "";
	
	private String level8 = "";
	
	private String level9 = "";
	
	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getLevel0() {
		return level0;
	}

	public void setLevel0(String level0) {
		this.level0 = level0;
	}

	public String getLevel1() {
		return level1;
	}

	public void setLevel1(String level1) {
		this.level1 = level1;
	}

	public String getLevel2() {
		return level2;
	}

	public void setLevel2(String level2) {
		this.level2 = level2;
	}

	public String getLevel3() {
		return level3;
	}

	public void setLevel3(String level3) {
		this.level3 = level3;
	}

	public String getLevel4() {
		return level4;
	}

	public void setLevel4(String level4) {
		this.level4 = level4;
	}

	public String getLevel5() {
		return level5;
	}

	public void setLevel5(String level5) {
		this.level5 = level5;
	}

	public String getLevel6() {
		return level6;
	}

	public void setLevel6(String level6) {
		this.level6 = level6;
	}

	public String getLevel7() {
		return level7;
	}

	public void setLevel7(String level7) {
		this.level7 = level7;
	}

	public String getLevel8() {
		return level8;
	}

	public void setLevel8(String level8) {
		this.level8 = level8;
	}

	public String getLevel9() {
		return level9;
	}

	public void setLevel9(String level9) {
		this.level9 = level9;
	}
	
}
