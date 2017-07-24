package com.energyxxer.craftrlang.minecraft.schematic.block;

import com.energyxxer.util.Pair;

import java.util.ArrayList;

public class BlockState {
	private ArrayList<Pair<String, String>> states = new ArrayList<>();
	
	public BlockState(String raw) {
		String[] params = raw.split(",");
		for(String p : params) {
			if(p.contains("=")) {
				String key = p.substring(0,p.indexOf("="));
				String value = p.substring(p.indexOf("=")+1);
				
				this.states.add(new Pair<>(key,value));
			} else {
				throw new RuntimeException("Error at parameter \"" + p + "\": Name-value separator not found.");
			}
		}
	}
	
	public BlockState() {}
	
	@SafeVarargs
	public BlockState(Pair<String, String>... s) {
		for(Pair<String, String> p : s) {
			states.add(p);
		}
	}
	
	@Override
	public String toString() {
		String o = "";
		for(Pair<String, String> p : states) {
			o += p.toString();
			o += ",";
		}
		if(o.endsWith(",")) {
			o = o.substring(0, o.length()-1);
		}
		return o;
	}
}
