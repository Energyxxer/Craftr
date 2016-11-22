package com.energyxxer.cbe.util;

import java.util.Arrays;
import java.util.List;

public class MethodInvocation {
	private String className;
	private String methodName;
	private List<String> methodSignature;
	private List<Object> params;
	
	public MethodInvocation(String className, String methodName, String[] methodSignature, Object[] params) {
		this.className = className;
		this.methodName = methodName;
		this.methodSignature = Arrays.asList(methodSignature);
		this.params = Arrays.asList(params);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MethodInvocation)) return false;
		
		MethodInvocation o = (MethodInvocation) obj;
		
		return o.className.equals(className) && 
				o.methodName.equals(methodName) &&
				o.methodSignature.equals(methodSignature) &&
				o.params.equals(params);
	}
	
	@Override
	public String toString() {
		String o = "";
		o += className + "." + methodName + "(";
		for(int i = 0; i < methodSignature.size(); i++) {
			o += methodSignature.get(i) + " " + params.get(i);
			if(i < methodSignature.size()-1) {
				o += ", ";
			}
		}
		o += ");";
		return o;
	}
}
