package com.energyxxer.craftr.minecraft.schematic.block.nbt;

public class TagInt extends Tag {
	int value;
	
	public TagInt(String name, int value) {
		super(name);
		this.value = value;
	}

	@Override
	public String toAnonymousString() {
		return value+"";
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, toAnonymousString());
	}
}
