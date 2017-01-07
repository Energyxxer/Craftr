package com.energyxxer.cbe.syntax;

import com.energyxxer.cbe.compile.analysis.token.TokenType;
import com.energyxxer.cbe.syntax.style.Style;

import java.util.HashMap;

/**
 * Defines what and how character sequences should be highlighted in a text
 * editorComponent.
 */
public class CBESyntaxDark extends Syntax {
	public static final CBESyntaxDark INSTANCE = new CBESyntaxDark();
    public static HashMap<String, HashMap<String, Object>> styles = new HashMap<String, HashMap<String, Object>>();

    static {
		styles.put("comment", new Style().setColor("#666666").setItalic().getMap());
		styles.put("number", new Style().setColor("#FF7766").getMap());
		styles.put("modifier", new Style().setColor("#1290c3").setBold().getMap());
		styles.put("unit_type", new Style().setColor("#1290c3").setBold().getMap());
		styles.put("unit_action", new Style().setColor("#1290c3").setBold().getMap());
		styles.put("data_type", new Style().setColor("#1290c3").setBold().getMap());
		styles.put("keyword", new Style().setColor("#1290C3").setBold().getMap());
		styles.put("action_keyword", new Style().setColor("#1290c3").setBold().getMap());
		styles.put("boolean", new Style().setColor("#1290c3").setBold().setItalic().getMap());
		styles.put("string_literal", new Style().setColor("#99FF66").getMap());
		styles.put("operator", new Style().setColor("#BB3333").getMap());
		styles.put("negation_operator", new Style().setColor("#A72D2D").getMap());
		styles.put("identifier_operator", new Style().setColor("#A72D2D").getMap());
		styles.put("null", new Style().setColor("#FF4444").setBold().getMap());
		styles.put("annotation_marker", new Style().setColor("#999999").setItalic().getMap());
		styles.put("blockstate", new Style().setColor("#66FFAA").setBold().setItalic().getMap());

		styles.put("#is_keyword", new Style().setColor("#1290c3").setBold().getMap());

		styles.put("#is_entity", new Style().setColor("#99FF66").getMap());
		styles.put("#is_enum", new Style().setColor("#AA66FF").getMap());
		styles.put("#is_enum_value", new Style().setColor("#66BBFF").setBold().getMap());
		styles.put("#is_annotation_header", new Style().setColor("#999999").setItalic().getMap());
		styles.put("#is_annotation", new Style().setItalic().getMap());

	}

	protected CBESyntaxDark() {super();}

	@Override
	public HashMap<String, HashMap<String, Object>> getStyles() {
		return styles;
	}
}
