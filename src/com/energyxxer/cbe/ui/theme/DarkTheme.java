package com.energyxxer.cbe.ui.theme;

import java.awt.Color;

public class DarkTheme extends Theme {
	
	private static Theme INSTANCE = new DarkTheme();
	
	private DarkTheme() {
		
		name = "Dark Theme";
		path = "dark_theme/";
		
		//Primary Colors
		p1 = new Color(50, 51, 53);
		p2 = new Color(65, 66, 68);
		p3 = new Color(60, 61, 63);
		p4 = new Color(75, 76, 78);
		p5 = null;
		p6 = null;
		p7 = null;
		p8 = null;
		//Secondary Colors
		s1 = null;
		s2 = null;
		s3 = null;
		s4 = null;
		//Text Colors
		t1 = new Color(255, 255, 255);
		t2 = null;
		t3 = new Color(125, 125, 125);
		t4 = null;
		//Line Colors
		l1 = new Color(90,90,90);
		l2 = new Color(100,105,110);
		//Button Colors
		b1 = new Color(70, 72, 74);
		b2 = new Color(55, 55, 55);
		b3 = new Color(75, 77, 79);
		b4 = new Color(55, 55, 55);
		//Gradient Colors
		g1 = new Color(255,255,255,20);
	}
	
	public static Theme getInstance() {
		return INSTANCE;
	}
}
