package com.energyxxer.craftr.global;

import com.energyxxer.craftr.logic.Project;
import com.energyxxer.craftr.ui.Tab;
import com.energyxxer.craftr.ui.explorer.Explorer;
import com.energyxxer.craftr.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class ProjectManager {
	private static ArrayList<Project> loadedProjects = new ArrayList<Project>();

	public static void loadWorkspace() {
		
		loadedProjects.clear();
		
		File workspace = new File(Preferences.get("workspace_dir"));

		File[] fileList = workspace.listFiles();
		if (fileList == null) {
			return;
		}

		//ArrayList<File> files = new ArrayList<>();
		for(File file : fileList) {
			if (file.isDirectory() && new File(file.getAbsolutePath() + File.separator + ".project").exists()) {
				//files.add(file);
				loadedProjects.add(new Project(new File(file.getAbsolutePath())));
			}
		}
	}
	
	public static Project getAssociatedProject(File file) {
		String projectName = file.getAbsolutePath().replaceFirst(Matcher.quoteReplacement(Preferences.get("workspace_dir") + File.separator), "").split(Matcher.quoteReplacement(File.separator))[0];
		for(Project project : loadedProjects) {
			if(project.getName().equals(projectName)) {
				return project;
			}
		}
		return null;
	}
	
	public static void setIconFor(File file, String value) {
		for(Project project : loadedProjects) {
			project.setIconFor(file, value);
		}
	}
	
	public static String getIconFor(File file) {
		for(Project project : loadedProjects) {
			String icon = project.getIconFor(file);
			if(icon != null) return icon;
		}
		return null;
	}
	
	
	public static Project getSelected() {
		Project selected = null;

		Tab selectedTab = TabManager.getSelectedTab();
		if(Explorer.selectedLabels.size() > 0) {
			selected = getAssociatedProject(new File(Explorer.selectedLabels.get(0).parent.path));
		} else if(selectedTab != null) {
			selected = selectedTab.getLinkedProject();
		}
		return selected;
	}
	
	public static void create(String name) {
		Project p = new Project(name);
		p.createNew();
		loadedProjects.add(p);
	}
	
	public static boolean renameFile(File file, String newName) {
		String path = file.getAbsolutePath();
		String name = file.getName();
		String rawName = StringUtil.stripExtension(name);
		String extension = name.replaceAll(rawName, "");
		String pathToParent = path.substring(0, path.lastIndexOf(name));
		
		File newFile = new File(pathToParent + newName + extension);
		
		boolean renamed = file.renameTo(newFile);
		
		
		if(renamed) {
			for(Project project : loadedProjects) {

				String oldRPath = project.getRelativePath(new File(path));
				String newRPath = project.getRelativePath(newFile);
				
				if(oldRPath != null && project.icons.containsKey(oldRPath.intern())) {
					project.icons.put(newRPath.intern(), project.icons.get(oldRPath.intern()));
					project.icons.remove(oldRPath.intern());
					project.updateConfig();
				}
			}
		}
		
		return renamed;
	}
	
}
