package com.energyxxer.cbe.ui.theme;

import java.awt.Color;

public class DarkTheme extends Theme {
	
	private static Theme INSTANCE = new DarkTheme();
	
	private DarkTheme() {
		super("CBE Dark");

		put("Assets.path","dark_theme/");

		put("Syntax","Dark");

		put("General.font","Tahoma");
		put("General.foreground",new Color(187, 187, 187));

		put("Window.background",new Color(59, 61, 64));

		put("Tooltip.background",new Color(49, 51, 54));
		put("Tooltip.foreground",new Color(187, 187, 187));
		put("Tooltip.border",new Color(40,40,42));

		put("Editor.font","monospaced");
		put("Editor.background",new Color(47, 48, 50));
		put("Editor.foreground",new Color(187, 187, 187));
		put("Editor.currentLine.background",new Color(52, 53, 55));

		put("Editor.selection.background",new Color(50, 87, 175));
		put("Editor.selection.foreground",new Color(187, 187, 187));

		put("Editor.lineNumber.background",new Color(57, 58, 60));
		put("Editor.lineNumber.foreground",new Color(125, 125, 125));
		put("Editor.lineNumber.border",new Color(40,40,42));



		put("Explorer.header.foreground",new Color(187, 187, 187));
		put("Explorer.background",new Color(49, 51, 54));
		put("Explorer.border",new Color(40,40,42));

		put("Explorer.label.foreground",new Color(187, 187, 187));
		put("Explorer.label.hover.background",new Color(59, 61, 64));
		put("Explorer.label.hover.border",new Color(40,40,42));
		put("Explorer.label.selected.background",new Color(64, 66, 69));
		put("Explorer.label.selected.border",new Color(40,40,42));



		put("MenuBar.background",new Color(59, 61, 64));
		put("MenuBar.border",new Color(90,90,90));

		put("General.menu.background",new Color(59, 61, 64));
		put("General.menu.foreground",new Color(187, 187, 187));
		put("General.menu.border",new Color(40,40,42));
		put("General.menu.selected.background",new Color(52, 53, 55));
		put("General.menu.separator",new Color(90,90,90));

		put("Toolbar.background",new Color(64, 66, 69));
		put("Toolbar.border",new Color(40,40,42));

		put("Toolbar.separator.light",new Color(90,90,90));
		put("Toolbar.separator.dark",new Color(64, 66, 69));

		put("TabList.background",new Color(74, 76, 79));
		put("TabList.border",new Color(40,40,42));

		put("Tab.foreground",new Color(187, 187, 187));
		put("Tab.background",new Color(74, 76, 79));
		put("Tab.border",new Color(40,40,42));
		put("Tab.hover.background",new Color(47, 48, 50));
		put("Tab.hover.border",new Color(40,40,42));
		put("Tab.selected.background",new Color(47, 48, 50));
		put("Tab.selected.border",new Color(40,40,42));

		put("Console.background",new Color(49, 51, 54));
		put("Console.foreground",new Color(187, 187, 187));
		put("Console.error",new Color(255,100,100));
		put("Console.warning",new Color(255, 171, 59));
		put("Console.font","monospaced");
		put("Console.header.background",new Color(64, 66, 69));
		put("Console.header.foreground",new Color(187, 187, 187));
		put("Console.header.border",new Color(40,40,42));

		/*
		* Dialogs
		* */

		put("ProjectProperties.background",new Color(64, 66, 69));
		put("ProjectProperties.list.background",new Color(49, 51, 54));
		put("ProjectProperties.list.border",new Color(40, 40, 42));
		put("ProjectProperties.list.cell.background",new Color(59, 61, 64));
		put("ProjectProperties.list.cell.foreground",new Color(187, 187, 187));
		put("ProjectProperties.list.cell.selected.background",new Color(64, 66, 69));
		put("ProjectProperties.list.cell.selected.border",new Color(40, 40, 42));
		put("ProjectProperties.list.cell.hover.background",new Color(64, 66, 69));
		put("ProjectProperties.list.cell.hover.border",new Color(40, 40, 42));

		put("ProjectProperties.content.background",new Color(64, 66, 69));
		put("ProjectProperties.content.header.background",new Color(64, 66, 69));
		put("ProjectProperties.content.header.foreground",new Color(187, 187, 187));
		put("ProjectProperties.content.header.border",new Color(40,40,42));

		put("ProjectProperties.content.label.foreground",new Color(187, 187, 187));

		put("General.textfield.background",new Color(59, 61, 64));
		put("General.textfield.foreground",new Color(187, 187, 187));
		put("General.textfield.border",new Color(40,40,42));
		put("General.textfield.selection.background",new Color(50, 87, 175));

		put("General.button.background",new Color(59, 61, 64));
		put("General.button.foreground",new Color(187, 187, 187));
		put("General.button.border",new Color(40,40,42));
		put("General.button.hover.background",new Color(74, 76, 79));
		put("General.button.pressed.background",new Color(49, 51, 54));
/*
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
		//CBEEditor Colors
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
		err0 = new Color(255,100,100);*/
	}
	
	public static Theme getInstance() {
		return INSTANCE;
	}
}
