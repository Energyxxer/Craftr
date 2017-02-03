package com.energyxxer.craftr.minecraft.schematic.block.nbt;

public class TagShort extends Tag {
	short value;
	
	public TagShort(String name, short value) {
		super(name);
		this.value = value;
	}

	@Override
	public String toAnonymousString() {
		return value+"s";
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, toAnonymousString());
	}
}
