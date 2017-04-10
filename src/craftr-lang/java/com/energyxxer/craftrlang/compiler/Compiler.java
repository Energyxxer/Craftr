package com.energyxxer.craftrlang.compiler;

import com.energyxxer.craftrlang.compiler.lexical_analysis.Scanner;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenStream;
import com.energyxxer.craftrlang.compiler.parsing.Parser;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.interfaces.ProgressListener;
import com.energyxxer.craftrlang.projects.Project;
import com.energyxxer.util.out.Console;

import java.util.ArrayList;

public class Compiler {

	private final Project project;
	private ArrayList<ProgressListener> progressListeners = new ArrayList<>();
	
	public Compiler(Project project) {
		this.project = project;

		/*for(Token t : ts) {
			System.out.println(t.getFullString());
		}*/
	}
	
	public void compile() {
		if(project.getWorld() == null) throw new IllegalStateException("Project does not have an output directory.");
		new Thread(() -> {
			this.setProgress("Scanning files...");
			TokenStream ts = new TokenStream();
			this.setProgress("Scanning files...");
			new Scanner(project.getDirectory(),ts);
			this.setProgress("Parsing tokens...");
			Parser parser = new Parser(ts);
			this.setProgress("Analyzing code...");
			new SemanticAnalyzer(parser.filePatterns, project.getDirectory());
		}).start();
	}

	public void addProgressListener(ProgressListener l) {
		progressListeners.add(l);
	}

	private void setProgress(String message) {
		progressListeners.forEach(l -> l.onProgress(message));
	}

	static {
		Console.warn.println("Compiler loaded.");
	}
}
