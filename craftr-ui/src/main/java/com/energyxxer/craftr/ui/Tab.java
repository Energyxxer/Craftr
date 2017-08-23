package com.energyxxer.craftr.ui;

import com.energyxxer.craftr.ui.display.DisplayModule;
import com.energyxxer.craftr.ui.editor.CraftrEditor;
import com.energyxxer.craftr.ui.image_viewer.ImageViewer;
import com.energyxxer.craftr.ui.tablist.TabItem;
import com.energyxxer.craftrlang.projects.Project;
import com.energyxxer.craftrlang.projects.ProjectManager;

import javax.swing.JComponent;
import javax.swing.text.BadLocationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Concept of an open tab in the interface. Contains a component that represents
 * the clickable tab element.
 */
public class Tab {
	private Project linkedProject;
	public String path;
	public DisplayModule module;
	private Object savedValue;
	public boolean visible = true;

	public long openedTimeStamp;
	private boolean saved = true;
	private String name;
	private TabItem tabItem;

	@Override
	public String toString() {
		return "Tab [title=" + getName() + ", path=" + path + ", visible=" + visible + "]";
	}

	public Tab(String path) {
		this.path = path;
		this.linkedProject = ProjectManager.getAssociatedProject(new File(path));
		if(path.endsWith(".png")) {
			module = new ImageViewer(this);
		} else {
			module = new CraftrEditor(this);
		}
		savedValue = module.getValue();

		openedTimeStamp = new Date().getTime();

		this.name = new File(path).getName();
	}

	private File getFile() {
		return new File(path);
	}

	public void onSelect() {
		openedTimeStamp = new Date().getTime();
		module.focus();
		module.displayCaretInfo();
	}

	public void onEdit() {
		this.saved = savedValue == null || module.getValue() == savedValue;
	}

	public void updateName() {
		tabItem.updateName();
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isActive() {
		return this.tabItem.isSelected();
	}

	public void save() {
		if(!module.canSave()) return;
		PrintWriter writer;
		try {
			writer = new PrintWriter(path, "UTF-8");

			String text = (String) module.getValue();
			if(!text.endsWith("\n")) {
				text = text.concat("\n");
				try {
					if(module instanceof CraftrEditor) {
						((CraftrEditor) module).editorComponent.getDocument().insertString(text.length()-1,"\n",null);
					}
				} catch(BadLocationException e) {
					e.printStackTrace();
				}
			}
			text = text.intern();

			writer.print(text);
			writer.close();
			savedValue = text;
			saved = true;
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public Project getLinkedProject() {
		return linkedProject;
	}

	public JComponent getModuleComponent() {
		return (JComponent) module;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public String getName() {
		return name;
	}

	public TabItem getLinkedTabItem() {
		return tabItem;
	}

	public void linkTabItem(TabItem tabItem) {
		this.tabItem = tabItem;
	}
}
