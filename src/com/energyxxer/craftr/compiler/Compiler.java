package com.energyxxer.craftr.compiler;

import com.energyxxer.craftr.compiler.lexical_analysis.Scanner;
import com.energyxxer.craftr.compiler.lexical_analysis.token.TokenStream;
import com.energyxxer.craftr.compiler.parsing.Parser;
import com.energyxxer.craftr.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftr.global.Console;
import com.energyxxer.craftr.global.ProjectManager;
import com.energyxxer.craftr.global.Status;
import com.energyxxer.craftr.logic.Project;
import com.energyxxer.craftr.main.window.CraftrWindow;

public class Compiler {

	public final Project project;
	
	private Compiler(Project project) {
		
		if(project.getWorld() == null) {
			project.promptOutput();
		}

		Status status = new Status("Scanning files...");
		
		this.project = project;
		TokenStream ts = new TokenStream();
		status.setMessage("Scanning files...");
		CraftrWindow.setStatus(status);
		new Scanner(project.getDirectory(),ts);
		status.setMessage("Parsing tokens...");
		CraftrWindow.setStatus(status);
		Parser parser = new Parser(ts);
		status.setMessage("Analyzing code...");
		CraftrWindow.setStatus(status);
		new SemanticAnalyzer(parser.filePatterns, project);
		CraftrWindow.dismissStatus(status);

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
	}
}
