package com.pwny.sauruk.preptracker.m_JSON;

import java.util.Comparator;

public class Label extends ItemType{
	
	public int uId = 0;
	public int initTime = 0;
	public String note = "";
	public int expiresAt = 0;
	
	public Label(String name, String category, int itemNumber, int lifetime, int uId, int initTime) {
		super(name, category, itemNumber, lifetime);
		this.uId = uId;
		this.initTime = initTime;
		this.expiresAt = initTime + lifetime*60;
	}
	
	public void setNote (String note){
		this.note = note;
	}
	@Override
	public String toString(){
		String returnVal = new String("UID = "+ Integer.toString(uId) + "  Item Number = " + Integer.toString(itemNumber));
		return returnVal;
	}

}
