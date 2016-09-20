package cbe;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Preferences {
	public static String path = "C:/Program Files/Command Block Engine/preferences";
	public static HashMap<String, String> data;
	
	public static void read() throws IOException {
		
		data = new HashMap<String, String>();
		
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		
		String str = new String(encoded, "UTF-8");
		
		String[] lines = str.split("\n");
		
		for(int i = 0; i < lines.length; i++) {
			String[] splits = lines[i].split(":",2);
			
			data.put(splits[0].trim(), splits[1].trim());
		}
	}
	
	public static void write() throws IOException {
		new File(Preferences.path).createNewFile();
		PrintWriter writer = new PrintWriter(Preferences.path, "UTF-8");
		for(HashMap.Entry<String, String> entry : data.entrySet()) {
			writer.println(entry.getKey() + ": " + entry.getValue());
		}
		writer.close();
	}
}
