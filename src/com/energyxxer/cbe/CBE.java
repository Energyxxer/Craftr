package com.energyxxer.cbe;

import java.io.IOException;

import javax.swing.Timer;

public class CBE {
	public static CBE generator;
	
	public static Window window;
	
	public static Timer timer;
	
	
	CBE() {
		window = new Window();
	}
	
	public static void main(String[] args) throws IOException {
		
		generator = new CBE();
		
		Window.explorer.generateProjectList();
	}
	
}
