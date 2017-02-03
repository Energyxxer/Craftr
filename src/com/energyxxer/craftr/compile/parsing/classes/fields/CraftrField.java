package com.energyxxer.craftr.compile.parsing.classes.fields;

import com.energyxxer.craftr.compile.parsing.classes.values.CraftrValue;

import java.util.ArrayList;

public abstract class CraftrField {

	public enum FieldModifier {
		PACKAGE, PUBLIC, PRIVATE, PROTECTED, STATIC, FINAL
	}

	public abstract ArrayList<FieldModifier> getModifiers();
	public abstract String getName();
	public abstract CraftrValue getValue();
}
