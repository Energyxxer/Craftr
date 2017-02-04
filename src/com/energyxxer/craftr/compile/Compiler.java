package com.energyxxer.craftr.compile;

import com.energyxxer.craftr.compile.analysis.Analyzer;
import com.energyxxer.craftr.compile.analysis.token.TokenStream;
import com.energyxxer.craftr.compile.exceptions.IllegalOperandsException;
import com.energyxxer.craftr.compile.parsing.Parser;
import com.energyxxer.craftr.compile.parsing.classes.values.CraftrIntegerValue;
import com.energyxxer.craftr.compile.parsing.classes.values.CraftrValue;
import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.global.ProjectManager;
import com.energyxxer.craftr.logic.Project;

public class Compiler {

	public final Project project;
	
	private Compiler(Project project) {
		
		if(project.getWorld() == null) {
			project.promptOutput();
		}
		
		this.project = project;
		TokenStream ts = new TokenStream();
		new Analyzer(project.getDirectory(),ts);
		new Parser(ts, project);

		try {
			Console.info.println(new CraftrIntegerValue(256).division(new CraftrIntegerValue(16)));
		} catch(IllegalOperandsException e) {
			e.printStackTrace();
		}
		
		/*for(Token t : ts) {
			System.out.println(t.getFullString());
		}*/
	}
	
	public static void compile() {
		Project project = ProjectManager.getSelected();
		if(project != null) {
			new Thread(() -> new Compiler(project),"Compiler").start();
		}
	}

	static {
		Console.warn.println("Compiler loaded.");
		//Initialize:
		CraftrValue.init();
	}
}
