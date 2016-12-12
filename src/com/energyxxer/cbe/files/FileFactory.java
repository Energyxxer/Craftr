package com.energyxxer.cbe.files;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import com.energyxxer.cbe.global.Preferences;
import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.ui.explorer.Explorer;
import com.energyxxer.cbe.util.FileUtil;
import com.energyxxer.cbe.util.StringPrompt;
import com.energyxxer.cbe.util.StringUtil;
import com.energyxxer.cbe.util.StringValidator;

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
			ProjectManager.create(projectName);
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
				File newFile = new File(path);
				newFile.createNewFile();
				ProjectManager.setIconFor(newFile, "entity");
				PrintWriter writer = new PrintWriter(path, "UTF-8");
				writer.println("package " + (StringUtil.stripExtension(FileUtil.getRelativePath(newFile.getParentFile(),ProjectManager.getAssociatedProject(newFile).directory))).replace(File.separator,".") + ";");
				writer.println();
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
					return FileUtil.validateFilename(str) && !new File(Explorer.selectedLabels.get(0).parent.path + File.separator + str + ".mcbe").exists();
				}
			}
		);
		if(itemName != null) {
			try {
				String path = Explorer.selectedLabels.get(0).parent.path + File.separator + itemName + ".mcbe";
				File newFile = new File(path);
				newFile.createNewFile();
				ProjectManager.setIconFor(newFile, "item");
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
