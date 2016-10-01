package com.energyxxer.files;

import java.io.File;
import java.io.IOException;

import com.energyxxer.cbe.Preferences;
import com.energyxxer.cbe.Window;
import com.energyxxer.util.StringPrompt;
import com.energyxxer.util.StringValidator;

/**
 * Creates projects and files.
 * */
public class FileFactory {
	public static void newProject() {
		System.out.println(Preferences.get("workspace_dir"));
		String projectName = StringPrompt.prompt("Create New Project", "Enter the name of your new project:", "", new StringValidator() {
			public boolean validate(String str) {
				return !new File(Preferences.get("workspace_dir") + File.separator + str).exists();
			}
		});
		if(projectName != null) {
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
}
