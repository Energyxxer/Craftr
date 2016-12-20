package com.energyxxer.cbe.compile.parsing.classes.fields;

import com.energyxxer.cbe.compile.parsing.classes.values.CBEValue;

import java.util.ArrayList;

public abstract class CBEField {

	public enum FieldModifier {
		PACKAGE, PUBLIC, PRIVATE, PROTECTED, STATIC, FINAL
	}

	public abstract ArrayList<FieldModifier> getModifiers();
	public abstract String getName();
	public abstract CBEValue getValue();
}
