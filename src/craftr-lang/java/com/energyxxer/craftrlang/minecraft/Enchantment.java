package com.energyxxer.craftrlang.minecraft;

/**
 * Every single enchantment in Minecraft as of 16w39c.
 * */
public enum Enchantment {
	PROTECTION				("Protection", 				0	),
	FIRE_PROTECTION			("Fire Protection", 		1	),
	FEATHER_FALLING			("Feather Falling", 		2	),
	BLAST_PROTECTION		("Blast Protection", 		3	),
	PROJECTILE_PROTECTION	("Projectile Protection",	4	),
	RESPIRATION				("Respiration", 			5	),
	AQUA_AFFINITY			("Aqua Affinity", 			6	),
	THORNS					("Thorns", 					7	),
	DEPTH_STRIDER			("Depth Strider", 			8	),
	FROST_WALKER			("Frost Walker", 			9	),
	CURSE_OF_BINDING		("Curse of Binding", 		10	),
	SHARPNESS				("Sharpness", 				16	),
	SMITE					("Smite", 					17	),
	BANE_OF_ARTHROPODS		("Bane of Arthropods", 		18	),
	KNOCKBACK				("Knockback", 				19	),
	FIRE_ASPECT				("Fire Aspect", 			20	),
	LOOTING					("Looting", 				21	),
	EFFICIENCY				("Efficiency", 				32	),
	SILK_TOUCH				("Silk Touch", 				33	),
	UNBREAKING				("Unbreaking", 				34	),
	FORTUNE					("Fortune", 				35	),
	POWER					("Power", 					48	),
	PUNCH					("Punch", 					49	),
	FLAME					("Flame", 					50	),
	INFINITY				("Infinity", 				51	),
	LUCK_OF_THE_SEA			("Luck of the Sea", 		61	),
	LURE					("Lure", 					62	),
	MENDING					("Mending", 				70	),
	CURSE_OF_VANISHING		("Curse of Vanishing", 		71	);

	public String id;
	public String name;
	public int numerical_id;
	Enchantment(String name, int numerical_id) {
		this.id = name().toLowerCase();
		this.name = name;
		this.numerical_id = numerical_id;
	}
}
