package com.pwny.sauruk.preptracker.m_JSON;

import java.io.Serializable;

public class ItemType  implements Serializable {
	public String name = "";
	public int itemNumber = 0;
	public String category = null;
	public int lifetime = 0;
	
	public ItemType (String name, String category, int itemNumber, int lifetime){
		this.name = name;
		this.category = category;
		this.itemNumber = itemNumber;
		this.lifetime = lifetime;
	}
	
}
