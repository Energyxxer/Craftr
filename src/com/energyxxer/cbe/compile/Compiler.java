package com.energyxxer.cbe.compile;

import com.energyxxer.cbe.compile.analysis.Analyzer;
import com.energyxxer.cbe.compile.analysis.token.TokenStream;
import com.energyxxer.cbe.compile.parsing.Parser;
import com.energyxxer.cbe.compile.parsing.classes.values.CBEIntegerValue;
import com.energyxxer.cbe.compile.parsing.exceptions.IllegalOperandsException;
import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.logic.Project;

public class Compiler {

	public final Project project;
	
	private Compiler(Project project) {
		
		if(project.getWorld() == null) {
			project.promptOutput();
		}
		
		this.project = project;
		TokenStream ts = new TokenStream();
		new Analyzer(project.directory,ts);
		new Parser(ts, project);

		try {
			System.out.println(new CBEIntegerValue(256).division(new CBEIntegerValue(16)));
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
}
