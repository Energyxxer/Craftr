package com.energyxxer.cbe.html;

import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.ColorUtil;

import java.awt.*;

public class HTMLFile {
	public String text = "";

	private String font;
	private String color;

	public HTMLFile() {
		ThemeChangeListener.addThemeChangeListener(t -> {
			font = t.getString("Console.font",t.getString("Editor.font","monospaced"));
			color = ColorUtil.toCSS(t.getColor("Console.foreground",t.getColor("General.foreground", Color.BLACK)));
		});
	}

	public void append(String text) {
		this.text += text;
	}

	public String getText() {
		return "<html><head><style>body {font-family:" + font + ";white-space:pre-wrap;font-size:9px;color:" + color + "} a {color:rgb(0,102,204);}</style></head><body><pre>"
				+ text + "</pre></body></html>";
	}
}
