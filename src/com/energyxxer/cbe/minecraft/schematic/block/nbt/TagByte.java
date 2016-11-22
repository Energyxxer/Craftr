package com.energyxxer.cbe.minecraft.schematic.block.nbt;

public class TagByte extends Tag {
	byte value;
	
	public TagByte(String name, byte value) {
		super(name);
		this.value = value;
	}

	@Override
	public String toAnonymousString() {
		return value+"b";
	}

	@Override
	public String toString() {
		return String.format("%s:%s", name, toAnonymousString());
	}
}
