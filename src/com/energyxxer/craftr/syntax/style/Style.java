package com.energyxxer.craftr.syntax.style;

import java.awt.Color;
import java.util.HashMap;

public class Style {
	Color color = null;
	boolean bold = false;
	boolean italic = false;
	
	public Style() {}
	public Style(Color color) {
		this.color = color;
	}
	public Style(String color) {
		this.color = Color.decode(color);
	}
	public Style(Color color, boolean bold, boolean italic) {
		this.color = color;
		this.bold = bold;
		this.italic = italic;
	}
	public Style(String color, boolean bold, boolean italic) {
		this.color = Color.decode(color);
		this.bold = bold;
		this.italic = italic;
	}
	
	public Style setColor(Color color) {
		this.color = color;
		return this;
	}
	public Style setColor(String color) {
		this.color = Color.decode(color);
		return this;
	}
	public Style setBold() {
		this.bold = true;
		return this;
	}
	public Style setItalic() {
		this.italic = true;
		return this;
	}
	
	public HashMap<String, Object> getMap() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(color != null) {
			map.put("color", color);
		}
		if(bold) map.put("bold", true);
		if(italic) map.put("italic", true);
		return map;
	}
}
