package com.energyxxer.craftrlang.compiler.lexical_analysis.profiles;

/**
 * Defines sub-routines to analyze special-case tokens.
 */
public interface ScannerContext {
    /**
     * Analyzes the given substring, starting at the
     * current position of the Scanner, and returns information about the analysis.
     *
     * @param str The substring to analyze.
     *
     * @return A semanticContext response object containing information about the analysis.
     * */
    ScannerContextResponse analyze(String str);

    default ContextCondition getCondition() {
        return ContextCondition.NONE;
    }

    enum ContextCondition {
        NONE, LEADING_WHITESPACE
    }
}
