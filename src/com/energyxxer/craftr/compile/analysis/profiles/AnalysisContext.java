package com.energyxxer.craftr.compile.analysis.profiles;

/**
 * Created by User on 2/4/2017.
 */
public interface AnalysisContext {

    /**
     * Used to check whether an analysis context should begin.
     * It will not be called if such context is currently active.
     *
     * @return AnalysisContextResponse: <br>
     *     <ol>
     *         <li><code>boolean</code> success: <code>true</code> if context should begin, false otherwise.</li>
     *         <li><code>String</code> value: If applicable, the segment of the given string to skip analysis for.</li>
     *     </ol>
     * */
    //AnalysisContextResponse checkStart(String str, AnalysisContextData data);

    /**
     * Used to check whether an analysis context should end.
     * Other in-context calculations may also be done.
     *
     * @return AnalysisContextResponse: <br>
     *     <ol>
     *         <li><code>boolean</code> success: <code>true</code> if context should end, false otherwise.</li>
     *         <li><code>String</code> value: If applicable, the segment of the given string to skip analysis for
     *         and add to the end of the token.</li>
     *     </ol>
     * */
    //AnalysisContextResponse checkInner(String str, AnalysisContextData data);

    AnalysisContextResponse analyze(String str);
}
