package com.energyxxer.craftr.ui.theme.change;

import com.energyxxer.craftr.global.Status;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.util.Disposable;
import com.energyxxer.util.out.Console;

import java.util.ArrayList;
import java.util.List;

public class ThemeListenerManager implements Disposable {
    private List<ThemeChangeListener> listeners = new ArrayList<>();
    private StackTraceElement[] stackTrace;

    public ThemeListenerManager() {
        this.stackTrace = Thread.currentThread().getStackTrace();
    }

    public void addThemeChangeListener(ThemeChangeListener l) {
        addThemeChangeListener(l, false);
    }
    public void addThemeChangeListener(ThemeChangeListener l, boolean priority) {
        ThemeChangeListener.addThemeChangeListener(l, priority);
        listeners.add(l);
    }

    @Override
    public void dispose() {
        if(listeners != null) {
            System.out.println("Disposing of " + listeners.size() + " listeners");
            listeners.forEach(ThemeChangeListener::dispose);
            listeners.clear();
            listeners = null;
        } else {
            StackTraceElement call = stackTrace[2];
            String source = call.getClassName().substring(call.getClassName().lastIndexOf('.')+1) + "." + call.getMethodName() + "(" + call.getFileName() + ":" + call.getLineNumber() + ")";

            CraftrWindow.statusBar.setStatus(new Status(Status.ERROR, "ERROR: Reused ThemeListenerManager '" + source + "'. Please report to Energyxxer. More details in the console."));

            StringBuilder extendedError = new StringBuilder("ERROR: Reused ThemeListenerManager. Please report to Energyxxer. Entire stack trace of the TLM instantiation:\n");
            for(StackTraceElement element : stackTrace) {
                extendedError.append("\tat ");
                extendedError.append(element.toString());
                extendedError.append('\n');
            }

            Console.err.println(extendedError.toString());
        }
    }
}
