package com.energyxxer.craftr.compile.analysis.profiles;

/**
 * Defines sub-routines to analyzes special-case tokens.
 */
public interface AnalysisContext {
    /**
     * Analyzes the given substring, starting at the
     * current position of the Analyzer, and returns information about the analysis.
     *
     * @param str The substring to analyze.
     *
     * @return A context response object containing information about the analysis.
     * */
    AnalysisContextResponse analyze(String str);
}
