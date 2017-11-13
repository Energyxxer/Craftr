package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.selector;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScoreArgument implements SelectorArgument {

    private HashMap<String, SelectorNumberArgument> scores = new HashMap<>();

    public ScoreArgument(HashMap<String, SelectorNumberArgument> scores) {
        this.scores = scores;
    }

    public void put(String objective, SelectorNumberArgument value) {
        this.scores.put(objective, value);
    }

    @Override
    public String getArgumentString() {
        StringBuilder sb = new StringBuilder("score={");
        Iterator<Map.Entry<String, SelectorNumberArgument>> it = this.scores.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String, SelectorNumberArgument> entry = it.next();

            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            if(it.hasNext()) sb.append(',');
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }
}
