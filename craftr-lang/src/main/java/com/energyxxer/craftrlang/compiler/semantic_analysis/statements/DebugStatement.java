package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.FunctionComment;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class DebugStatement extends Statement {

    private String label;

    public DebugStatement(TokenPattern<?> pattern, SemanticContext semanticContext, FunctionSection section) {
        super(pattern, semanticContext, section);
        this.label = ((TokenItem) pattern.find("DEBUG_LABEL")).getContents().value;
    }

    @Override
    public Value evaluate(FunctionSection section) {

        String out = "";

        switch(label) {
            case "implicity": {
                out = "" + ((CodeBlock) semanticContext).asObjectInstance().isImplicit();
                break;
            }
            case "semanticContext": {
                out = "" + semanticContext;
                break;
            }
            case "instance": {
                out = "" + ((CodeBlock) semanticContext).asObjectInstance();
                break;
            }
            case "executionContext": {
                out = "" + section.getExecutionContext();
                break;
            }
            case "finalSender": {
                out = "" + section.getExecutionContext().getFinalSender();
                break;
            }
            case "breakpoint": {
                boolean a = true;
                break;
            }
        }

        section.append(new FunctionComment("debug " + label + ": " + out));
        return null;
    }
}
