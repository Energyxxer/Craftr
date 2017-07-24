package com.energyxxer.craftrlang.compiler.semantic_analysis.context;

import com.energyxxer.craftrlang.compiler.semantic_analysis.CraftrFile;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.compiler.semantic_analysis.Unit;
import com.energyxxer.craftrlang.compiler.semantic_analysis.code_blocks.CodeBlock;

/**
 * Created by User on 5/16/2017.
 */
public interface Context {
    CraftrFile getDeclaringFile();
    Unit getUnit();
    ContextType getContextType();
    SemanticAnalyzer getAnalyzer();
}
