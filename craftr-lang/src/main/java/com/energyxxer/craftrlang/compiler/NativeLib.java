package com.energyxxer.craftrlang.compiler;

import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.util.ThreadLock;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Energyxxer on 07/30/2017.
 */
public class NativeLib {
    private static File dir = null;
    private static HashMap<File, TokenPattern<?>> libPatterns = null;

    public static void setLib(File dir) {
        if(!dir.isDirectory()) throw new IllegalArgumentException("ERROR: File '" + dir + "' is not a directory. Native libraries must be contained inside a folder");
        NativeLib.dir = dir;
        libPatterns = null;
    }

    public static void awaitLib(LibraryLoad callback, final ThreadLock lock) {
        synchronized(lock) {
            if(dir == null) {
                System.out.println("WARN: No library has been set.");
                lock.condition = true;
                lock.notifyAll();
                callback.onLoad(new HashMap<>());
            } else if(libPatterns == null) {
                Compiler libCompiler = new Compiler(dir);
                libCompiler.setBreakpoint(2);
                libCompiler.addCompletionListener(() -> {
                    libPatterns = libCompiler.getParser().getFilePatterns();
                    callback.onLoad(libPatterns);
                });
                try {
                    libCompiler.compile();
                    libCompiler.getThread().join();
                } catch(InterruptedException x) {
                    x.printStackTrace();
                }
                lock.condition = true;
                lock.notifyAll();
            } else {
                lock.condition = true;
                lock.notifyAll();
                callback.onLoad(libPatterns);
            }
        }
    }
}
