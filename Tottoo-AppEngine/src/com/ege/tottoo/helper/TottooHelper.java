package com.ege.tottoo.helper;

import java.util.Random;

import com.ege.tottoo.Tottoo;
import com.ege.tottoo.listadapter.SortedTottooList;

public class TottooHelper {

	public static void generateLevels(Tottoo tottoo) {
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
}
