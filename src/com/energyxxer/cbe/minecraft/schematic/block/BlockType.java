package com.energyxxer.cbe.minecraft.schematic.block;

import com.energyxxer.cbe.minecraft.schematic.block.nbt.TagCompound;

public class BlockType {
	
	String id;
	BlockState state;
	TagCompound nbt;
	
	public BlockType(String id, BlockState state, TagCompound nbt) {
		this.id = id.intern();
		this.state = state;
		this.nbt = nbt;
	}
	
}
