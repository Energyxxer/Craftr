package com.energyxxer.craftr.compile.analysis.profiles;

import com.energyxxer.craftr.compile.analysis.token.Token;
import com.energyxxer.craftr.compile.analysis.token.TokenStream;

import java.util.List;

/**
 * Created by User on 2/4/2017.
 */
public abstract class AnalysisProfile {
    public List<AnalysisContext> contexts;
    protected TokenStream stream = null;

    public boolean canMerge(char ch0, char ch1) {
        return false;
    }

    public boolean filter(Token token) {
        return true;
    }

    public boolean isSignificant(Token token) {
        return true;
    }

    public void setStream(TokenStream stream) {
        this.stream = stream;
    }

    public abstract void putHeaderInfo(Token header);
}
