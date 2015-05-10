package com.ege.tottoo.helper;

import java.util.Random;

import com.ege.tottoo.Tottoo;
import com.ege.tottoo.listadapter.SortedTottooList;

public class TottooHelper {

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
	
	public static void setCurrentTottooLevel(Tottoo t,int currentLevel,String levelx) {
		if(currentLevel==0) {
			t.setLevel0(levelx);
		} else if(currentLevel==1) {
			t.setLevel1(levelx);
		} else if(currentLevel==2) {
			t.setLevel2(levelx);
		} else if(currentLevel==3) {
			t.setLevel3(levelx);
		} else if(currentLevel==4) {
			t.setLevel4(levelx);
		} else if(currentLevel==5) {
			t.setLevel5(levelx);
		} else if(currentLevel==6) {
			t.setLevel6(levelx);
		} else if(currentLevel==7) {
			t.setLevel7(levelx);
		} else if(currentLevel==8) {
			t.setLevel8(levelx);
		} else if(currentLevel==9) {
			t.setLevel9(levelx);
		}
	}
	
	public static void generateAllLevels(Tottoo tottoo) {
		tottoo.setLevel0("speedupX2-1,speedupX5-2,speedupX2-3,speedupX2-6,levelup-9");//generateLevels(1,1,1,new int[]{1,2},0,0,0,10)); 
		//tottoo.setLevel0(generateLevels(1,1,1,new int[]{1,2},0,0,0,10));
		tottoo.setLevel1(generateLevels(1,1,2,new int[]{1,2},1,0,0,100));
		tottoo.setLevel2(generateLevels(1,1,3,new int[]{1,2,5},1,3,0,100)); 
		tottoo.setLevel3(generateLevels(1,1,4,new int[]{2,5,10},1,2,1,100));
		tottoo.setLevel4(generateLevels(1,1,5,new int[]{2,5,10},3,3,1,1000));
		tottoo.setLevel5(generateLevels(1,1,6,new int[]{5,10},2,3,2,1000));
		tottoo.setLevel6(generateLevels(1,1,7,new int[]{5,10},1,3,3,1000));
		tottoo.setLevel7(generateLevels(1,1,8,new int[]{5,10},0,4,3,1000));
		tottoo.setLevel8(generateLevels(1,1,9,new int[]{5,10},0,3,4,1000));
		tottoo.setLevel9(generateLevels(1,1,10,new int[]{5,10},0,5,5,10000));
		
		printTottoo(tottoo);
	}
	
	private static void printTottoo(Tottoo tottoo) {
		System.out.println("level0 : "+tottoo.getLevel0());
		System.out.println("level1 : "+tottoo.getLevel1());
		System.out.println("level2 : "+tottoo.getLevel2());
		System.out.println("level3 : "+tottoo.getLevel3());
		System.out.println("level4 : "+tottoo.getLevel4());
		System.out.println("level5 : "+tottoo.getLevel5());
		System.out.println("level6 : "+tottoo.getLevel6());
		System.out.println("level7 : "+tottoo.getLevel7());
		System.out.println("level8 : "+tottoo.getLevel8());
		System.out.println("level9 : "+tottoo.getLevel9());
		System.out.println("-------------------------------------");
	}
	
