package com.energyxxer.craftr.minecraft;

public enum Dimension {
	NETHER(-1, "Nether"),
	OVERWORLD(0, "Overworld", "region"),
	END(1, "End");
	
	int id;
	String name;
	String folderName;
	
	Dimension(int id, String name) {
		this(id, name, "DIM" + id);
	}

	Dimension(int id, String name, String folderName) {
		this.id = id;
		this.name = name;
		this.folderName = folderName;
	}
}
