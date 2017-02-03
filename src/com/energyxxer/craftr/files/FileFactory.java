package com.energyxxer.craftr.files;

import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.global.ProjectManager;
import com.energyxxer.craftr.global.TabManager;
import com.energyxxer.craftr.logic.Project;
import com.energyxxer.craftr.main.window.Window;
import com.energyxxer.craftr.ui.explorer.Explorer;
import com.energyxxer.craftr.util.FileUtil;
import com.energyxxer.craftr.util.StringPrompt;
import com.energyxxer.craftr.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Creates projects and files.
 */
public class FileFactory {

	private static final List<String> NO_PROMPT_TYPES = Collections.singletonList("World");

	private static String filter(String template, HashMap<String, String> variables) {
		for(Map.Entry<String, String> variable : variables.entrySet()) {
			String pattern = Pattern.compile("\\$" + variable.getKey().toUpperCase() + "\\$", Pattern.CASE_INSENSITIVE).toString();
			//Console.debug.println(pattern);
			template = template.replaceAll(pattern, variable.getValue());
		}
		return template;
	}

	private static String getPackage(File file) {
		Project associatedProject = ProjectManager.getAssociatedProject(file);
		return (
			(associatedProject != null) ?
				StringUtil.stripExtension(
					FileUtil.getRelativePath(
						file.getParentFile(),
						associatedProject.directory
					)
				) : "src"
		).replace(File.separator,".");
	}

	public static void newProject() {
		String projectName = StringPrompt.prompt("Create New Project", "Enter the name of your new project:", "",
			str -> FileUtil.validateFilename(str) && !new File(Preferences.get("workspace_dir") + File.separator + str).exists()
		);
		if (projectName != null) {
			ProjectManager.create(projectName);
			Window.explorer.generateProjectList();
		}
	}
	
	public static void newFolder() {
		if(Explorer.selectedLabels.size() != 1) return;
		if(!new File(Explorer.selectedLabels.get(0).parent.path).isDirectory()) return;
		String folderName = StringPrompt.prompt("Create New Folder", "Enter the name of your new folder:", "",
			str -> FileUtil.validateFilename(str) && !new File(Explorer.selectedLabels.get(0).parent.path + File.separator + str).exists()
		);
		if (folderName != null) {
			if(Explorer.selectedLabels.size() != 1) return;
			if(!new File(Explorer.selectedLabels.get(0).parent.path + File.separator + folderName).mkdirs())
				Console.err.println("Couldn't create folder '" + folderName + "'");
			Window.explorer.generateProjectList();
		}
	}

	public static void newUnit(String type) {
		if(Explorer.selectedLabels.size() != 1) return;
		if(!new File(Explorer.selectedLabels.get(0).parent.path).isDirectory()) return;
		String unitName;
		if(!NO_PROMPT_TYPES.contains(type)) {
			unitName = StringPrompt.prompt("Create New Entity", "Enter the name of your new entity:", "",
					str -> FileUtil.validateFilename(str) && !new File(Explorer.selectedLabels.get(0).parent.path + File.separator + str + ".craftr").exists()
			);
		} else unitName = type;
		if(unitName != null) {
			try {
				String path = Explorer.selectedLabels.get(0).parent.path + File.separator + unitName + ".craftr";
				File newFile = new File(path);
				boolean successful = newFile.createNewFile();

				int pos = 0;

				if(successful) {
					ProjectManager.setIconFor(newFile, type.toLowerCase());
					PrintWriter writer = new PrintWriter(path, "UTF-8");

					Date date = new Date();

					HashMap<String, String> variables = new HashMap<>();
					variables.put("package", getPackage(newFile));
					variables.put("name", unitName);
					variables.put("user", Preferences.get("username", "User"));

					variables.put("day", Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG_FORMAT, Locale.getDefault()));
					variables.put("date", Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
					variables.put("week", Integer.toString(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)));
					variables.put("month", Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1));
					variables.put("year", Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
					variables.put("timestamp", date.toString());

					String text = filter(FileDefaults.defaults.get(type.toLowerCase()), variables);
					pos = Math.max(0, text.indexOf("$END$"));
					text = text.replace("$END$", "");

					writer.print(text);
					writer.close();
				}

				if(newFile.exists()) TabManager.openTab(path,pos);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Window.explorer.generateProjectList();
		}
	}
	/*
	public static void newEntity() {
		if(Explorer.selectedLabels.size() != 1) return;
		if(!new File(Explorer.selectedLabels.get(0).parent.path).isDirectory()) return;
		String unitName = StringPrompt.prompt("Create New Entity", "Enter the name of your new entity:", "",
			new StringValidator() {
				@Override
				public boolean validate(String str) {
					return FileUtil.validateFilename(str) && !new File(Explorer.selectedLabels.get(0).parent.path + File.separator + str + ".craftr").exists();
				}
			}
		);
		if(unitName != null) {
			try {
				String path = Explorer.selectedLabels.get(0).parent.path + File.separator + unitName + ".craftr";
				File newFile = new File(path);
				newFile.createNewFile();
				ProjectManager.setIconFor(newFile, "entity");
				PrintWriter writer = new PrintWriter(path, "UTF-8");

				HashMap<String, String> variables = new HashMap<>();
				variables.put("package", getPackage(newFile));
				variables.put("name", unitName);
				variables.put("timestamp", new Date().toString());

				writer.print(filter(FileDefaults.defaults.get("entity"), variables));
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
					return FileUtil.validateFilename(str) && !new File(Explorer.selectedLabels.get(0).parent.path + File.separator + str + ".craftr").exists();
				}
			}
		);
		if(itemName != null) {
			try {
				String path = Explorer.selectedLabels.get(0).parent.path + File.separator + itemName + ".craftr";
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
	}*/
}
