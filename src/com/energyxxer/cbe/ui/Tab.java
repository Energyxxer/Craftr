package com.energyxxer.cbe.ui;

import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.logic.Project;
import com.energyxxer.cbe.ui.editor.CBEEditor;

import javax.swing.text.BadLocationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Concept of an open tab in the interface. Contains a component that represents
 * the clickable tab element.
 */
public class Tab {
	private TabComponent linkedTabComponent;
	private Project linkedProject;
	public String path;
	public CBEEditor editor;
	public String savedString;
	public boolean visible = true;

	public long openedTimeStamp;

	@Override
	public String toString() {
		return "Tab [title=" + linkedTabComponent.getName() + ", path=" + path + ", visible=" + visible + "]";
	}

	public Tab(String path) {
		this.path = path;
		this.linkedProject = ProjectManager.getAssociatedProject(new File(path));
		editor = new CBEEditor(this);
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get(path));
			String s = new String(encoded);
			editor.setText(s);
			editor.editor.setCaretPosition(0);
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
		if (linkedTabComponent != null) {
			boolean newIsSaved = editor.editor.getText().intern() == savedString || savedString == null;
			linkedTabComponent.setSaved(newIsSaved);
		}
	}

	public void updateName() {
		linkedTabComponent.setName(getFile().getName());
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isActive() {
		return this.linkedTabComponent.selected;
	}

	public void save() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");

			String text = editor.editor.getText();
			if(!text.endsWith("\n")) {
				text = text.concat("\n");
				try {
					editor.editor.getDocument().insertString(text.length()-1,"\n",null);
				} catch(BadLocationException e) {
					e.printStackTrace();
				}
			}
			text = text.intern();

			writer.print(text);
			writer.close();
			savedString = text;
			linkedTabComponent.setSaved(true);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public Project getLinkedProject() {
		return linkedProject;
	}
}
