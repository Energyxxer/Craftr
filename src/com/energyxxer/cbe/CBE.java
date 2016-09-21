package com.energyxxer.cbe;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

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
		
		new File("C:/Program Files/Command Block Engine").mkdir();
		if(!new File(Preferences.path).isFile()) {
			new File(Preferences.path).createNewFile();
			PrintWriter writer = new PrintWriter(Preferences.path, "UTF-8");
			
			String result = WorkspaceSelector.prompt();
			
			try {
				Paths.get(result);
			} catch (InvalidPathException |  NullPointerException ex) {
				result = "C:/Program Files/Command Block Engine/workspace";
            }
			
			new File(result).mkdirs();
			
			writer.println("workspace_dir: " + ((result != null) ? result : "C:/Program Files/Command Block Engine/workspace"));
			writer.close();
		}
		
		Preferences.read();
		
		Window.explorer.generateProjectList();
	}
	
}
