package com.energyxxer.craftr.compiler.lexical_analysis.presets;

import com.energyxxer.craftr.compiler.lexical_analysis.profiles.ScannerContext;
import com.energyxxer.craftr.compiler.lexical_analysis.profiles.ScannerContextResponse;
import com.energyxxer.craftr.compiler.lexical_analysis.profiles.ScannerProfile;
import com.energyxxer.craftr.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftr.compiler.lexical_analysis.token.TokenSection;
import com.energyxxer.craftr.compiler.lexical_analysis.token.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 2/6/2017.
 */
public class JSONScannerProfile extends ScannerProfile {

    /**
     * Holds the previous token for multi-token analysis.
     * */
    private Token tokenBuffer = null;

    /**
     * Creates a JSON Analysis Profile.
     * */
    public JSONScannerProfile() {
        //String
        ScannerContext stringContext = new ScannerContext() {

            String delimiters = "\"'";

            @Override
            public ScannerContextResponse analyze(String str) {
                if(str.length() <= 0) return new ScannerContextResponse(false);
                char startingCharacter = str.charAt(0);

                if(delimiters.contains(Character.toString(startingCharacter))) {

                    StringBuilder token = new StringBuilder(Character.toString(startingCharacter));

                    HashMap<TokenSection, String> escapedChars = new HashMap<>();

                    for(int i = 1; i < str.length(); i++) {
                        char c = str.charAt(i);

                        if(c == '\n') {
                            throw new RuntimeException("Illegal line end in string literal");
                        }
                        token.append(c);
                        if(c == '\\') {
                            token.append(str.charAt(i+1));
                            escapedChars.put(new TokenSection(i,2), "string_literal.escape");
                            i++;
                        } else if(c == startingCharacter) {
                            return new ScannerContextResponse(true, token.toString(), TokenType.STRING_LITERAL, escapedChars);
                        }
                    }
                    //Unexpected end of input
                    throw new RuntimeException("Unexpected end of input");
                } else return new ScannerContextResponse(false);
            }
        };
        //Numbers
        ScannerContext numberContext = new ScannerContext() {

            private Pattern regex = Pattern.compile("(-?\\d+(\\.\\d+)?)");

            @Override
            public ScannerContextResponse analyze(String str) {
                Matcher matcher = regex.matcher(str);

                if(matcher.lookingAt()) {
                    int length = matcher.end();
                    return new ScannerContextResponse(true, str.substring(0,length), TokenType.NUMBER);
                } else return new ScannerContextResponse(false);
            }
        };
        //Braces
        ScannerContext braceContext = (str) -> {
            if(str.length() <= 0) return new ScannerContextResponse(false);
            if("[]{}".contains(str.substring(0,1))) {
                return new ScannerContextResponse(true, str.substring(0,1), TokenType.BRACE);
            }
            return new ScannerContextResponse(false);
        };

        //Misc
        ScannerContext miscellaneousContext = new ScannerContext() {

            String[] patterns = { ",", ":" };
            String[] types = { TokenType.COMMA, TokenType.COLON };

            @Override
            public ScannerContextResponse analyze(String str) {
                if(str.length() <= 0) return new ScannerContextResponse(false);
                for(int i = 0; i < patterns.length; i++) {
                    if(str.startsWith(patterns[i])) {
                        return new ScannerContextResponse(true, patterns[i], types[i]);
                    }
                }
                return new ScannerContextResponse(false);
            }
        };

        ArrayList<ScannerContext> jsonContexts = new ArrayList<>();
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
        if(token.type == TokenType.IDENTIFIER) {
            if(token.value.equals("true") || token.value.equals("false")) {
                token.type = TokenType.BOOLEAN;
            }
        }
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
