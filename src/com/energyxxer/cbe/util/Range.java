package com.energyxxer.cbe.util;

public class Range {
	public double min,max;
	public Range(double min, double max) {
		this.min = min;
		this.max = max;
	}
	
	public boolean contains(double v) {
		return v >= min && v <= max;
	}
	
	public static Range union(Range r1, Range r2) {
		return new Range(Math.min(r1.min, r2.min),Math.max(r1.min, r2.min));
	}
}
