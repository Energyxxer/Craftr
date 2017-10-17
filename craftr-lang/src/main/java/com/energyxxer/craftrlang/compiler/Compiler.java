package com.energyxxer.craftrlang.compiler;

import com.energyxxer.craftrlang.compiler.lexical_analysis.Scanner;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenStream;
import com.energyxxer.craftrlang.compiler.parsing.Parser;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.interfaces.ProgressListener;
import com.energyxxer.craftrlang.projects.Project;
import com.energyxxer.util.ThreadLock;
import com.energyxxer.util.out.Console;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Compiler {
	private final Project project;
	private final String projectName;
	private final File source;
	private final boolean silent;
	private ArrayList<ProgressListener> progressListeners = new ArrayList<>();
	private ArrayList<Runnable> completionListeners = new ArrayList<>();
	private CompilerReport report = null;
	private int breakpoint = -1;

	private TokenStream ts;
	private Scanner sc;
	private Parser parser;
	private SemanticAnalyzer analyzer;

	private Thread thread = null;

	private final ThreadLock lock = new ThreadLock();
	private CraftrLibrary library = null;
	
	public Compiler(Project project) {
		this.project = project;
		this.projectName = project.getName();
		this.source = project.getSource();

		this.thread = new Thread(this::runCompilation,"Craftr-Compiler");

		silent = false;
	}

	Compiler(File source, String name) {
		this.project = null;
		this.projectName = name;
		this.source = source;

		this.thread = new Thread(this::runCompilation,"Craftr-Compiler");

		silent = true;
	}
	
	public void compile() {
		report = new CompilerReport();
		if(!silent && project == null) {
			report.addNotice(new Notice(NoticeType.ERROR, "No project selected"));
			completionListeners.forEach(Runnable::run);
			return;
		}
		if(!silent && project.getWorld() == null) {
			report.addNotice(new Notice(NoticeType.ERROR, "Project does not have an output directory."));
		}
		this.thread.start();
	}

	private void runCompilation() {
		int point = 0;
		if(point == breakpoint) {
			finalizeCompilation();
			return;
		}
		this.setProgress("Scanning files... [" + projectName + "]");
		ts = new TokenStream();
		sc = new Scanner(source,ts);
		System.out.println(ts.tokens);
		this.getReport().addNotices(sc.getNotices());
		if(sc.getNotices().size() > 0) {
			finalizeCompilation();
			return;
		}
		point++;
		if(point == breakpoint) {
			finalizeCompilation();
			return;
		}
		this.setProgress("Parsing tokens... [" + projectName + "]");
		parser = new Parser(this, ts);
		this.getReport().addNotices(parser.getNotices());
		point++;
		if(point == breakpoint) {
			finalizeCompilation();
			return;
		}
		this.setProgress("Analyzing code... [" + projectName + "]");

		HashMap<File, TokenPattern<?>> allPatterns = new HashMap<>();

		allPatterns.putAll(parser.getFilePatterns());

		analyzer = new SemanticAnalyzer(this, allPatterns, source);
		if(library != null) {
			LibraryLoad callback = (c,r) -> {
				analyzer.join(c.analyzer);
				report.addNotices(r.getAllNotices());
				c.setReport(report);
			};

			library.awaitLib(this, callback, lock);

			try {
				synchronized (lock) {
					while (!lock.condition) {
						lock.wait();
					}
				}
			} catch (InterruptedException x) {
				x.printStackTrace();
			}
		}
		analyzer.start();
		point++;
		if(point == breakpoint) {
			finalizeCompilation();
			return;
		}
		this.setProgress("Compilation completed with " + report.getTotalsString());
		finalizeCompilation();
	}

	public Project getProject() {
		return project;
	}

	private void finalizeCompilation() {
		completionListeners.forEach(Runnable::run);
		progressListeners.clear();
		completionListeners.clear();
	}

	public void addProgressListener(ProgressListener l) {
		progressListeners.add(l);
	}

	private void removeProgressListener(ProgressListener l) {
		progressListeners.remove(l);
	}

	void setProgress(String message) {
		progressListeners.forEach(l -> l.onProgress(message));
	}

	static {
		Console.warn.println("Compiler loaded.");
	}

	public CompilerReport getReport() {
		return report;
	}

	public void setReport(CompilerReport report) {
		this.report = report;
	}

	public void addCompletionListener(Runnable r) {
		completionListeners.add(r);
	}

	public void removeCompletionListener(Runnable r) {
		completionListeners.remove(r);
	}

	public int getBreakpoint() {
		return breakpoint;
	}

	public void setBreakpoint(int breakpoint) {
		this.breakpoint = breakpoint;
	}

	public TokenStream getTokenStream() {
		return ts;
	}

	public Scanner getScanner() {
		return sc;
	}

	public Parser getParser() {
		return parser;
	}

	public SemanticAnalyzer getAnalyzer() {
		return analyzer;
	}

	public Thread getThread() {
		return thread;
	}

	public File getSource() {
		return source;
	}

	public CraftrLibrary getLibrary() {
		return library;
	}

	public void setLibrary(CraftrLibrary library) {
		this.library = library;
	}
}
