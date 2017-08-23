package com.energyxxer.craftrlang.minecraft;

import java.io.File;
import java.util.ArrayList;

/**
 * A long and painfully written list of all things Minecraft.
 */
public class MinecraftConstants {
	public static ArrayList<String> entities_old = new ArrayList<>();
	public static ArrayList<String> entities = new ArrayList<>();
	public static ArrayList<String> block_enums = new ArrayList<>();
	public static ArrayList<String> gamemode_enums = new ArrayList<>();
	public static ArrayList<String> effect_enums = new ArrayList<>();
	public static ArrayList<String> particle_enums = new ArrayList<>();
	public static ArrayList<String> enchantment_enums = new ArrayList<>();
	public static ArrayList<String> dimension_enums = new ArrayList<>();

	static {
		entities_old.add("Player");
		entities_old.add("Bat");
		entities_old.add("Chicken");
		entities_old.add("Cow");
		entities_old.add("MushroomCow");
		entities_old.add("Pig");
		entities_old.add("Rabbit");
		entities_old.add("Sheep");
		entities_old.add("Squid");
		entities_old.add("Villager");
		entities_old.add("Enderman");
		entities_old.add("PolarBear");
		entities_old.add("Spider");
		entities_old.add("CaveSpider");
		entities_old.add("PigZombie");
		entities_old.add("Blaze");
		entities_old.add("Creeper");
		entities_old.add("Endermite");
		entities_old.add("Ghast");
		entities_old.add("Guardian");
		entities_old.add("LavaSlime");
		entities_old.add("Shulker");
		entities_old.add("Silverfish");
		entities_old.add("Skeleton");
		entities_old.add("Slime");
		entities_old.add("Witch");
		entities_old.add("Zombie");
		entities_old.add("EntityHorse");
		entities_old.add("Ozelot");
		entities_old.add("Wolf");
		entities_old.add("VillagerGolem");
		entities_old.add("SnowMan");
		entities_old.add("EnderDragon");
		entities_old.add("WitherBoss");
		entities_old.add("Giant");
		entities_old.add("FallingSand");
		entities_old.add("PrimedTnt");
		entities_old.add("Boat");
		entities_old.add("MinecartRideable");
		entities_old.add("MinecartChest");
		entities_old.add("MinecartCommandBlock");
		entities_old.add("MinecartFurnace");
		entities_old.add("MinecartHopper");
		entities_old.add("MinecartTnt");
		entities_old.add("MinecartSpawner");
		entities_old.add("SmallFireball");
		entities_old.add("DragonFireball");
		entities_old.add("Fireball");
		entities_old.add("SpectralArrow");
		entities_old.add("Arrow");
		entities_old.add("ThrownExpBottle");
		entities_old.add("ThrownEgg");
		entities_old.add("ThrownEnderpearl");
		entities_old.add("EyeOfEnderSignal");
		entities_old.add("Snowball");
		entities_old.add("ThrownPotion");
		entities_old.add("WitherSkull");
		entities_old.add("ArmorStand");
		entities_old.add("EnderCrystal");
		entities_old.add("ItemFrame");
		entities_old.add("LeashKnot");
		entities_old.add("Painting");
		entities_old.add("XPOrb");
		entities_old.add("Item");
		entities_old.add("LightningBolt");
		entities_old.add("FireworksRocketEntity");
		entities_old.add("AreaEffectCloud");
	}

