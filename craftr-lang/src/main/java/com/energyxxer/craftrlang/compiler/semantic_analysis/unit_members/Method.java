package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.craftrlang.CraftrUtil;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.AbstractFileComponent;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.code_blocks.CodeBlock;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
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
    private List<FormalParameter> positionalParams = new ArrayList<>();
    private List<FormalParameter> keywordParams = new ArrayList<>();
    private String thread;

    private CodeBlock codeBlock;

    private final MethodSignature signature;

    public Method(Unit declaringUnit, TokenStructure pattern) {
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

        if(this.type == MethodType.OPERATOR_OVERLOAD) {
            this.name = ((TokenItem) (pattern.find("OPERATOR_REFERENCE").getContents())).getContents().value;
        } else {
            this.name = ((TokenItem) pattern.find("METHOD_NAME")).getContents().value;
        }

        boolean validConstructor = this.type == MethodType.CONSTRUCTOR;

        if(this.type == MethodType.CONSTRUCTOR && !this.name.equals(this.declaringUnit.getName())) {
            this.declaringUnit.getAnalyzer().getCompiler().getReport().addNotice(
                    new Notice(
                            NoticeType.ERROR,
                            "Invalid method declaration; return type required",
                            pattern.find("METHOD_NAME").getFormattedPath()
                    )
            );
            validConstructor = false;
        }

        TokenList modifierPatterns = (TokenList) pattern.find("MODIFIER_LIST");
        if(modifierPatterns != null) {
            modifiers = SemanticUtils.getModifiers(Arrays.asList(modifierPatterns.getContents()), declaringUnit.getAnalyzer());
        }
        else modifiers = new ArrayList<>();

        this.declaringUnit.getSubSymbolTable().put(this);

        TokenList rawParameterList = (TokenList) pattern.find("PARAMETER_LIST");

        if(rawParameterList != null) {
            List<TokenPattern<?>> parameterPatterns = rawParameterList.searchByName("FORMAL_PARAMETER");

            for(TokenPattern<?> rawParam : parameterPatterns) {
                boolean isKeyword = rawParam.find("PARAMETER_INITIALIZER") != null;
                if(!isKeyword && !keywordParams.isEmpty()) {
                    this.declaringUnit.getAnalyzer().getCompiler().getReport().addNotice(
                            new Notice(
                                    NoticeType.ERROR,
                                    "Positional parameters must not follow keyword parameters",
                                    rawParam.getFormattedPath()
                            )
                    );
                }

                FormalParameter param = new FormalParameter(this, rawParam);

                boolean isDuplicate = false;

                for(FormalParameter p : positionalParams) {
                    if(p.getName().equals(param.getName())) {
                        this.declaringUnit.getAnalyzer().getCompiler().getReport().addNotice(
                                new Notice(
                                        NoticeType.ERROR,
                                        "Variable '" + param.getName() + "' already defined in the scope",
                                        rawParam.find("PARAMETER_NAME").getFormattedPath()
                                )
                        );
                        isDuplicate = true;
                        break;
                    }
                }
                if(!isDuplicate) for(FormalParameter p : keywordParams) {
                    if(p.getName().equals(param.getName())) {
                        this.declaringUnit.getAnalyzer().getCompiler().getReport().addNotice(
                                new Notice(
                                        NoticeType.ERROR,
                                        "Variable '" + param.getName() + "' already defined in the scope",
                                        rawParam.find("PARAMETER_NAME").getFormattedPath()
                                )
                        );
                        isDuplicate = true;
                        break;
                    }
                }

                if(!isDuplicate) {
                    ((isKeyword) ? keywordParams : positionalParams).add(param);
                }

            }
        }

        if(type == MethodType.CONSTRUCTOR) this.returnType = new DataType(declaringUnit);
        else if(type == MethodType.EVENT) this.returnType = DataType.VOID;
        else this.returnType = DataType.parseType(pattern.find("RETURN_TYPE").flattenTokens(), declaringUnit.getDeclaringFile().getReferenceTable(), declaringUnit);

        this.signature = new MethodSignature(declaringUnit, name, positionalParams);

        if(modifierPatterns != null && modifiers.contains(CraftrUtil.Modifier.NATIVE)) {
            declaringUnit.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Native Methods", NoticeType.INFO, "Require native implementation for '" + getSignature().toString() + "'", modifierPatterns.getFormattedPath()));
        }
    }

    public boolean isStatic() {
        return modifiers.contains(CraftrUtil.Modifier.STATIC);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @NotNull Unit getUnit() {
        return declaringUnit;
    }

    @Override
    public String toString() {
        return modifiers + " " + this.name;
    }

    public SymbolVisibility getVisibility() {
        return modifiers.contains(CraftrUtil.Modifier.PUBLIC) ? SymbolVisibility.GLOBAL :
                modifiers.contains(CraftrUtil.Modifier.PROTECTED) ? SymbolVisibility.UNIT_INHERITED :
                        modifiers.contains(CraftrUtil.Modifier.PRIVATE) ? SymbolVisibility.UNIT :
                                SymbolVisibility.PACKAGE;
    }

    public String getFullyQualifiedName() {
        return declaringUnit.getFullyQualifiedName() + "::" + this.name;
    }

    public MethodSignature getSignature() {
        return signature;
    }
}
