package com.energyxxer.craftr.minecraft.schematic.block;

import com.energyxxer.craftr.util.Point3D;

public class Block {
	public BlockType type;
	public Point3D pos;
	
	public Block(BlockType type, Point3D pos) {
		this.type = type;
		this.pos = pos;
	}
}
