package com.energyxxer.craftr.compiler.lexical_analysis.profiles;

import com.energyxxer.craftr.compiler.lexical_analysis.token.TokenSection;
import com.energyxxer.craftr.util.StringLocation;

import java.util.HashMap;

/**
 * Defines a response model for custom analysis contexts.
 */
public class ScannerContextResponse {
    /**
     * Whether the analysis was successful and returned a token.
     * */
    public final boolean success;
    /**
     * The full string that makes up the token associated with
     * the context, if applicable. null if the analysis wasn't successful.
     * */
    public final String value;
    /**
     * The token type of the return token, if applicable.
     * null if the analysis wasn't successful.
     * */
    public final String tokenType;
    /**
     * The location within the substring passed to the analysis method where
     * the given token ends. Should include the index, line and column changes.
     * null if the analysis wasn't successful.
     * */
    public final StringLocation endLocation;
    /**
     * A map containing the indices at which the string should be formatted differently,
     * with the given syntax key.
     * */
    public final HashMap<TokenSection, String> subSections;

    /**
     * Creates a response from the given success value. This should <b>only</b> be used
     * when the analysis wasn't successful, otherwise the Scanner might throw a NullPointerException.
     *
     * @param success Whether the analysis was successful. For this constructor, it should only be false.
     * */
    public ScannerContextResponse(boolean success) {
        this(success,null, null, null, null);
    }

    /**
     * Creates a response.
     *
     * @param success Whether the analysis was successful.
     * @param value The value of the resulting token.
     * @param tokenType The type of the resulting token.
     * <br>
     * <br>
     * The end location will be assumed to be:
     * <ul>
     * <li>index: Equal to the length of the value parameter.</li>
     * <li>line: 0</li>
     * <li>column: Equal to the length of the value parameter.</li>
     * </ul>
     * */
    public ScannerContextResponse(boolean success, String value, String tokenType) {
        this(success, value, new StringLocation(value.length()), tokenType);
    }

    /**
     * Creates a response.
     *
     * @param success Whether the analysis was successful.
     * @param value The value of the resulting token.
     * @param tokenType The type of the resulting token.
     * @param subSections Map containing sections of the string to be formatted differently.
     * <br>
     * <br>
     * The end location will be assumed to be:
     * <ul>
     * <li>index: Equal to the length of the value parameter.</li>
     * <li>line: 0</li>
     * <li>column: Equal to the length of the value parameter.</li>
     * </ul>
     * */
    public ScannerContextResponse(boolean success, String value, String tokenType, HashMap<TokenSection, String> subSections) {
        this(success, value, new StringLocation(value.length()), tokenType, subSections);
    }


    /**
     * Creates a response.
     *
     * @param success Whether the analysis was successful.
     * @param value The value of the resulting token.
     * @param endLoc The location of the end index within the substring.
     * @param tokenType The type of the resulting token.
     * */
    public ScannerContextResponse(boolean success, String value, StringLocation endLoc, String tokenType) {
        this.success = success;
        this.value = value;
        this.endLocation = endLoc;
        this.tokenType = tokenType;
        this.subSections = null;
    }


    /**
     * Creates a response.
     *
     * @param success Whether the analysis was successful.
     * @param value The value of the resulting token.
     * @param endLoc The location of the end index within the substring.
     * @param tokenType The type of the resulting token.
     * @param subSections Map containing sections of the string to be formatted differently.
     * */
    public ScannerContextResponse(boolean success, String value, StringLocation endLoc, String tokenType, HashMap<TokenSection, String> subSections) {
        this.success = success;
        this.value = value;
        this.endLocation = endLoc;
        this.tokenType = tokenType;
        this.subSections = subSections;
    }
}
