package com.energyxxer.cbe.ui.theme;

import java.awt.Color;

public class DarkTheme extends Theme {
	
	private static Theme INSTANCE = new DarkTheme();
	
	private DarkTheme() {
		
		name = "Dark Theme";
		path = "dark_theme/";

		//Fonts
		font1 = "Tahoma";
		font2 = "monospaced";
		//Primary Colors
		p1 = new Color(49, 51, 54);
		p2 = new Color(64, 66, 69);
		p3 = new Color(59, 61, 64);
		p4 = new Color(74, 76, 79);
		p5 = null;
		p6 = null;
		p7 = null;
		p8 = null;
		//Secondary Colors
		s1 = null;
		s2 = null;
		s3 = null;
		s4 = null;
		//Editor Colors
		e1 = new Color(47, 48, 50);
		e2 = new Color(57, 58, 60);
		e3 = new Color(47, 48, 50);
		//Highlighting Colors
		h1 = new Color(52, 53, 55);
		h2 = new Color(52, 53, 55);
		h3 = new Color(50, 87, 175);
		h4 = new Color(187, 187, 187);
		//Text Colors
		t1 = new Color(187, 187, 187);
		t2 = null;
		t3 = new Color(125, 125, 125);
		t4 = null;
		//Line Colors
		l1 = new Color(40,40,42);
		l2 = new Color(90,90,90);
		l3 = new Color(40,40,40);
		//Button Colors
		b1 = new Color(70, 72, 74);
		b2 = new Color(55, 55, 55);
		b3 = new Color(75, 77, 79);
		b4 = new Color(55, 55, 55);
		//Gradient Colors
		g1 = new Color(255,255,255,20);

		//Error Text Colors
		err0 = new Color(255,100,100);
	}
	
	public static Theme getInstance() {
		return INSTANCE;
	}
}
