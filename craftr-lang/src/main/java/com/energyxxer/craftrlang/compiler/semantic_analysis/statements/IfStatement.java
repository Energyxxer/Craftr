package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.commands.CommandGroup;
import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.enxlex.pattern_matching.structures.TokenPattern;
import com.energyxxer.enxlex.pattern_matching.structures.TokenStructure;
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

        CommandGroup inner = new CommandGroup(section);
        Statement statement = Statement.read(((TokenStructure) pattern.find("STATEMENT")).getContents(), semanticContext, inner);
        if(statement == null) return null;
        if(statement instanceof CodeBlock) ((CodeBlock) statement).initialize(semanticContext.getInstance());

        DataReference reference = value.getReference();
        if(reference instanceof BooleanReference) {
            if(reference instanceof ExplicitBoolean) {
                if(((ExplicitBoolean) reference).getValue()) {
                    Value returnValue = statement.evaluate(inner);
                    section.append(inner);
                    return returnValue;
                }
                //explicit false, break
            } else {
                BooleanResolution resolution = ((BooleanReference) reference).resolveBoolean(section, semanticContext, false);
                ExecuteCommand exec = new ExecuteCommand(inner);
                resolution.getModifiers().forEach(exec::addModifier);
                Value returnValue = statement.evaluate(inner);
                section.append(exec);
                return returnValue;
            }
        }

        return null;
    }

    @Override
    public boolean isExplicit() {
        Value value = ExprResolver.analyzeValue(pattern.find("VALUE"), semanticContext, dataHolder, section);
        if(!value.isExplicit()) return false;
        CommandGroup inner = new CommandGroup(section);
        Statement statement = Statement.read(((TokenStructure) pattern.find("STATEMENT")).getContents(), semanticContext, inner);
        return statement == null || statement.isExplicit();
    }
}
