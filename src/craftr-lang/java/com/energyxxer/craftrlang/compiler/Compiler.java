package com.energyxxer.craftrlang.compiler;

import com.energyxxer.craftrlang.compiler.lexical_analysis.Scanner;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenStream;
import com.energyxxer.craftrlang.compiler.parsing.Parser;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.interfaces.ProgressListener;
import com.energyxxer.craftrlang.projects.Project;
import com.energyxxer.util.out.Console;

import java.util.ArrayList;

public class Compiler {

	private final Project project;
	private ArrayList<ProgressListener> progressListeners = new ArrayList<>();
	private ArrayList<Runnable> completionListeners = new ArrayList<>();
	private CompilerReport report = null;
	
	public Compiler(Project project) {
		this.project = project;

		/*for(Token t : ts) {
			System.out.println(t.getFullString());
		}*/
	}
	
	public void compile() {
		report = new CompilerReport();
		if(project == null) {
			report.addNotice(new Notice(NoticeType.ERROR, "No project selected"));
			completionListeners.forEach(Runnable::run);
			return;
		}
		if(project.getWorld() == null) {
			report.addNotice(new Notice(NoticeType.ERROR, "Project does not have an output directory."));
		}
		new Thread(() -> {
			this.setProgress("Scanning files... [" + project.getName() + "]");
			TokenStream ts = new TokenStream();
			this.setProgress("Scanning files... [" + project.getName() + "]");
			new Scanner(project.getDirectory(),ts);
			this.setProgress("Parsing tokens... [" + project.getName() + "]");
			Parser parser = new Parser(ts);
			this.setProgress("Analyzing code... [" + project.getName() + "]");
			new SemanticAnalyzer(this, parser.filePatterns, project.getDirectory());

			this.setProgress("Compilation completed with " + report.getTotalsString());
			completionListeners.forEach(Runnable::run);
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

	public CompilerReport getReport() {
		return report;
	}

	public void addCompletionListener(Runnable r) {
		completionListeners.add(r);
	}
}
