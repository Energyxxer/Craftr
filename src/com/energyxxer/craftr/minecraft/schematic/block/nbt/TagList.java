package com.energyxxer.craftr.minecraft.schematic.block.nbt;

import java.util.ArrayList;

public class TagList extends Tag {
	ArrayList<Tag> value = new ArrayList<Tag>();
	
	public TagList(String name, Tag... values) {
		super(name);
		for(Tag t : values) {
			value.add(t);
		}
	}
	
	public TagList(String name) {
		super(name);
	}
	
	public TagList add(Tag tag) {
		value.add(tag);
		return this;
	}

	@Override
	public String toAnonymousString() {
		String o = "[";
		
		{
			String c = "";
			for(Tag t : value) {
				c += t.toAnonymousString();
				c += ",";
			}
			if(c.endsWith(",")) {
				c = c.substring(0,c.length()-1);
			}
			o += c;
		}
		
		o += "]";
		return o;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, toAnonymousString());
	}

}
