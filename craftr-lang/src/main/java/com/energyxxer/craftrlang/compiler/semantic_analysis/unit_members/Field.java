package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.AbstractFileComponent;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.util.out.Console;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 2/26/2017.
 */
public class Field extends AbstractFileComponent implements Symbol {
    private Unit declaringUnit;

    private List<CraftrUtil.Modifier> modifiers;
    private DataType type;
    private String name;

    public Field(Unit declaringUnit, TokenPattern<?> pattern, List<CraftrUtil.Modifier> modifiers, DataType type) {
        super(pattern);
        this.declaringUnit = declaringUnit;
        this.modifiers = new ArrayList<>();
        this.modifiers.addAll(modifiers);
        this.type = null;

        this.name = ((TokenItem) pattern.find("VARIABLE_NAME")).getContents().value;
        if(this.name.equalsIgnoreCase("debug")) {
            Console.info.println("Pointer test: " + this.declaringUnit.getSubSymbolTable().getRoot().getSymbol("com.energyxxer.aetherii.entities.living.hostile.tempest.shootTime", declaringUnit));
        }

        if(!this.declaringUnit.getSubSymbolTable().getMap().containsKey(this.name)) {
            this.declaringUnit.getSubSymbolTable().put(this);
        } else {
            declaringUnit.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Variable '" + name + "' already declared in the scope", this.pattern.getFormattedPath()));
        }


        Console.debug.println("at " + declaringUnit + "#" + name);
    }

    public static List<Field> parseDeclaration(TokenPattern<?> pattern, Unit declaringUnit) {
        ArrayList<Field> fields = new ArrayList<>();

        //Skipping over annotations

        List<CraftrUtil.Modifier> modifiers = Collections.emptyList();

        TokenList modifierPatterns = (TokenList) pattern.find("INNER.MODIFIER_LIST");
        if(modifierPatterns != null) modifiers = SemanticUtils.getModifiers(Arrays.asList(modifierPatterns.getContents()), declaringUnit.getAnalyzer());

        Console.debug.println("[Field] Modifiers: " + modifiers);

        TokenPattern<?>[] declarationList = ((TokenList) pattern.find("INNER.VARIABLE_DECLARATION_LIST")).getContents();

        for(TokenPattern<?> p : declarationList) {
            if(!p.getName().equals("VARIABLE_DECLARATION")) continue;
            fields.add(new Field(declaringUnit, p, modifiers, null));
        }

        return fields;
    }

    @Override
    public @Nullable Unit getUnit() {
        return declaringUnit;
    }

    @Override
    public @Nullable Package getPackage() {
        return declaringUnit.getPackage();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return modifiers + " " + this.name;
    }

    public SymbolVisibility getVisibility() {
        return modifiers.contains(CraftrUtil.Modifier.PUBLIC) ? SymbolVisibility.GLOBAL :
                modifiers.contains(CraftrUtil.Modifier.PROTECTED) ? SymbolVisibility.UNIT :
                        modifiers.contains(CraftrUtil.Modifier.PRIVATE) ? SymbolVisibility.UNIT :
                                SymbolVisibility.PACKAGE;
    }
}
