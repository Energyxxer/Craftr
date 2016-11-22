package com.energyxxer.cbe.minecraft;

/**
 * Every single enchantment in Minecraft as of 16w39c.
 * */
public enum Enchantment {
	PROTECTION("protection", "Protection", 0),
	FIRE_PROTECTION("fire_protection", "Fire Protection", 1),
	FEATHER_FALLING("feather_falling", "Feather Falling", 2),
	BLAST_PROTECTION("blast_protection", "Blast Protection", 3),
	PROJECTILE_PROTECTION("projectile_protection", "Projectile Protection", 4),
	RESPIRATION("respiration", "Respiration", 5),
	AQUA_AFFINITY("aqua_affinity", "Aqua Affinity", 6),
	THORNS("thorns", "Thorns", 7),
	DEPTH_STRIDER("depth_strider", "Depth Strider", 8),
	FROST_WALKER("frost_walker", "Frost Walker", 9),
	CURSE_OF_BINDING("curse_of_binding", "Curse of Binding", 10),
	SHARPNESS("sharpness", "Sharpness", 16),
	SMITE("smite", "Smite", 17),
	BANE_OF_ARTHROPODS("bane_of_arthropods", "Bane of Arthropods", 18),
	KNOCKBACK("knockback", "Knockback", 19),
	FIRE_ASPECT("fire_aspect", "Fire Aspect", 20),
	LOOTING("looting", "Looting", 21),
	EFFICIENCY("efficiency", "Efficiency", 32),
	SILK_TOUCH("silk_touch", "Silk Touch", 33),
	UNBREAKING("unbreaking", "Unbreaking", 34),
	FORTUNE("fortune", "Fortune", 35),
	POWER("power", "Power", 48),
	PUNCH("punch", "Punch", 49),
	FLAME("flame", "Flame", 50),
	INFINITY("infinity", "Infinity", 51),
	LUCK_OF_THE_SEA("luck_of_the_sea", "Luck of the Sea", 61),
	LURE("lure", "Lure", 62),
	MENDING("mending", "Mending", 70),
	CURSE_OF_VANISHING("curse_of_vanishing", "Curse of Vanishing", 71);

	public String id;
	public String name;
	public int numerical_id;
	Enchantment(String id, String name, int numerical_id) {
		this.id = id;
		this.name = name;
		this.numerical_id = numerical_id;
	}
}
