package com.energyxxer.craftrlang.compiler;

import com.energyxxer.commodore.module.ModulePackGenerator;
import com.energyxxer.commodore.standard.StandardDefinitionPacks;
import com.energyxxer.craftrlang.compiler.parsing.Parser;
import com.energyxxer.craftrlang.compiler.semantic_analysis.SemanticAnalyzer;
import com.energyxxer.craftrlang.interfaces.ProgressListener;
import com.energyxxer.craftrlang.projects.Project;
import com.energyxxer.craftrlang.projects.ProjectManager;
import com.energyxxer.enxlex.lexical_analysis.Scanner;
import com.energyxxer.enxlex.lexical_analysis.token.TokenStream;
import com.energyxxer.enxlex.pattern_matching.structures.TokenPattern;
import com.energyxxer.enxlex.report.Notice;
import com.energyxxer.enxlex.report.NoticeType;
import com.energyxxer.util.StringUtil;
import com.energyxxer.util.ThreadLock;
import com.energyxxer.util.out.Console;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Compiler {
    private final Project project;
    private final String projectName;
    private final String projectPrefix;
    private final File projectOutput;
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

    private CraftrCommandModule module;

    private Thread thread = null;

    private final ThreadLock lock = new ThreadLock();
    private CraftrLibrary library = null;
    
    public Compiler(Project project) {
        this.project = project;
        this.projectName = project.getName();
        this.projectPrefix = project.getPrefix();
        this.projectOutput = (project.getWorld() != null) ? new File(project.getWorld()) : null;
        this.source = project.getSource();

        this.thread = new Thread(this::runCompilation,"Craftr-Compiler[" + projectName + "]");

        silent = false;
    }

    Compiler(File source, String name, String prefix) {
        this.project = null;
        this.projectName = name;
        this.projectPrefix = prefix;
        this.projectOutput = null;
        this.source = source;

        this.thread = new Thread(this::runCompilation,"Craftr-Compiler[" + projectName + "]");

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
        sc = new Scanner(ts);
        recursivelyParse(sc, source);

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

        HashMap<File, TokenPattern<?>> allPatterns = new HashMap<>(parser.getFilePatterns());

        analyzer = new SemanticAnalyzer(this, allPatterns, source);
        module = new CraftrCommandModule(projectName, projectPrefix);
        this.setProgress("Importing data definitions... [" + projectName + "]");

        try {
            module.importDefinitions(StandardDefinitionPacks.MINECRAFT_J_1_13);
        } catch(IOException x) {
            x.printStackTrace();
        }

        if(library != null) {
            LibraryLoad callback = (c,r) -> {
                module.join(c.module);
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
        this.setProgress("Analyzing code... [" + projectName + "]");
        analyzer.start();
        point++;
        if(point == breakpoint) {
            finalizeCompilation();
            return;
        }
        module.getObjectiveManager().setCreationFunction(module.projectNS.getFunctionManager().create("init"));
        if(projectOutput != null && !report.hasErrors()) try {
            module.compile(projectOutput, ModulePackGenerator.OutputType.FOLDER);
        } catch(IOException x) {
            x.printStackTrace();
        }
        this.setProgress("Compilation completed with " + report.getTotalsString());
        finalizeCompilation();
    }

    private void recursivelyParse(Scanner sc, File dir) {
        File[] files = dir.listFiles();
        if(files == null) return;
        for (File file : files) {
            String name = file.getName();
            if (file.isDirectory()) {
                if(!file.getName().equals("resources") || !file.getParentFile().getParent().equals(ProjectManager.getWorkspaceDir())) {
                    //This is not the resource pack directory.
                    recursivelyParse(sc, file);
                }
            } else {
                Lang fileLang = Lang.getLangForFile(name);
                if(fileLang == null) continue;

                try {
                    String str = new String(Files.readAllBytes(Paths.get(file.getPath())));
                    sc.tokenize(file, str, fileLang.createProfile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Project getProject() {
        return project;
    }

    private String randomPrefix = StringUtil.getRandomString(3);

    public String getPrefix() {
        if(projectPrefix != null) {
            return projectPrefix;
        }
        if(project != null) {
            return project.getPrefix();
        }
        return randomPrefix;
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

    public CraftrCommandModule getModule() {
        return module;
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
