package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.semantic_analysis.implicity.ImplicityState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Energyxxer on 07/14/2017.
 */
public enum UnitType {
    ENTITY("name:Entity,plural:Entities",ImplicityState.IMPLICIT), ITEM("name:Item,plural:Items",ImplicityState.EXPLICIT), FEATURE("name:Feature,plural:Features",ImplicityState.EXPLICIT), CLASS("name:Class,plural:Classes",ImplicityState.EXPLICIT), ENUM("name:Enum,plural:Enums",ImplicityState.EXPLICIT), WORLD("name:World,plural:Worlds,singleton:true",ImplicityState.EXPLICIT);

    private String name;
    private String plural;
    private List<CraftrLang.Modifier> inferredModifiers;
    private ImplicityState implicity;
    private boolean singleton = false;

    UnitType(String raw, ImplicityState implicity) {
        this.inferredModifiers = new ArrayList<>();
        this.implicity = implicity;

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
                        CraftrLang.Modifier value = CraftrLang.Modifier.valueOf(modifier.toUpperCase());
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

    public List<CraftrLang.Modifier> getInferredModifiers() {
        return inferredModifiers;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public ImplicityState getImplicity() {
        return implicity;
    }
}
