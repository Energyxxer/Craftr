package syntax;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Syntax {
	public static HashMap<String, ArrayList<String>> patterns;
	public static HashMap<String, HashMap<String,String>> styles;

	public abstract HashMap<String, ArrayList<String>> getPatterns();
	public abstract HashMap<String, HashMap<String,Object>> getStyles();
}
