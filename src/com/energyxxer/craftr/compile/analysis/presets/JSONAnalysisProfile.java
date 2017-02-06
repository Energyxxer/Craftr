package com.energyxxer.craftr.compile.analysis.presets;

import com.energyxxer.craftr.compile.analysis.profiles.AnalysisContext;
import com.energyxxer.craftr.compile.analysis.profiles.AnalysisContextResponse;
import com.energyxxer.craftr.compile.analysis.profiles.AnalysisProfile;
import com.energyxxer.craftr.compile.analysis.token.Token;
import com.energyxxer.craftr.compile.analysis.token.TokenType;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 2/6/2017.
 */
public class JSONAnalysisProfile extends AnalysisProfile {

    private Token tokenBuffer = null;

    public JSONAnalysisProfile() {
        //String
        AnalysisContext stringContext = new AnalysisContext() {

            String delimiters = "\"'";

            @Override
            public AnalysisContextResponse analyze(String str) {
                if(str.length() <= 0) return new AnalysisContextResponse(false);
                char startingCharacter = str.charAt(0);

                if(delimiters.contains(Character.toString(startingCharacter))) {

                    StringBuilder token = new StringBuilder(Character.toString(startingCharacter));

                    for(int i = 1; i < str.length(); i++) {
                        char c = str.charAt(i);

                        if(c == '\n') {
                            throw new RuntimeException("Illegal line end in string literal");
                        }
                        token.append(c);
                        if(c == '\\') {
                            token.append(str.charAt(i+1));
                            i++;
                        } else if(c == startingCharacter) {
                            return new AnalysisContextResponse(true, token.toString(), TokenType.STRING_LITERAL);
                        }
                    }
                    //Unexpected end of input
                    throw new RuntimeException("Unexpected end of input");
                } else return new AnalysisContextResponse(false);
            }
        };
        //Numbers
        AnalysisContext numberContext = new AnalysisContext() {

            private Pattern regex = Pattern.compile("(\\d+(\\.\\d+)?)");

            @Override
            public AnalysisContextResponse analyze(String str) {
                Matcher matcher = regex.matcher(str);

                if(matcher.lookingAt()) {
                    int length = matcher.end();
                    return new AnalysisContextResponse(true, str.substring(0,length), TokenType.NUMBER);
                } else return new AnalysisContextResponse(false);
            }
        };
        //Braces
        AnalysisContext braceContext = (str) -> {
            if(str.length() <= 0) return new AnalysisContextResponse(false);
            if("[]{}".contains(str.substring(0,1))) {
                return new AnalysisContextResponse(true, str.substring(0,1), TokenType.BRACE);
            }
            return new AnalysisContextResponse(false);
        };

        //Misc
        AnalysisContext miscellaneousContext = new AnalysisContext() {

            String[] patterns = { ",", ":" };
            String[] types = { TokenType.COMMA, TokenType.COLON };

            @Override
            public AnalysisContextResponse analyze(String str) {
                if(str.length() <= 0) return new AnalysisContextResponse(false);
                for(int i = 0; i < patterns.length; i++) {
                    if(str.startsWith(patterns[i])) {
                        return new AnalysisContextResponse(true, patterns[i], types[i]);
                    }
                }
                return new AnalysisContextResponse(false);
            }
        };

        ArrayList<AnalysisContext> jsonContexts = new ArrayList<>();
        jsonContexts.add(stringContext);
        jsonContexts.add(braceContext);
        jsonContexts.add(miscellaneousContext);
        jsonContexts.add(numberContext);
        this.contexts = jsonContexts;
    }

    @Override
    public boolean canMerge(char ch0, char ch1) {
        return Character.isJavaIdentifierPart(ch0) && Character.isJavaIdentifierPart(ch1);
    }

    @Override
    public boolean filter(Token token) {
        if(token.type == TokenType.STRING_LITERAL) {
            if(tokenBuffer != null) this.stream.write(tokenBuffer, true);
            tokenBuffer = token;
            return true;
        }
        if(token.type == TokenType.COLON && tokenBuffer != null) {
            tokenBuffer.attributes.put("IS_PROPERTY",true);
            this.stream.write(tokenBuffer, true);
            tokenBuffer = null;
            return false;
        }
        if(tokenBuffer != null) {
            this.stream.write(tokenBuffer, true);
            tokenBuffer = null;
        }
        return false;
    }

    @Override
    public void putHeaderInfo(Token header) {
        header.attributes.put("TYPE","json");
        header.attributes.put("DESC","JavaScript Object Notation File");
    }
}
