package com.energyxxer.syntax;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Abstract syntax class, in case I want to add support for multiple syntaxes in the future.
 * */
public abstract class Syntax {
	public static HashMap<String, ArrayList<String>> patterns;
	public static HashMap<String, HashMap<String,String>> styles;

	public abstract HashMap<String, ArrayList<String>> getPatterns();
	public abstract HashMap<String, HashMap<String,Object>> getStyles();
}