	public static void generateLevelByMinLevel(Tottoo tottoo,int level) {
		if(level==0) {
			generateAllLevels(tottoo);
		} else if(level==1) {
			tottoo.setLevel1(generateLevels(1,1,2,new int[]{1,2},1,0,0,100));
			tottoo.setLevel2(generateLevels(1,1,3,new int[]{1,2,5},1,3,0,100)); 
			tottoo.setLevel3(generateLevels(1,1,4,new int[]{2,5,10},1,2,1,100));
			tottoo.setLevel4(generateLevels(1,1,5,new int[]{2,5,10},3,3,1,1000));
			tottoo.setLevel5(generateLevels(1,1,6,new int[]{5,10},2,3,2,1000));
			tottoo.setLevel6(generateLevels(1,1,7,new int[]{5,10},1,3,3,1000));
			tottoo.setLevel7(generateLevels(1,1,8,new int[]{5,10},0,4,3,1000));
			tottoo.setLevel8(generateLevels(1,1,9,new int[]{5,10},0,3,4,1000));
			tottoo.setLevel9(generateLevels(1,1,10,new int[]{5,10},0,5,5,10000));
		} else if(level==2) {
			tottoo.setLevel2(generateLevels(1,1,3,new int[]{1,2,5},1,3,0,100)); 
			tottoo.setLevel3(generateLevels(1,1,4,new int[]{2,5,10},1,2,1,100));
			tottoo.setLevel4(generateLevels(1,1,5,new int[]{2,5,10},3,3,1,1000));
			tottoo.setLevel5(generateLevels(1,1,6,new int[]{5,10},2,3,2,1000));
			tottoo.setLevel6(generateLevels(1,1,7,new int[]{5,10},1,3,3,1000));
			tottoo.setLevel7(generateLevels(1,1,8,new int[]{5,10},0,4,3,1000));
			tottoo.setLevel8(generateLevels(1,1,9,new int[]{5,10},0,3,4,1000));
			tottoo.setLevel9(generateLevels(1,1,10,new int[]{5,10},0,5,5,10000));
		} else if(level==3) {
			tottoo.setLevel3(generateLevels(1,1,4,new int[]{2,5,10},1,2,1,100));
			tottoo.setLevel4(generateLevels(1,1,5,new int[]{2,5,10},3,3,1,1000));
			tottoo.setLevel5(generateLevels(1,1,6,new int[]{5,10},2,3,2,1000));
			tottoo.setLevel6(generateLevels(1,1,7,new int[]{5,10},1,3,3,1000));
			tottoo.setLevel7(generateLevels(1,1,8,new int[]{5,10},0,4,3,1000));
			tottoo.setLevel8(generateLevels(1,1,9,new int[]{5,10},0,3,4,1000));
			tottoo.setLevel9(generateLevels(1,1,10,new int[]{5,10},0,5,5,10000));
		} else if(level==4) {
			tottoo.setLevel4(generateLevels(1,1,5,new int[]{2,5,10},3,3,1,1000));
			tottoo.setLevel5(generateLevels(1,1,6,new int[]{5,10},2,3,2,1000));
			tottoo.setLevel6(generateLevels(1,1,7,new int[]{5,10},1,3,3,1000));
			tottoo.setLevel7(generateLevels(1,1,8,new int[]{5,10},0,4,3,1000));
			tottoo.setLevel8(generateLevels(1,1,9,new int[]{5,10},0,3,4,1000));
			tottoo.setLevel9(generateLevels(1,1,10,new int[]{5,10},0,5,5,10000));
		} else if(level==5) {
			tottoo.setLevel5(generateLevels(1,1,6,new int[]{5,10},2,3,2,1000));
			tottoo.setLevel6(generateLevels(1,1,7,new int[]{5,10},1,3,3,1000));
			tottoo.setLevel7(generateLevels(1,1,8,new int[]{5,10},0,4,3,1000));
			tottoo.setLevel8(generateLevels(1,1,9,new int[]{5,10},0,3,4,1000));
			tottoo.setLevel9(generateLevels(1,1,10,new int[]{5,10},0,5,5,10000));
		} else if(level==6) {
			tottoo.setLevel6(generateLevels(1,1,7,new int[]{5,10},1,3,3,1000));
			tottoo.setLevel7(generateLevels(1,1,8,new int[]{5,10},0,4,3,1000));
			tottoo.setLevel8(generateLevels(1,1,9,new int[]{5,10},0,3,4,1000));
			tottoo.setLevel9(generateLevels(1,1,10,new int[]{5,10},0,5,5,10000));
		} else if(level==7) {
			tottoo.setLevel7(generateLevels(1,1,8,new int[]{5,10},0,4,3,1000));
			tottoo.setLevel8(generateLevels(1,1,9,new int[]{5,10},0,3,4,1000));
			tottoo.setLevel9(generateLevels(1,1,10,new int[]{5,10},0,5,5,10000));
		} else if(level==8) {
			tottoo.setLevel8(generateLevels(1,1,9,new int[]{5,10},0,3,4,1000));
			tottoo.setLevel9(generateLevels(1,1,10,new int[]{5,10},0,5,5,10000));
		} else if(level==9) {
			tottoo.setLevel9(generateLevels(1,1,10,new int[]{5,10},0,5,5,10000));
		}
		
		printTottoo(tottoo);
	}
	
