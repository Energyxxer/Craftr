package com.energyxxer.craftr.minecraft;

public enum Dimension {
	NETHER(-1, "Nether"),
	OVERWORLD(0, "Overworld"),
	END(1, "End");
	
	int id;
	String name;
	
	Dimension(int id, String name) {
		this.id = id;
		this.name = name;
	}
}
