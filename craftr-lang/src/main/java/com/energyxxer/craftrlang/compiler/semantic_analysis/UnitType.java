package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.CraftrUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Energyxxer on 07/14/2017.
 */
public enum UnitType {
    ENTITY("name:Entity,plural:Entities"), ITEM("name:Item,plural:Items"), FEATURE("name:Feature,plural:Features"), CLASS("name:Class,plural:Classes"), ENUM("name:Enum,plural:Enums"), WORLD("name:World,plural:Worlds,singleton:true");

    private String name;
    private String plural;
    private List<CraftrUtil.Modifier> inferredModifiers;
    private boolean singleton = false;

    UnitType(String raw) {
        this.inferredModifiers = new ArrayList<>();

        String[] params = raw.split(",");
        for(String param : params) {
            String[] pair = param.split(":",2);
            switch(pair[0].trim()) {
                case "name": {
                    this.name = pair[1].trim(); break;
                } case "plural": {
                    this.plural = pair[1].trim(); break;
                } case "modifiers": {
                    String[] rawModifiers = pair[1].trim().split(" ");
                    for(String modifier : rawModifiers) {
                        CraftrUtil.Modifier value = CraftrUtil.Modifier.valueOf(modifier.toUpperCase());
                        inferredModifiers.add(value);
                    }
                } case "singleton": {
                    this.singleton = pair[1].trim().equals("true");
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getPlural() {
        return plural;
    }

    public List<CraftrUtil.Modifier> getInferredModifiers() {
        return inferredModifiers;
    }

    public boolean isSingleton() {
        return singleton;
    }
}
