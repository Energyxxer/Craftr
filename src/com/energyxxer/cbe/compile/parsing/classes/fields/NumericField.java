package com.energyxxer.cbe.compile.parsing.classes.fields;

import com.energyxxer.cbe.util.Range;

public class NumericField {
	
	public boolean isAbstract = true;
	public final NumericFieldType type;
	public Range range = null;
	
	public NumericField(NumericFieldType type) {
		this.type = type;
	}
	public NumericField(NumericFieldType type, double min, double max) {
		this.type = type;
		this.range = new Range(min,max);
	}
	
	public enum NumericFieldType {
		INT, FLOAT
	}
}
