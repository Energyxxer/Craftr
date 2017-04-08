package com.energyxxer.craftr.minecraft.schematic.block.nbt;

import java.util.ArrayList;

public class TagCompound extends Tag {
	public ArrayList<Tag> value = new ArrayList<Tag>();
	
	public TagCompound(String name, Tag... values) {
		super(name);
		for(Tag t : values) {
			value.add(t);
		}
	}
	
	public TagCompound(String name) {
		super(name);
	}
	
	public TagCompound add(Tag tag) {
		value.add(tag);
		return this;
	}
	
	@Override
	public String toAnonymousString() {
		String o = "{";
		
		{
			String c = "";
			for(Tag t : value) {
				c += t.toString();
				c += ",";
			}
			if(c.endsWith(",")) {
				c = c.substring(0,c.length()-1);
			}
			o += c;
		}
		
		o += "}";
		return o;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, toAnonymousString());
	}
}
