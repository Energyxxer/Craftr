package com.energyxxer.craftr.compile.parsing.classes.units;

import com.energyxxer.craftr.compile.analysis.token.Token;
import com.energyxxer.craftr.compile.analysis.token.TokenType;
import com.energyxxer.craftr.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.craftr.compile.exceptions.CraftrParserException;
import com.energyxxer.craftr.compile.parsing.classes.files.CraftrFile;
import com.energyxxer.craftr.compile.parsing.classes.files.CraftrPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.energyxxer.craftr.compile.parsing.classes.units.CraftrUnit.UnitModifier.PACKAGE;
import static com.energyxxer.craftr.compile.parsing.classes.units.CraftrUnit.UnitModifier.PUBLIC;

/**
 * Created by User on 12/2/2016.
 */
public class CraftrUnit {
    protected final String name;
    protected final CraftrFile file;

    protected final TokenPattern<?> declaration;

    protected final ArrayList<UnitModifier> modifiers;

    protected CraftrPackage unitPackage;

    public enum UnitModifier {
        PACKAGE, PUBLIC, FINAL, ABSTRACT, COMPILATION, INGAME
    }

    public CraftrUnit(CraftrFile file, TokenPattern<?> unit) throws CraftrParserException {

        this.file = file;
        unitPackage = file.getPackage();

        TokenPattern<?> header = unit.searchByName("UNIT_DECLARATION").get(0);

        String type = header.search(TokenType.UNIT_TYPE).get(0).value;

        this.name = ((Token) header.searchByName("UNIT_NAME").get(0).getContents()).value;

        List<TokenPattern<?>> modifierTokens = unit.searchByName("UNIT_MODIFIER");

        ArrayList<String> modifierStrings = new ArrayList<>();

        for (TokenPattern<?> t : modifierTokens) {
            Token token = (Token) t.getContents();
            if(modifierStrings.contains(token.value)) {
                throw new CraftrParserException("Duplicate modifier '" + token.value + "'", token);
            }
            for(String[] rawCombination : UnitConstants.INVALID_UNIT_MODIFIERS) {
                List<String> combination = Arrays.asList(rawCombination);
                if(!combination.contains(token.value)) continue;

                for(String str : combination) {
                    if(str.equals(token.value)) continue;
                    if(modifierStrings.contains(str)) {
                        throw new CraftrParserException("Illegal combination of modifiers: '" + str + "' and '" + token.value + "'.", token);
                    }
                }
            }
            modifierStrings.add(token.value);
        }

        modifiers = new ArrayList<>();

        for(String m : modifierStrings) {
            modifiers.add(UnitModifier.valueOf(m.toUpperCase()));
        }
        if(!modifiers.contains(PUBLIC)) modifiers.add(PACKAGE);

        this.declaration = unit;
    }

    public String getName() {
        return name;
    }

    public String getIdentity() {
        return file.getPackage().toString() + "." + name;
    }

    public TokenPattern<?> getDeclaration() {
        return declaration;
    }

    public CraftrPackage getUnitPackage() {
        return unitPackage;
    }

    public void setUnitPackage(CraftrPackage unitPackage) {
        this.unitPackage = unitPackage;
    }
}