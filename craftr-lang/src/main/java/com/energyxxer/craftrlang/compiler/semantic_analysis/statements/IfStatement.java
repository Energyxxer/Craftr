package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.booleans.BooleanResolution;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit.ExplicitBoolean;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public class IfStatement extends Statement {
    public IfStatement(TokenPattern<?> pattern, SemanticContext semanticContext, FunctionSection section) {
        super(pattern, semanticContext, section);
    }

    @Override
    public Value evaluate(FunctionSection section) {
        Value value = ExprResolver.analyzeValue(pattern.find("VALUE"), semanticContext, dataHolder, section);
        Statement statement = Statement.read(((TokenStructure) pattern.find("STATEMENT")).getContents(), semanticContext, section);
        if(statement != null && statement instanceof CodeBlock) ((CodeBlock) statement).initialize(semanticContext.getInstance());

        DataReference reference = value.getReference();
        if(reference instanceof BooleanReference) {
            if(reference instanceof ExplicitBoolean) {
                if(((ExplicitBoolean) reference).getValue()) {
                    if(statement != null) return statement.evaluate(section);
                    else return null;
                }
                //explicit false, break
            } else {
                BooleanResolution resolution = ((BooleanReference) reference).resolveBoolean(section, semanticContext, false);
                return null;
            }
        }

        return null;
    }
}
