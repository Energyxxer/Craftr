package com.energyxxer.cbe.compile.parsing.classes.units;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenType;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.compile.exceptions.CBEParserException;
import com.energyxxer.cbe.compile.parsing.classes.files.CBEFile;
import com.energyxxer.cbe.compile.parsing.classes.files.CBEPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.energyxxer.cbe.compile.parsing.classes.units.CBEUnit.UnitModifier.PACKAGE;
import static com.energyxxer.cbe.compile.parsing.classes.units.CBEUnit.UnitModifier.PUBLIC;

/**
 * Created by User on 12/2/2016.
 */
public class CBEUnit {
    protected final String name;
    protected final CBEFile file;

    protected final TokenPattern declaration;

    protected final ArrayList<UnitModifier> modifiers;

    protected CBEPackage unitPackage;

    public enum UnitModifier {
        PACKAGE, PUBLIC, FINAL, ABSTRACT, COMPILATION, INGAME
    }

    public CBEUnit(CBEFile file, TokenPattern<?> unit) throws CBEParserException {

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
                throw new CBEParserException("Duplicate modifier '" + token.value + "'", token);
            }
            for(String[] rawCombination : UnitConstants.INVALID_UNIT_MODIFIERS) {
                List<String> combination = Arrays.asList(rawCombination);
                if(!combination.contains(token.value)) continue;

                for(String str : combination) {
                    if(str.equals(token.value)) continue;
                    if(modifierStrings.contains(str)) {
                        throw new CBEParserException("Illegal combination of modifiers: '" + str + "' and '" + token.value + "'.", token);
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

    public TokenPattern getDeclaration() {
        return declaration;
    }

    public CBEPackage getUnitPackage() {
        return unitPackage;
    }

    public void setUnitPackage(CBEPackage unitPackage) {
        this.unitPackage = unitPackage;
    }
}