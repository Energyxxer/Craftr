package com.energyxxer.craftrlang.compiler;

import com.energyxxer.util.ThreadLock;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by Energyxxer on 07/30/2017.
 */
public class CraftrLibrary {
    private final File dir;
    private Compiler compiler = null;

    public CraftrLibrary(@NotNull File dir) {
        if(!dir.isDirectory()) throw new IllegalArgumentException("ERROR: File '" + dir + "' is not a directory. Native libraries must be contained inside a folder");
        this.dir = dir;
    }

    public void awaitLib(LibraryLoad callback, final ThreadLock lock) {
        synchronized(lock) {
            lock.condition = false;
            if(compiler == null) {
                compiler = new Compiler(dir);
                compiler.setBreakpoint(3);
                compiler.addCompletionListener(() -> callback.onLoad(compiler));
                try {
                    compiler.compile();
                    compiler.getThread().join();
                } catch(InterruptedException x) {
                    x.printStackTrace();
                }
                lock.condition = true;
                lock.notifyAll();
            } else {
                lock.condition = true;
                lock.notifyAll();
                callback.onLoad(compiler);
            }
        }
    }

    public Compiler getCompiler() {
        return compiler;
    }
}
