package com.ege.tottoo.tester;

import java.util.Calendar;

import com.ege.tottoo.Tottoo;
import com.ege.tottoo.helper.TottooHelper;

public class Tester {

	public static void main(String[] args) {
		int bulamadi = 0;
		for (int i = 0; i < 1000 ; i++) {
			int count = tryIt();
			System.out.println("******************************");
			if(count==0)
				System.out.println("Kasa kazandı!");
			else
				System.out.println("Kumarbaz kazandı!..Deneme Sayisi : "+count);
		}
		
		//double yuzde = ((double)bulamadi/1000);
		//System.out.println("% : "+yuzde*100 + " ihtimalle hepsi yesil olamaz");
		
	}
	
	private static int tryIt () {
		int count = 0;
		Tottoo t = null;
		Calendar now = Calendar.getInstance();
		int totalTryCount = 0;
		
		while(count<1000) {
			t = new Tottoo();
			TottooHelper.generateAllLevels(t);
			if(t.getLevel0().contains("y")
					&& t.getLevel1().contains("y")
					&& t.getLevel2().contains("y")
					&& t.getLevel3().contains("y")
					&& t.getLevel4().contains("y")
					&& t.getLevel5().contains("y")
					&& t.getLevel6().contains("y")
					&& t.getLevel7().contains("y")
					&& t.getLevel8().contains("y")
					&& t.getLevel9().contains("y"))
			{
				totalTryCount = calculateTotalTryCount(t);
				break;
			} else {
				count++;
				/*if(count%10==0) {
					System.out.println("Count Now : "+count);
					System.out.println("level0 : "+t.getLevel0());
					System.out.println("level1 : "+t.getLevel1());
					System.out.println("level2 : "+t.getLevel2());
					System.out.println("level3 : "+t.getLevel3());
					System.out.println("level4 : "+t.getLevel4());
					System.out.println("level5 : "+t.getLevel5());
					System.out.println("level6 : "+t.getLevel6());
					System.out.println("level7 : "+t.getLevel7());
					System.out.println("level8 : "+t.getLevel8());
					System.out.println("level9 : "+t.getLevel9());
					System.out.println("-------------------------------------");
				}*/
			}
		}
		
		Calendar after = Calendar.getInstance();
		
		long diff = after.getTimeInMillis()-now.getTimeInMillis();
		
		System.out.println("Count : "+count);
		System.out.println("Milli Seconds : "+diff);
		
		/*System.out.println("level0 : "+t.getLevel0());
		System.out.println("level1 : "+t.getLevel1());
		System.out.println("level2 : "+t.getLevel2());
		System.out.println("level3 : "+t.getLevel3());
		System.out.println("level4 : "+t.getLevel4());
		System.out.println("level5 : "+t.getLevel5());
		System.out.println("level6 : "+t.getLevel6());
		System.out.println("level7 : "+t.getLevel7());
		System.out.println("level8 : "+t.getLevel8());
		System.out.println("level9 : "+t.getLevel9());*/
		return totalTryCount;
	}

	private static int calculateTotalTryCount(Tottoo t) {
		int l0 = Integer.valueOf(t.getLevel0().split("-")[1]);
		int l1 = Integer.valueOf(t.getLevel1().split("-")[1]);
		int l2 = Integer.valueOf(t.getLevel2().split("-")[1]);
		int l3 = Integer.valueOf(t.getLevel3().split("-")[1]);
		int l4 = Integer.valueOf(t.getLevel4().split("-")[1]);
		int l5 = Integer.valueOf(t.getLevel5().split("-")[1]);
		int l6 = Integer.valueOf(t.getLevel6().split("-")[1]);
		int l7 = Integer.valueOf(t.getLevel7().split("-")[1]);
		int l8 = Integer.valueOf(t.getLevel8().split("-")[1]);
		int l9 = Integer.valueOf(t.getLevel9().split("-")[1]);
		
		int total = l0+l1+l2+l3+l4+l5+l6+l7+l8+l9;
		
		return total;
	}

}
