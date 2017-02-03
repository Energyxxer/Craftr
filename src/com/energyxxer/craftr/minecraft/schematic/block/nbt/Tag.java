package com.energyxxer.craftr.minecraft.schematic.block.nbt;

public abstract class Tag {
	protected final String name;
	
	public Tag(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract String toAnonymousString();
	public abstract String toString();
}
