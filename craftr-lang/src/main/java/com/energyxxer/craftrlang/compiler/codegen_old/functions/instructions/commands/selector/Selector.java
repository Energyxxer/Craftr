package com.energyxxer.craftrlang.compiler.codegen.functions.instructions.commands.selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Selector {
    public enum BaseSelector {
        ALL_PLAYERS("a"), NEAREST_PLAYER("p"), RANDOM_PLAYER("r"), ALL_ENTITIES("e"), SENDER("s");

        private String header;

        BaseSelector(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }
    }
    private BaseSelector base;

    private ArrayList<SelectorArgument> args = new ArrayList<>();
    public Selector(BaseSelector base) {
        this.base = base;
    }

    public void addArguments(SelectorArgument... arguments) {
        this.args.addAll(Arrays.asList(arguments));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("@");
        sb.append(base.getHeader());
        if(!args.isEmpty()) {
            sb.append('[');
            Iterator<SelectorArgument> it = args.iterator();
            while(it.hasNext()) {
                SelectorArgument arg = it.next();
                sb.append(arg.getArgumentString());
                if(it.hasNext()) sb.append(',');
            }
            sb.append(']');
        }
        return sb.toString();
    }
}
