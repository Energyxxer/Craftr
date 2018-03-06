package com.energyxxer.craftrlang.compiler.lexical_analysis.presets;

import com.energyxxer.craftrlang.compiler.lexical_analysis.presets.mcfunction.MCFunction;
import com.energyxxer.craftrlang.compiler.lexical_analysis.profiles.ScannerContext;
import com.energyxxer.craftrlang.compiler.lexical_analysis.profiles.ScannerContextResponse;
import com.energyxxer.craftrlang.compiler.lexical_analysis.profiles.ScannerProfile;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenSection;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;
import com.energyxxer.util.StringLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCFunctionScannerProfile extends ScannerProfile {

    public MCFunctionScannerProfile() {

        ArrayList<ScannerContext> contexts = new ArrayList<>();

        contexts.add(new ScannerContext() {

            private String headers = "pears";

            @Override
            public ScannerContextResponse analyze(String str) {
                if(str.length() < 2) return new ScannerContextResponse(false);
                if(!str.startsWith("@")) return new ScannerContextResponse(false);
                if(headers.contains(str.charAt(1) + "")) {
                    return new ScannerContextResponse(true, str.substring(0,2), MCFunction.SELECTOR_HEADER);
                }
                return new ScannerContextResponse(false);
            }

            @Override
            public ContextCondition getCondition() {
                return ContextCondition.LEADING_WHITESPACE;
            }
        });
        
        contexts.add(new ScannerContext() {

            String delimiters = "\"";

            @Override
            public ScannerContextResponse analyze(String str) {
                if(str.length() <= 0) return new ScannerContextResponse(false);
                char startingCharacter = str.charAt(0);

                if(delimiters.contains(Character.toString(startingCharacter))) {

                    StringBuilder token = new StringBuilder(Character.toString(startingCharacter));
                    StringLocation end = new StringLocation(1,0,1);

                    HashMap<TokenSection, String> escapedChars = new HashMap<>();

                    for(int i = 1; i < str.length(); i++) {
                        char c = str.charAt(i);

                        if(c == '\n') {
                            end.line++;
                            end.column = 0;
                        } else {
                            end.column++;
                        }
                        end.index++;

                        if(c == '\n') {
                            ScannerContextResponse response = new ScannerContextResponse(true, token.toString(), end, MCFunction.STRING_LITERAL, escapedChars);
                            response.setError("Illegal line end in string literal", i, 1);
                            return response;
                        }
                        token.append(c);
                        if(c == '\\') {
                            token.append(str.charAt(i+1));
                            escapedChars.put(new TokenSection(i,2), "string_literal.escape");
                            i++;
                        } else if(c == startingCharacter) {
                            return new ScannerContextResponse(true, token.toString(), end, MCFunction.STRING_LITERAL, escapedChars);
                        }
                    }
                    //Unexpected end of input
                    ScannerContextResponse response = new ScannerContextResponse(true, token.toString(), end, MCFunction.STRING_LITERAL, escapedChars);
                    response.setError("Unexpected end of input", str.length()-1, 1);
                    return response;
                } else return new ScannerContextResponse(false);
            }
        });

        contexts.add(new ScannerContext() {
            @Override
            public ScannerContextResponse analyze(String str) {
                if(!str.startsWith("#")) return new ScannerContextResponse(false);
                if(str.contains("\n")) {
                    return new ScannerContextResponse(true, str.substring(0, str.indexOf("\n")), MCFunction.COMMENT);
                } else return new ScannerContextResponse(true, str, MCFunction.COMMENT);
            }

            @Override
            public ContextCondition getCondition() {
                return ContextCondition.LINE_START;
            }
        });
        
        contexts.add(new ScannerContext() {

            String[] patterns = { ".", ",", ":", "=", "(", ")", "[", "]", "{", "}", "~", "^", "\n" };
            TokenType[] types = { MCFunction.DOT, MCFunction.COMMA, MCFunction.EQUALS, MCFunction.COLON, MCFunction.BRACE, MCFunction.BRACE, MCFunction.BRACE, MCFunction.BRACE, MCFunction.BRACE, MCFunction.BRACE, MCFunction.TILDE, MCFunction.CARET, MCFunction.NEWLINE };

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
        });
        
        contexts.add(new ScannerContext() {

            private Pattern regex = Pattern.compile("([+-]?\\d+(\\.\\d+)?[bdfsL]?)");

            @Override
            public ScannerContextResponse analyze(String str) {
                Matcher matcher = regex.matcher(str);

                if(matcher.lookingAt()) {
                    int length = matcher.end();
                    return new ScannerContextResponse(true, str.substring(0,length), (Character.isLetter(str.charAt(length-1)) ? MCFunction.TYPED_NUMBER : ((str.substring(0, length).contains(".")) ? MCFunction.REAL_NUMBER : MCFunction.INTEGER_NUMBER)));
                } else return new ScannerContextResponse(false);
            }

            @Override
            public ContextCondition getCondition() {
                return ContextCondition.LEADING_WHITESPACE;
            }
        });
        
        this.contexts = contexts;
    }

    @Override
    public boolean canMerge(char ch0, char ch1) {
        return Character.isJavaIdentifierPart(ch0) && Character.isJavaIdentifierPart(ch1);
    }

    @Override
    public void putHeaderInfo(Token header) {
        header.attributes.put("TYPE","mcfunction");
        header.attributes.put("DESC","Minecraft Function File");
    }
}