	private static String generateLevels(int levelupCount,int backstepCount,
			int speedupCount,int[] speedupList,int bonusCount,int smallTrapCount,
			int bigTrapCount,int seed) {
		String result = "";
		SortedTottooList levelupTottooList = new SortedTottooList();
		SortedTottooList bonusTottooList = new SortedTottooList();
		SortedTottooList smallTrapTottooList = new SortedTottooList();
		SortedTottooList bigTrapTottooList = new SortedTottooList();
		SortedTottooList backstepTottooList = new SortedTottooList();
		SortedTottooList speedupTottooList = new SortedTottooList();
		
		Random r = new Random();
		int templevelupi = r.nextInt(seed)+1;
		levelupTottooList.addToList(templevelupi);
		levelupCount--;
		
		while(levelupCount>0) {
			templevelupi = r.nextInt(seed)+1;
			if(!levelupTottooList.getTottooList().contains(templevelupi)){
				levelupTottooList.addToList(templevelupi);
				levelupCount--;
			}
		}

		while(bonusCount>0) {
			int tempbonusi = r.nextInt(seed)+1;
			if(!levelupTottooList.getTottooList().contains(tempbonusi)
					&& !bonusTottooList.getTottooList().contains(tempbonusi)){
				bonusTottooList.addToList(tempbonusi);
				bonusCount--;
			}
		}
		
		while(backstepCount>0) {
			int tempbackstepi = r.nextInt(seed)+1;
			if(!levelupTottooList.getTottooList().contains(tempbackstepi)
					&& !bonusTottooList.getTottooList().contains(tempbackstepi)
					&& !backstepTottooList.getTottooList().contains(tempbackstepi)){
				backstepTottooList.addToList(tempbackstepi);
				backstepCount--;
			}
		}
		
		while(smallTrapCount>0) {
			int tempsmalltrapi = r.nextInt(seed)+1;
			if(!levelupTottooList.getTottooList().contains(tempsmalltrapi)
					&& !bonusTottooList.getTottooList().contains(tempsmalltrapi)
					&& !backstepTottooList.getTottooList().contains(tempsmalltrapi)
					&& !smallTrapTottooList.getTottooList().contains(tempsmalltrapi)){
				smallTrapTottooList.addToList(tempsmalltrapi);
				smallTrapCount--;
			}
		}
		
		while(bigTrapCount>0) {
			int tempbigtrapi = r.nextInt(seed)+1;
			if(!levelupTottooList.getTottooList().contains(tempbigtrapi)
					&& !bonusTottooList.getTottooList().contains(tempbigtrapi)
					&& !backstepTottooList.getTottooList().contains(tempbigtrapi)
					&& !smallTrapTottooList.getTottooList().contains(tempbigtrapi)
					&& !bigTrapTottooList.getTottooList().contains(tempbigtrapi)){
				bigTrapTottooList.addToList(tempbigtrapi);
				bigTrapCount--;
			}
		}
		
		while(speedupCount>0) {
			int tempspeedupi = r.nextInt(seed)+1;
			if(!levelupTottooList.getTottooList().contains(tempspeedupi)
					&& !bonusTottooList.getTottooList().contains(tempspeedupi)
					&& !backstepTottooList.getTottooList().contains(tempspeedupi)
					&& !smallTrapTottooList.getTottooList().contains(tempspeedupi)
					&& !bigTrapTottooList.getTottooList().contains(tempspeedupi)
					&& !speedupTottooList.getTottooList().contains(tempspeedupi)){
				speedupTottooList.addToList(tempspeedupi);
				speedupCount--;
			}
		}
		
		
		int min = Integer.MAX_VALUE;
		int levelupi = levelupTottooList.getItem(0);
		boolean isLevelupMin = false;
		if(levelupi<min) {
			min = levelupi;
			isLevelupMin = true;
		}
		
		boolean isBonusMin = false;
		if(bonusTottooList.getSize()>0) {
			int bonusi = bonusTottooList.getItem(0);
			
			if(bonusi<min) {
				min = bonusi;
				isLevelupMin = false;
				isBonusMin = true;
			}
		}
		
		int backstepi = backstepTottooList.getItem(0);
		boolean backstepMin = false;
		if(backstepi<min) {
			min = backstepi;
			isLevelupMin = false;
			isBonusMin = false;
			backstepMin = true;
		}
		
		boolean smalltrapMin = false;
		if(smallTrapTottooList.getSize()>0) {
			int smalltrapi = smallTrapTottooList.getItem(0);
			
			if(smalltrapi<min) {
				min = smalltrapi;
				isLevelupMin = false;
				isBonusMin = false;
				backstepMin = false;
				smalltrapMin = true;
			}
		}
		
		boolean bigtrapMin = false;
		if(bigTrapTottooList.getSize()>0) {
			int bigtrapi = bigTrapTottooList.getItem(0);
			
			if(bigtrapi<min) {
				min = bigtrapi;
				isLevelupMin = false;
				isBonusMin = false;
				backstepMin = false;
				smalltrapMin = false;
				bigtrapMin = true;
			}
		}
		
		if(isLevelupMin) {
			result = getSpeedUpString(speedupTottooList,min,speedupList);
			result += "levelup-"+min;
		} else if(isBonusMin) {
			result = getSpeedUpString(speedupTottooList,min,speedupList);
			result += "bonus-"+min;
		} else if(backstepMin) {
			result = getSpeedUpString(speedupTottooList,min,speedupList);
			result += "backstep-"+min;
		} else if(smalltrapMin) {
			result = getSpeedUpString(speedupTottooList,min,speedupList);
			result += "smalltrap-"+min;
		} else if(bigtrapMin) {
			result = getSpeedUpString(speedupTottooList,min,speedupList);
			result += "bigtrap-"+min;
		}
		
		return result;
	}
	
	private static String getSpeedUpString(SortedTottooList speedupTottooList,int min,int[] speedupList) {
		String result = "";
		
		for (int i = 0; i < speedupTottooList.getSize(); i++) {
			int speedupi = speedupTottooList.getItem(i);
			if(speedupi<min) {
				int speeduplength = speedupList.length;
				Random r2 = new Random();
				int speedupSpanIndex = r2.nextInt(speeduplength);
				int speedupSpan = speedupList[speedupSpanIndex];
				result += "speedupX"+speedupSpan+"-"+speedupi+",";
			}
		}
				
		return result;
	}
	
}