	static {
		entities.add("player");
		entities.add("bat");
		entities.add("chicken");
		entities.add("cow");
		entities.add("mooshroom");
		entities.add("pig");
		entities.add("rabbit");
		entities.add("sheep");
		entities.add("squid");
		entities.add("villager");
		entities.add("enderman");
		entities.add("polar_bear");
		entities.add("spider");
		entities.add("cave_spider");
		entities.add("zombie_pigman");
		entities.add("blaze");
		entities.add("creeper");
		entities.add("endermite");
		entities.add("ghast");
		entities.add("guardian");
		entities.add("elder_guardian");
		entities.add("magma_cube");
		entities.add("shulker");
		entities.add("silverfish");
		entities.add("skeleton");
		entities.add("stray");
		entities.add("wither_skeleton");
		entities.add("slime");
		entities.add("witch");
		entities.add("zombie");
		entities.add("husk");
		entities.add("zombie_villager");
		entities.add("horse");
		entities.add("donkey");
		entities.add("mule");
		entities.add("zombie_horse");
		entities.add("skeleton_horse");
		entities.add("ocelot");
		entities.add("wolf");
		entities.add("villager_golem");
		entities.add("snowman");
		entities.add("ender_dragon");
		entities.add("wither");
		entities.add("giant");
		entities.add("falling_block");
		entities.add("tnt");
		entities.add("boat");
		entities.add("minecart");
		entities.add("chest_minecart");
		entities.add("commandblock_minecart");
		entities.add("furnace_minecart");
		entities.add("hopper_minecart");
		entities.add("tnt_minecart");
		entities.add("spawner_minecart");
		entities.add("small_fireball");
		entities.add("dragon_fireball");
		entities.add("fireball");
		entities.add("spectral_arrow");
		entities.add("arrow");
		entities.add("xp_bottle");
		entities.add("egg");
		entities.add("ender_pearl");
		entities.add("eye_of_ender_signal");
		entities.add("snowball");
		entities.add("shulker_bullet");
		entities.add("potion");
		entities.add("wither_skull");
		entities.add("armor_stand");
		entities.add("ender_crystal");
		entities.add("item_frame");
		entities.add("leash_knot");
		entities.add("painting");
		entities.add("xp_orb");
		entities.add("item");
		entities.add("lightning_bolt");
		entities.add("fireworks_rocket");
		entities.add("area_effect_cloud");
		entities.add("evocation_illager");
		entities.add("vex");
		entities.add("vindication_illager");
		entities.add("llama");
		entities.add("llama_spit");
		entities.add("evocation_fangs");
	}

	static {
		Block[] blocks = Block.STONE.getDeclaringClass().getEnumConstants();
		for(int i = 0; i < blocks.length; i++) {
			block_enums.add(blocks[i].name());
		}
	}
	
	static {
		Gamemode[] gamemodes = Gamemode.SURVIVAL.getDeclaringClass().getEnumConstants();
		for(int i = 0; i < gamemodes.length; i++) {
			gamemode_enums.add(gamemodes[i].name());
		}
	}
	
	static {
		Effect[] effects = Effect.SPEED.getDeclaringClass().getEnumConstants();
		for(int i = 0; i < effects.length; i++) {
			effect_enums.add(effects[i].name());
		}
	}

	static {
		Particle[] particles = Particle.TAKE.getDeclaringClass().getEnumConstants();
		for(int i = 0; i < particles.length; i++) {
			particle_enums.add(particles[i].name());
		}
	}
	
	static {
		Enchantment[] enchantments = Enchantment.PROTECTION.getDeclaringClass().getEnumConstants();
		for(int i = 0; i < enchantments.length; i++) {
			enchantment_enums.add(enchantments[i].name());
		}
	}
	
	static {
		Dimension[] dimensions = Dimension.OVERWORLD.getDeclaringClass().getEnumConstants();
		for(int i = 0; i < dimensions.length; i++) {
			dimension_enums.add(dimensions[i].name());
		}
	}
	
	public static String getMinecraftDir() {
		String workingDirectory;
		// here, we assign the name of the OS, according to Java, to a
		// variable...
		String OS = (System.getProperty("os.name")).toUpperCase();
		// to determine what the workingDirectory is.
		// if it is some version of Windows
		if (OS.contains("WIN")) {
			// it is simply the location of the "AppData" folder
			workingDirectory = System.getenv("AppData");
		}
		// Otherwise, we assume Linux or Mac
		else {
			// in either case, we would start in the user's home directory
			workingDirectory = System.getProperty("user.home");
			// if we are on a Mac, we are not done, we look for "Application
			// Support"
			if(OS.contains("MAC")) workingDirectory += "/Library/Application Support";
		}

		workingDirectory += File.separator + (!OS.contains("MAC") ? "." : "") + "minecraft";

		return workingDirectory;
	}
}
