package com.energyxxer.craftr.minecraft;

public enum Effect {
	SPEED(1, "speed"),
	SLOWNESS(2, "slowness"),
	HASTE(3, "haste"),
	MINING_FATIGUE(4, "mining_fatigue"),
	STRENGTH(5, "strength"),
	INSTANT_HEALTH(6, "instant_health"),
	INSTANT_DAMAGE(7, "instant_damage"),
	JUMP_BOOST(8, "jump_boost"),
	NAUSEA(9, "nausea"),
	REGENERATION(10, "regeneration"),
	RESISTANCE(11, "resistance"),
	FIRE_RESISTANCE(12, "fire_resistance"),
	WATER_BREATHING(13, "water_breathing"),
	INVISIBILITY(14, "invisibility"),
	BLINDNESS(15, "blindness"),
	NIGHT_VISION(16, "night_vision"),
	HUNGER(17, "hunger"),
	WEAKNESS(18, "weakness"),
	POISON(19, "poison"),
	WITHER(20, "wither"),
	HEALTH_BOOST(21, "health_boost"),
	ABSORPTION(22, "absorption"),
	SATURATION(23, "saturation"),
	GLOWING(24, "glowing"),
	LEVITATION(25, "levitation"),
	LUCK(1, "luck"),
	BAD_LUCK(1, "bad_luck");
	
	int numerical_id;
	String id;
	
	Effect(int numerical_id, String id) {
		this.numerical_id = numerical_id;
		this.id = id;
	}
}
