package com.energyxxer.cbe.lang;

@SuppressWarnings("unused")
public class Entity {
	private String name;
	private float maxHealth;
	private float health;

	public static void getBlockAt(float x, float y, float z) {}
	public static void isStandingOn(Block block) {}
	public static void kill() {kill(false);}
	public static void kill(boolean hideAnimation) {}
}
