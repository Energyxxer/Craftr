package com.energyxxer.craftr.minecraft.schematic.block.nbt;

public class TagDouble extends Tag {
	double value;
	
	public TagDouble(String name, double value) {
		super(name);
		this.value = value;
	}

	@Override
	public String toAnonymousString() {
		return value+"d";
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, toAnonymousString());
	}
}
