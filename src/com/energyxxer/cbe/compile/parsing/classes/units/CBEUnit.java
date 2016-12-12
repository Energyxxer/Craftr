package com.energyxxer.cbe.compile.parsing.classes.units;

import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenType;
import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.compile.parsing.classes.files.CBEFile;
import com.energyxxer.cbe.compile.parsing.classes.files.CBEPackage;
import com.energyxxer.cbe.compile.parsing.exceptions.CBEParserException;

import java.util.ArrayList;
import java.util.List;

import static com.energyxxer.cbe.compile.parsing.classes.units.CBEUnit.UnitModifiers.*;

/**
 * Created by User on 12/2/2016.
 */
public class CBEUnit {
    protected final String name;
    protected final CBEFile file;

    protected final TokenPattern declaration;

    protected final ArrayList<UnitModifiers> modifiers;

    protected CBEPackage unitPackage;



    public enum UnitType {
        ENTITY, ITEM, FEATURE, CLASS;
    }


    public enum UnitModifiers {
        PACKAGE, PUBLIC, FINAL;
    }
    public CBEUnit(CBEFile file, TokenPattern<?> unit) throws CBEParserException {

        this.file = file;
        unitPackage = file.getPackage();

        TokenPattern<?> header = unit.searchByName("UNIT_DECLARATION").get(0);

        String type = header.search(TokenType.UNIT_TYPE).get(0).value;

        this.name = ((Token) header.searchByName("UNIT_NAME").get(0).getContents()).value;

        List<TokenPattern<?>> modifierTokens = unit.searchByName("UNIT_MODIFIER");

        boolean isPublic = false;
        boolean isFinal = false;

        for (TokenPattern<?> t : modifierTokens) {
            Token token = (Token) t.getContents();
            if (token.value.equals("final")) {
                if(!isFinal) {
                    isFinal = true;
                    continue;
                } else {
                    throw new CBEParserException("Duplicate modifier 'final'", token);
                }
            } else if (token.value.equals("public")) {
                if(!isPublic) {
                    isPublic = true;
                    continue;
                } else {
                    throw new CBEParserException("Duplicate modifier 'public'", token);
                }
            } else {
                throw new CBEParserException("Modifier '" + token.value + "' is not allowed here", token);
            }
        }

        modifiers = new ArrayList<>();
        modifiers.add((isPublic) ? PUBLIC : PACKAGE);
        if(isFinal) modifiers.add(FINAL);

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