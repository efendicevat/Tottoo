package com.ege.tottoo.helper;

import java.util.Random;

import com.ege.tottoo.Tottoo;
import com.ege.tottoo.listadapter.SortedTottooList;

public class TottooHelper {

	public static void generateAllLevels(Tottoo tottoo) {
		tottoo.setLevel0(generateLevels(1,1,10)); 
		tottoo.setLevel1(generateLevels(1,1,100));
		tottoo.setLevel2(generateLevels(1,1,100)); 
		tottoo.setLevel3(generateLevels(1,1,1000));
		tottoo.setLevel4(generateLevels(1,1,1000));
		tottoo.setLevel5(generateLevels(1,1,1000));
		tottoo.setLevel6(generateLevels(1,1,1000));
		tottoo.setLevel7(generateLevels(1,1,10000));
		tottoo.setLevel8(generateLevels(1,1,10000));
		tottoo.setLevel9(generateLevels(1,1,10000));
	}
	
	public static void generateLevelByMinLevel(Tottoo tottoo,int level) {
		if(level==0) {
			generateAllLevels(tottoo);
		} else if(level==1) {
			tottoo.setLevel1(generateLevels(1,1,100));
			tottoo.setLevel2(generateLevels(1,1,100)); 
			tottoo.setLevel3(generateLevels(1,1,1000));
			tottoo.setLevel4(generateLevels(1,1,1000));
			tottoo.setLevel5(generateLevels(1,1,1000));
			tottoo.setLevel6(generateLevels(1,1,1000));
			tottoo.setLevel7(generateLevels(1,1,10000));
			tottoo.setLevel8(generateLevels(1,1,10000));
			tottoo.setLevel9(generateLevels(1,1,10000));
		} else if(level==2) {
			tottoo.setLevel2(generateLevels(1,1,100)); 
			tottoo.setLevel3(generateLevels(1,1,1000));
			tottoo.setLevel4(generateLevels(1,1,1000));
			tottoo.setLevel5(generateLevels(1,1,1000));
			tottoo.setLevel6(generateLevels(1,1,1000));
			tottoo.setLevel7(generateLevels(1,1,10000));
			tottoo.setLevel8(generateLevels(1,1,10000));
			tottoo.setLevel9(generateLevels(1,1,10000));
		} else if(level==3) {
			tottoo.setLevel3(generateLevels(1,1,1000));
			tottoo.setLevel4(generateLevels(1,1,1000));
			tottoo.setLevel5(generateLevels(1,1,1000));
			tottoo.setLevel6(generateLevels(1,1,1000));
			tottoo.setLevel7(generateLevels(1,1,10000));
			tottoo.setLevel8(generateLevels(1,1,10000));
			tottoo.setLevel9(generateLevels(1,1,10000));
		} else if(level==4) {
			tottoo.setLevel4(generateLevels(1,1,1000));
			tottoo.setLevel5(generateLevels(1,1,1000));
			tottoo.setLevel6(generateLevels(1,1,1000));
			tottoo.setLevel7(generateLevels(1,1,10000));
			tottoo.setLevel8(generateLevels(1,1,10000));
			tottoo.setLevel9(generateLevels(1,1,10000));
		} else if(level==5) {
			tottoo.setLevel5(generateLevels(1,1,1000));
			tottoo.setLevel6(generateLevels(1,1,1000));
			tottoo.setLevel7(generateLevels(1,1,10000));
			tottoo.setLevel8(generateLevels(1,1,10000));
			tottoo.setLevel9(generateLevels(1,1,10000));
		} else if(level==6) {
			tottoo.setLevel6(generateLevels(1,1,1000));
			tottoo.setLevel7(generateLevels(1,1,10000));
			tottoo.setLevel8(generateLevels(1,1,10000));
			tottoo.setLevel9(generateLevels(1,1,10000));
		} else if(level==7) {
			tottoo.setLevel7(generateLevels(1,1,10000));
			tottoo.setLevel8(generateLevels(1,1,10000));
			tottoo.setLevel9(generateLevels(1,1,10000));
		} else if(level==8) {
			tottoo.setLevel8(generateLevels(1,1,10000));
			tottoo.setLevel9(generateLevels(1,1,10000));
		} else if(level==9) {
			tottoo.setLevel9(generateLevels(1,1,10000));
		}
	}
	
	private static String generateLevels(int yCount,int kCount,int seed) {
		String result = "";
		SortedTottooList yTottooList = new SortedTottooList();
		SortedTottooList kTottooList = new SortedTottooList();
		
		Random r = new Random();
		int tempyi = r.nextInt(seed)+1;
		yTottooList.addToList(tempyi);
		yCount--;
		
		while(yCount>0) {
			tempyi = r.nextInt(seed)+1;
			if(!yTottooList.getTottooList().contains(tempyi)){
				yTottooList.addToList(tempyi);
				yCount--;
			}
		}

		while(kCount>0) {
			int tempki = r.nextInt(seed)+1;
			if(!yTottooList.getTottooList().contains(tempki)
					&& !kTottooList.getTottooList().contains(tempki)){
				kTottooList.addToList(tempki);
				kCount--;
			}
		}
		
		int yi = yTottooList.getItem(0);
		int ki = kTottooList.getItem(0);

		
		
		if(yi<ki) {
			result = "y-"+yi;
		} else {
			result = "k-"+ki;
		}
	
		return result;
	}
	
	public static String getCurrentTottooLevel(Tottoo t,int currentLevel) {
		String levelx = "";
		if(currentLevel==0) {
			levelx = t.getLevel0();
		} else if(currentLevel==1) {
			levelx = t.getLevel1();
		} else if(currentLevel==2) {
			levelx = t.getLevel2();
		} else if(currentLevel==3) {
			levelx = t.getLevel3();
		} else if(currentLevel==4) {
			levelx = t.getLevel4();
		} else if(currentLevel==5) {
			levelx = t.getLevel5();
		} else if(currentLevel==6) {
			levelx = t.getLevel6();
		} else if(currentLevel==7) {
			levelx = t.getLevel7();
		} else if(currentLevel==8) {
			levelx = t.getLevel8();
		} else if(currentLevel==9) {
			levelx = t.getLevel9();
		}
		return levelx;
	}
}
