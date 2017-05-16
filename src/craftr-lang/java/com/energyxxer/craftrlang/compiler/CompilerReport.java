package com.energyxxer.craftrlang.compiler;

import com.energyxxer.craftrlang.compiler.report.Notice;

import java.util.ArrayList;

/**
 * Created by User on 5/15/2017.
 */
public class CompilerReport {
    private final ArrayList<Notice> errors = new ArrayList<>();
    private final ArrayList<Notice> warnings = new ArrayList<>();
    private final ArrayList<Notice> info = new ArrayList<>();

    public CompilerReport() {
    }

    public void addNotice(Notice n) {
        switch(n.getType()) {
            case ERROR: {
                errors.add(n);
                break;
            }
            case WARNING: {
                warnings.add(n);
                break;
            }
            case INFO: {
                info.add(n);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported notice type " + n);
            }
        }
    }

    public ArrayList<Notice> getErrors() {
        return errors;
    }

    public ArrayList<Notice> getWarnings() {
        return warnings;
    }

    public ArrayList<Notice> getInfo() {
        return info;
    }

    public String getTotals() {
        int errorCount = errors.size();
        int warningsCount = warnings.size();

        return "" + ((errorCount == 0) ? "no" : errorCount) + " error" + ((errorCount == 1) ? "" : "s") + " and " + ((warningsCount == 0) ? "no" : warningsCount) + " warning" + ((warningsCount == 1) ? "" : "s");
    }
}
