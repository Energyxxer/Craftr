package com.energyxxer.craftrlang.compiler.semantic_analysis;

import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.semantic_analysis.implicity.ImplicityState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Energyxxer on 07/14/2017.
 */
public enum UnitType {
    ENTITY  ("name: Entity, plural: Entities, implicity: implicit"),
    ITEM    ("name: Item, plural: Items, implicity: explicit"),
    FEATURE ("name: Feature, plural: Features, implicity: explicit"),
    CLASS   ("name: Class, plural: Classes, implicity: explicit"),
    ENUM    ("name: Enum, plural: Enums, implicity: explicit"),
    WORLD   ("name: World, plural: Worlds, singleton: true, implicity: explicit");

    private String name;
    private String plural;
    private List<CraftrLang.Modifier> inferredModifiers;
    private ImplicityState implicity;
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
                        CraftrLang.Modifier value = CraftrLang.Modifier.valueOf(modifier.toUpperCase());
                        inferredModifiers.add(value);
                    }
                    break;
                } case "singleton": {
                    this.singleton = pair[1].trim().equals("true");
                    break;
                } case "implicity": {
                    this.implicity = ImplicityState.valueOf(pair[1].trim().toUpperCase());
                    break;
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
