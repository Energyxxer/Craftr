package com.energyxxer.cbe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import com.energyxxer.ui.CBEEditor;
import com.energyxxer.ui.TabComponent;

public class Tab {
	private TabComponent linkedTabComponent;
	public String path;
	CBEEditor editor;
	public String savedString;
	
	public long openedTimeStamp;
	
	public Tab(String path) {
		this.path = path;
		editor = new CBEEditor(this);
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			String s = new String(encoded);
			editor.setText(s);
			editor.startEditListeners();
			savedString = s.intern();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		associate(new TabComponent(this));		
		
		TabManager.addTabComponent(getLinkedTabComponent());
		
		openedTimeStamp = new Date().getTime();
	}
	
	public void associate(TabComponent tc) {
		linkedTabComponent = tc;
	}
	
	@SuppressWarnings("unused")
	private File getFile() {
		return new File(path);
	}

	public TabComponent getLinkedTabComponent() {
		return linkedTabComponent;
	}
	
	public void onSelect() {
		openedTimeStamp = new Date().getTime();
	}
	
	public void onEdit() {
		if(linkedTabComponent != null) {
			boolean newIsSaved = editor.editor.getText().intern() == savedString || savedString == null;
			linkedTabComponent.setSaved(newIsSaved);
		}
	}
	
	public void save() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");
			
			writer.print(editor.editor.getText());
			writer.close();
			savedString = editor.editor.getText().intern();
			linkedTabComponent.setSaved(true);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
