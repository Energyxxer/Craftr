package com.energyxxer.craftr.minecraft;

public abstract class Entity {
	public String name;
	public float maxHealth;
	public float health;

	public static void getBlockAt(float x, float y, float z) {}

	public static void isStandingOn(Block block) {}

	public static void kill() {
		kill(false);
	}

	public static void kill(boolean hideAnimation) {}
}
