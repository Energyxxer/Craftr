package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.exceptions.CraftrException;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.AbstractFileComponent;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.util.out.Console;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 5/16/2017.
 */
public class Method extends AbstractFileComponent implements Symbol {
    private Unit declaringUnit;

    private MethodType type;
    private List<CraftrUtil.Modifier> modifiers;
    private DataType returnType;
    private String name;
    private List<FormalParameter> parameters;
    private String thread;

    public Method(Unit declaringUnit, TokenStructure pattern) throws CraftrException {
        super(pattern);

        switch(pattern.getContents().getName()) {
            case "METHOD_METHOD": {
                this.type = MethodType.METHOD;
                break;
            }
            case "METHOD_CONSTRUCTOR": {
                this.type = MethodType.CONSTRUCTOR;
                break;
            }
            case "METHOD_EVENT": {
                this.type = MethodType.EVENT;
                break;
            }
            case "METHOD_OPERATOR": {
                this.type = MethodType.OPERATOR_OVERLOAD;
                break;
            }
        }

        this.declaringUnit = declaringUnit;
        this.modifiers = new ArrayList<>();
        this.returnType = null;
        if(this.type == MethodType.OPERATOR_OVERLOAD) {
            this.name = ((TokenItem) (pattern.find("OPERATOR_REFERENCE").getContents())).getContents().value;
        } else {
            this.name = ((TokenItem) pattern.find("METHOD_NAME")).getContents().value;
        }

        if(this.type == MethodType.CONSTRUCTOR && !this.name.equals(this.declaringUnit.getName())) {
            this.declaringUnit.getDeclaringFile().getAnalyzer().getCompiler().getReport().addNotice(
                    new Notice(
                            NoticeType.ERROR,
                            "Invalid method declaration; return type required",
                            pattern.find("METHOD_NAME").getFormattedPath()
                    )
            );
        }

        List<CraftrUtil.Modifier> modifiers = Collections.emptyList();

        TokenList modifierPatterns = (TokenList) pattern.find("MODIFIER_LIST");
        if(modifierPatterns != null) modifiers = SemanticUtils.getModifiers(Arrays.asList(modifierPatterns.getContents()));

        if(modifiers.contains(CraftrUtil.Modifier.STATIC)) {
            this.declaringUnit.getStaticSymbolTable().put(this);
        } else {
            this.declaringUnit.getInstanceSymbolTable().put(this);
        }

        Console.debug.println("[" + this.type + "] " + modifiers + " " + this.declaringUnit + "::" + this.name);
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
