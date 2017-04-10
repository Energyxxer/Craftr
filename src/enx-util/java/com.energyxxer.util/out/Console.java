package com.energyxxer.util.out;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by User on 1/8/2017.
 */
public class Console {

    public static final PrintStream info;
    public static final PrintStream warn;
    public static final PrintStream err;
    public static final PrintStream debug;

    private static final MultiOutputStream infoOut;
    private static final MultiOutputStream warnOut;
    private static final MultiOutputStream errOut;
    private static final MultiOutputStream debugOut;

    static {
        info = new PrintStream(infoOut = new MultiOutputStream(System.out));
        warn = new PrintStream(warnOut = new MultiOutputStream(System.out));
        err = new PrintStream(errOut = new MultiOutputStream(System.err));
        debug = new PrintStream(debugOut = new MultiOutputStream(System.out));
    }

    public static void addInfoStream(OutputStream os) {
        infoOut.addStream(os);
    }

    public static void addWarnStream(OutputStream os) {
        warnOut.addStream(os);
    }

    public static void addErrStream(OutputStream os) {
        errOut.addStream(os);
    }

    public static void addDebugStream(OutputStream os) {
        debugOut.addStream(os);
    }
}
