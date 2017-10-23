package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.AbstractFileComponent;
import com.energyxxer.craftrlang.compiler.semantic_analysis.CraftrFile;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.abstract_package.Package;
import com.energyxxer.craftrlang.compiler.semantic_analysis.code_blocks.CodeBlock;
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.ContextType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Symbol;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolVisibility;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 5/16/2017.
 */
public class Method extends AbstractFileComponent implements Symbol, Context {
    private Unit declaringUnit;

    private MethodType type;
    private List<CraftrLang.Modifier> modifiers;
    private DataType returnType;
    private String name;
    private final boolean validName;
    private List<FormalParameter> positionalParams = new ArrayList<>();
    private List<FormalParameter> keywordParams = new ArrayList<>();

    private CodeBlock codeBlock;

    private final MethodSignature signature;

    public Method(TokenPattern<?> pattern, Unit declaringUnit, MethodType type, List<CraftrLang.Modifier> modifiers, DataType returnType, String name, boolean validName, List<FormalParameter> positionalParams, List<FormalParameter> keywordParams, CodeBlock codeBlock, MethodSignature signature) {
        super(pattern);
        this.declaringUnit = declaringUnit;
        this.type = type;
        this.modifiers = modifiers;
        this.returnType = returnType;
        this.name = name;
        this.validName = validName;
        this.positionalParams = positionalParams;
        this.keywordParams = keywordParams;
        this.codeBlock = codeBlock;
        this.signature = signature;
    }

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
        this.validName = !CraftrLang.isPseudoIdentifier(this.name);
        if(!validName) {
            getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal method name", pattern.find("METHOD_NAME").getFormattedPath()));
        }

        boolean validConstructor = this.type == MethodType.CONSTRUCTOR;

        if(this.type == MethodType.CONSTRUCTOR && !this.name.equals(this.declaringUnit.getName())) {
            getAnalyzer().getCompiler().getReport().addNotice(
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

                FormalParameter param = new FormalParameter(rawParam, this);

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

        if(type == MethodType.CONSTRUCTOR) this.returnType = (validConstructor) ? declaringUnit.getDataType() : DataType.VOID;
        else if(type == MethodType.EVENT) this.returnType = DataType.VOID;
        else this.returnType = DataType.parseType(pattern.find("RETURN_TYPE").flattenTokens(), declaringUnit.getDeclaringFile().getReferenceTable(), declaringUnit);

        this.signature = new MethodSignature(declaringUnit, name, positionalParams);

        if(modifierPatterns != null && modifiers.contains(CraftrLang.Modifier.NATIVE)) {
            declaringUnit.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Native Methods", NoticeType.INFO, "Require native implementation for '" + getSignature().toString() + "'", modifierPatterns.getFormattedPath()));
        }

        TokenPattern<?> body = pattern.find("METHOD_BODY");
        boolean omitted = body.find("OMITTED_BODY") != null;
        if(omitted) {
            if(!(modifiers.contains(CraftrLang.Modifier.NATIVE) || modifiers.contains(CraftrLang.Modifier.ABSTRACT))) {
                declaringUnit.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Missing method body", body.getFormattedPath()));
            }
        } else {
            TokenPattern<?> block = body.find("DELIMITED_CODE_BLOCK");
            this.codeBlock = new CodeBlock(block, this);
            this.codeBlock.setStatic(this.isStatic());
        }
    }

    public Method duplicate() {
        return new Method(pattern, declaringUnit, type, modifiers, returnType, name, validName, positionalParams, keywordParams, codeBlock, signature);
    }

    public boolean isStatic() {
        return modifiers.contains(CraftrLang.Modifier.STATIC);
    }

    public String getPlayerName() {
        return declaringUnit.getName().toUpperCase();
    }

    public DataType getReturnType() {
        return returnType;
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
        return modifiers.contains(CraftrLang.Modifier.PUBLIC) ? SymbolVisibility.GLOBAL :
                modifiers.contains(CraftrLang.Modifier.PROTECTED) ? SymbolVisibility.UNIT_INHERITED :
                        modifiers.contains(CraftrLang.Modifier.PRIVATE) ? SymbolVisibility.UNIT :
                                SymbolVisibility.PACKAGE;
    }

    public String getFullyQualifiedName() {
        return declaringUnit.getFullyQualifiedName() + "::" + this.name;
    }

    public MethodSignature getSignature() {
        return signature;
    }

    public void initCodeBlock() {
        if(codeBlock != null) {
            codeBlock.initialize();
            System.out.println(codeBlock.getFunction().build());
        }
    }

    public CodeBlock getCodeBlock() {
        return codeBlock;
    }

    @Override
    public CraftrFile getDeclaringFile() {
        return declaringUnit.getDeclaringFile();
    }

    @Override
    public ContextType getContextType() {
        return ContextType.BLOCK;
    }

    @Override
    public SemanticAnalyzer getAnalyzer() {
        return declaringUnit.getAnalyzer();
    }

    @Override
    public Context getParent() {
        return declaringUnit;
    }

    @Override
    public @NotNull Package getPackage() {
        return declaringUnit.getPackage();
    }

    @Override
    public SymbolTable getReferenceTable() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Method method = (Method) o;

        return signature.equals(method.signature);
    }

    @Override
    public int hashCode() {
        return signature.hashCode();
    }
}
