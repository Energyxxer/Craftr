package com.energyxxer.craftr.compile.parsing;

import com.energyxxer.craftr.compile.analysis.token.Token;
import com.energyxxer.craftr.compile.analysis.token.TokenType;
import com.energyxxer.craftr.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.craftr.compile.parsing.classes.units.CraftrEntity;
import com.energyxxer.craftr.minecraft.MinecraftConstants;
import com.energyxxer.craftr.util.vprimitives.VInteger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class EntityRegistry implements Iterable<CraftrEntity> {
	private HashMap<String, CraftrEntity> entities = new HashMap<>();
	
	public void add(CraftrEntity e) {
		entities.put(e.getName(),e);
	}
	
	public boolean close() {
		Iterator<String> it = entities.keySet().iterator();
		while(it.hasNext()) {
			CraftrEntity e = entities.get(it.next());
			if(e.entityExtends == null) {
				continue;
			}
			if(entities.containsKey(e.entityExtends)) {
				CraftrEntity parent = entities.get(e.entityExtends);
				parent.subEntities.add(e);
			} else if(MinecraftConstants.entities.contains(e.entityExtends)) {
				
			} else {
				Token token = null;
				List<TokenPattern<?>> actions = e.getDeclaration().searchByName("UNIT_ACTION");
				for(TokenPattern<?> action : actions) {
					if(action.search(TokenType.UNIT_ACTION).get(0).value.equals("extends")) {
						token = action.search(TokenType.IDENTIFIER).get(0);
					}
				}
				if(token != null) {
					System.out.println("<span style=\"color:red\">Error: Entity \"" + e.entityExtends + "\" is not defined.\n\tat </span>" + 
							token.getFormattedPath());
				} else {
					System.out.println("<span style=\"color:red\">Error: Entity \"" + e.entityExtends + "\" is not defined.</span>");
				}
				return false;
			}
		}
		
		ArrayList<String> markedForRemoval = new ArrayList<>();

		it = entities.keySet().iterator();
		while(it.hasNext()) {
			CraftrEntity e = entities.get(it.next());
			if(MinecraftConstants.entities.contains(e.getName())) {
				e.entityExtends = e.entityType = e.getName();
			} else if(e.entityExtends != null && !MinecraftConstants.entities.contains(e.entityExtends)) {
				markedForRemoval.add(e.getName());
			} else if(e.entityExtends == null) {
				e.entityExtends = e.entityType = "armor_stand";
			}
		}
		
		for(String s : markedForRemoval) {
			entities.remove(s);
		}

		it = entities.keySet().iterator();
		VInteger i = new VInteger(1);
		while(it.hasNext()) {
			CraftrEntity e = entities.get(it.next());
			e.setID(i);
			e.propagateType();
		}
		return true;
	}
	
	@Override
	public String toString() {
		return entities.values().toString();
	}

	@Override
	public Iterator<CraftrEntity> iterator() {
		return entities.values().iterator();
	}
}
