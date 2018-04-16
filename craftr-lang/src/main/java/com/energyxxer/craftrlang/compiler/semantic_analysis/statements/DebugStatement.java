package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionComment;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class DebugStatement extends Statement {

    private String label;

    public DebugStatement(TokenPattern<?> pattern, SemanticContext semanticContext, Function function) {
        super(pattern, semanticContext, function);
        this.label = ((TokenItem) pattern.find("DEBUG_LABEL")).getContents().value;
    }

    @Override
    public Value evaluate(Function function) {

        String out = "";

        switch(label) {
            case "implicity": {
                out = "" + ((CodeBlock) semanticContext).asObjectInstance().isImplicit();
                break;
            }
            case "context": {
                out = "" + semanticContext;
                break;
            }
            case "instance": {
                out = "" + ((CodeBlock) semanticContext).asObjectInstance();
                break;
            }
        }

        function.append(new FunctionComment("debug " + label + ": " + out));
        return null;
    }
}
