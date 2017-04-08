package com.energyxxer.craftr.minecraft.schematic.block.nbt;

public class TagLong extends Tag {
	long value;
	
	public TagLong(String name, long value) {
		super(name);
		this.value = value;
	}

	@Override
	public String toAnonymousString() {
		return value+"L";
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, toAnonymousString());
	}
}
