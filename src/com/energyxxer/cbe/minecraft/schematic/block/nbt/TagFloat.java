package com.energyxxer.cbe.minecraft.schematic.block.nbt;

public class TagFloat extends Tag {
	float value;
	
	public TagFloat(String name, float value) {
		super(name);
		this.value = value;
	}

	@Override
	public String toAnonymousString() {
		return value+"f";
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, toAnonymousString());
	}
}
