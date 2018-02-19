package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.commodore.score.ScoreHolder;
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
import com.energyxxer.craftrlang.compiler.semantic_analysis.constants.SemanticUtils;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.*;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.natives.NativeMethods;
import com.energyxxer.craftrlang.compiler.semantic_analysis.statements.CodeBlock;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by User on 5/16/2017.
 */
public class Method extends AbstractFileComponent implements Symbol, SemanticContext {
    private Unit declaringUnit;

    private MethodType type;
    private List<CraftrLang.Modifier> modifiers;
    private DataType returnType;
    private String name;
    private final boolean validName;
    private List<FormalParameter> positionalParams = new ArrayList<>();
    private HashMap<String, FormalParameter> keywordParams = new HashMap<>();

    private CodeBlock codeBlock;

    private final MethodSignature signature;

    public Method(TokenPattern<?> pattern, Unit declaringUnit, MethodType type, List<CraftrLang.Modifier> modifiers, DataType returnType, String name, boolean validName, List<FormalParameter> positionalParams, HashMap<String, FormalParameter> keywordParams, CodeBlock codeBlock, MethodSignature signature) {
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
                if(!isDuplicate) {
                    if(keywordParams.containsKey(param.getName())) {
                        this.declaringUnit.getAnalyzer().getCompiler().getReport().addNotice(
                                new Notice(
                                        NoticeType.ERROR,
                                        "Variable '" + param.getName() + "' already defined in the scope",
                                        rawParam.find("PARAMETER_NAME").getFormattedPath()
                                )
                        );
                        isDuplicate = true;
                    }
                }

                if(!isDuplicate) {
                    if(isKeyword) {
                        keywordParams.put(param.getName(),param);
                    } else positionalParams.add(param);
                }

            }
        }

        if(type == MethodType.CONSTRUCTOR) this.returnType = (validConstructor) ? declaringUnit.getDataType() : DataType.VOID;
        else if(type == MethodType.EVENT) this.returnType = DataType.VOID;
        else this.returnType = DataType.parseType(pattern.find("RETURN_TYPE").flattenTokens(), declaringUnit.getDeclaringFile().getReferenceTable(), declaringUnit);

        this.signature = new MethodSignature(declaringUnit, name, positionalParams);

        TokenPattern<?> body = pattern.find("METHOD_BODY");
        boolean omitted = body.find("OMITTED_BODY") != null;
        if(omitted) {
            if(!(modifiers.contains(CraftrLang.Modifier.NATIVE) || modifiers.contains(CraftrLang.Modifier.ABSTRACT))) {
                declaringUnit.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Missing method body", body.getFormattedPath()));
            }
        } else {
            TokenPattern<?> block = body.find("DELIMITED_CODE_BLOCK");
            this.codeBlock = new CodeBlock(block, this);
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
            codeBlock.clearSymbols();
            ArrayList<LocalScore> paramReferences = new ArrayList<>();

            for(FormalParameter param : positionalParams) {
                /*LocalScore paramReference = this.getPlayer().PARAMETER.get();
                paramReference.setInUse(true);
                paramReferences.add(paramReference);
                codeBlock.getSymbolTable().put(
                        new Variable(
                                param.getName(),
                                Collections.emptyList(),
                                param.getType(),
                                this,
                                param.getType().createImplicit(
                                        paramReference,
                                        this
                                )
                        )
                );*/
            }
            FormalParameter[] keywordArr = keywordParams.values().toArray(new FormalParameter[0]);
            for(FormalParameter param : keywordArr) {
                /*LocalScore paramReference = this.getPlayer().PARAMETER.get();
                paramReference.setInUse(true);
                paramReferences.add(paramReference);
                codeBlock.getSymbolTable().put(
                        new Variable(
                                param.getName(),
                                Collections.emptyList(),
                                param.getType(),
                                this,
                                param.getType().createImplicit(
                                        paramReference,
                                        this
                                )
                        )
                );*/
            }
            codeBlock.setSilent(false);
            codeBlock.initialize();
            //paramReferences.forEach(p -> p.setInUse(false));

            //System.out.println(codeBlock.getFunction().build());
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
    public SemanticContext getParent() {
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

    public List<FormalParameter> getPositionalParams() {
        return positionalParams;
    }

    public HashMap<String, FormalParameter> getKeywordParams() {
        return keywordParams;
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

    public Value writeCall(Function function, List<ActualParameter> positionalParams, HashMap<String, ActualParameter> keywordParams, TokenPattern<?> pattern, SemanticContext semanticContext) {
        if(this.modifiers.contains(CraftrLang.Modifier.NATIVE)) {
            return NativeMethods.execute(this, function, positionalParams, keywordParams, pattern, semanticContext);
        }

        if(codeBlock == null) {
            semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Method '" + this.getSignature().getFullyQualifiedName() + "' hasn't been defined... for some reason... how did you even call this method?", pattern.getFormattedPath()));
            return null;
        }

        //positionalParams and this.positionalParams SHOULD NOT DIFFER IN LENGTH. IF THEY DO, WELL CRUD.
        //Assign actual positional params to code block
        for(int i = 0; i < this.positionalParams.size(); i++) {
            Value param = positionalParams.get(i).getValue();
            if(param == null) {
                codeBlock.getSymbolTable().put(this.positionalParams.get(i).getName(), null);
                continue;
            }
            param = param.unwrap(function);

            codeBlock.getSymbolTable().put(new Variable(this.positionalParams.get(i).getName(), Collections.emptyList(), this.positionalParams.get(i).getType(), this, param));
        }

        //Assign actual keyword params to code block
        //TODO: NULL VALUES IF KEYWORD PARAMETERS OMITTED
        for(Map.Entry<String, ActualParameter> entry : keywordParams.entrySet()) {
            if(!this.keywordParams.containsKey(entry.getKey())) {
                semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Unknown keyword parameter label '" + entry.getKey() + "'", entry.getValue().pattern.getFormattedPath()));
                continue;
            }
            if(entry.getValue().getDataType().instanceOf(this.keywordParams.get(entry.getKey()).getType())) {
                codeBlock.getSymbolTable().put(new Variable(entry.getKey(), Collections.emptyList(), this.keywordParams.get(entry.getKey()).getType(), this, entry.getValue().getValue()));
            } else {
                semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + entry.getValue().getDataType() + " cannot be converted to " + this.keywordParams.get(entry.getKey()).getType(), entry.getValue().pattern.getFormattedPath()));
            }
        }
        codeBlock.setSilent(true);
        return codeBlock.writeToFunction(function);
    }

    @Override
    public DataHolder getDataHolder() {
        return this.isStatic() ? declaringUnit : declaringUnit.getGenericInstance();
    }

    @Override
    public ObjectInstance getInstance() {
        return (!this.isStatic()) ? declaringUnit.getGenericInstance() : null;
    }

    @Override
    public ScoreHolder getPlayer() {
        return this.isStatic() ? declaringUnit.getPlayer() : declaringUnit.getGenericInstance().getScoreHolder();
    }
}
