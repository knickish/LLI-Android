package com.pwny.sauruk.preptracker.m_JSON;


import java.time.LocalDateTime;

import java.time.LocalDateTime;

public class CurrentTime {
	public static int currTime(){
		int currTime = (int) (System.currentTimeMillis() / 100000L);
		return currTime;
	}

}
