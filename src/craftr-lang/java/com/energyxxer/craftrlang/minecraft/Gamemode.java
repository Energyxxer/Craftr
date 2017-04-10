package com.energyxxer.craftrlang.minecraft;

public enum Gamemode {
	SURVIVAL(0, "survival"),
	CREATIVE(1, "creative"),
	ADVENTURE(2, "adventure"),
	SPECTATOR(3, "spectator");
	
	int id;
	String name;
	
	Gamemode(int id, String name) {
		this.id = id;
		this.name = name;
	}
}
