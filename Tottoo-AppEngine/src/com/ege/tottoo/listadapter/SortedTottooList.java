package com.ege.tottoo.listadapter;

import java.util.ArrayList;

public class SortedTottooList implements Comparable {

	private ArrayList<Integer> tottooList = new ArrayList<Integer>();
	
	public SortedTottooList() {
		super();
	}
	
	public void addToList(int key) {
		int size = getSize();
		if(size==0) {
			getTottooList().add(key);
		} else {
			int idx = compareTo(key);
			if(idx==size)
			{
				getTottooList().add(key);
			}
			else {
				ArrayList<Integer> keyListTmp = new ArrayList<Integer>();
				for(int i=0;i<size;) {
					int tmpPojo;
					if(i==idx) {
						tmpPojo = key;
						idx = -1;
					}
					else {
						tmpPojo = getItem(i);
						i++;
					}
					keyListTmp.add(tmpPojo);
				}
				setKeyList(keyListTmp);
			}
		}
	}
	
	@Override
	public int compareTo(Object arg0) {
		int i=0;
		int size = getSize();
		int val2 = (Integer)arg0;
		for(;i<size;i++) {
			int val = getItem(i);
			if(val<val2) {
				continue;
			} else {
				break;
			}
		}
		return i;
	}
	
	public ArrayList<Integer> getTottooList() {
		return tottooList;
	}

	public void setKeyList(ArrayList<Integer> keyList) {
		this.tottooList = keyList;
	}
	
	public Integer getItem(int index) {
		return getTottooList().get(index);
	}
	
	public int getSize() {
		return getTottooList().size();
	}
}
