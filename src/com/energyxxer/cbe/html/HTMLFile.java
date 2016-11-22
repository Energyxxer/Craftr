package com.energyxxer.cbe.html;

import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.util.ColorUtil;

public class HTMLFile {
	public String text = "";

	public HTMLFile() {
	}

	public void append(String text) {
		this.text += text;
	}

	public String getText() {
		return "<html><head><style>body {font-family:monospaced;white-space:pre-wrap;font-size:9px;color:" + ColorUtil.toCSS(Window.theme.t1) + "} a {color:rgb(0,102,204);}</style></head><body><pre>"
				+ text + "</pre></body></html>";
	}
}
