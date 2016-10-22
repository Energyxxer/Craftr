package com.energyxxer.files;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.energyxxer.cbe.Preferences;
import com.energyxxer.cbe.Window;
import com.energyxxer.ui.explorer.Explorer;
import com.energyxxer.util.FileUtil;
import com.energyxxer.util.StringPrompt;
import com.energyxxer.util.StringValidator;

/**
 * Creates projects and files.
 */
public class FileFactory {
	public static void newProject() {
		String projectName = StringPrompt.prompt("Create New Project", "Enter the name of your new project:", "",
			new StringValidator() {
				@Override
				public boolean validate(String str) {
					return FileUtil.validateFilename(str) && !new File(Preferences.get("workspace_dir") + File.separator + str).exists();
				}
			}
		);
		if (projectName != null) {
			new File(Preferences.get("workspace_dir") + File.separator + projectName).mkdirs();
			try {
				new File(Preferences.get("workspace_dir") + File.separator + projectName + File.separator + ".project").createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Window.explorer.generateProjectList();
		}
		System.out.println(projectName);
	}
	
	public static void newFolder() {
		if(Explorer.selectedLabels.size() != 1) return;
		if(!new File(Explorer.selectedLabels.get(0).parent.path).isDirectory()) return;
		String folderName = StringPrompt.prompt("Create New Folder", "Enter the name of your new folder:", "",
			new StringValidator() {
				@Override
				public boolean validate(String str) {
					return FileUtil.validateFilename(str) && !new File(Explorer.selectedLabels.get(0).parent.path + File.separator + str).exists();
				}
			}
		);
		if (folderName != null) {
			if(Explorer.selectedLabels.size() != 1) return;
			new File(Explorer.selectedLabels.get(0).parent.path + File.separator + folderName).mkdirs();
			Window.explorer.generateProjectList();
		}
		System.out.println(folderName);
	}
	
	public static void newEntity() {
		if(Explorer.selectedLabels.size() != 1) return;
		if(!new File(Explorer.selectedLabels.get(0).parent.path).isDirectory()) return;
		String entityName = StringPrompt.prompt("Create New Entity", "Enter the name of your new entity:", "",
			new StringValidator() {
				@Override
				public boolean validate(String str) {
					return FileUtil.validateFilename(str) && !new File(Explorer.selectedLabels.get(0).parent.path + File.separator + str + ".mcbe").exists();
				}
			}
		);
		if(entityName != null) {
			try {
				String path = Explorer.selectedLabels.get(0).parent.path + File.separator + entityName + ".mcbe";
				new File(path).createNewFile();
				PrintWriter writer = new PrintWriter(path, "UTF-8");
				writer.println("public entity " + entityName + " {");
				writer.println("	");
				writer.println("}");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Window.explorer.generateProjectList();
		}
		System.out.println(entityName);
	}
	
	public static void newItem() {
		if(Explorer.selectedLabels.size() != 1) return;
		if(!new File(Explorer.selectedLabels.get(0).parent.path).isDirectory()) return;
		String itemName = StringPrompt.prompt("Create New Item", "Enter the name of your new item:", "",
			new StringValidator() {
				@Override
				public boolean validate(String str) {
					return FileUtil.validateFilename(str) && !new File(Explorer.selectedLabels.get(0).parent.path + File.separator + str + ".mcbi").exists();
				}
			}
		);
		if(itemName != null) {
			try {
				String path = Explorer.selectedLabels.get(0).parent.path + File.separator + itemName + ".mcbi";
				new File(path).createNewFile();
				PrintWriter writer = new PrintWriter(path, "UTF-8");
				writer.println("public item " + itemName + " {");
				writer.println("	");
				writer.println("}");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Window.explorer.generateProjectList();
		}
		System.out.println(itemName);
	}
}
