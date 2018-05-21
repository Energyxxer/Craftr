package com.energyxxer.craftrlang.compiler.lexical_analysis.presets;

import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.lexical_analysis.presets.data.craftr.CraftrTokenAttributes;
import com.energyxxer.craftrlang.compiler.lexical_analysis.profiles.ScannerContext;
import com.energyxxer.craftrlang.compiler.lexical_analysis.profiles.ScannerContextResponse;
import com.energyxxer.craftrlang.compiler.lexical_analysis.profiles.ScannerProfile;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenSection;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;
import com.energyxxer.util.StringLocation;
import com.energyxxer.util.out.Console;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.energyxxer.util.StringUtil.FALSE;
import static com.energyxxer.util.StringUtil.TRUE;

/**
 * Defines a profile for the analysis of Craftr files.
 */
public class CraftrScannerProfile extends ScannerProfile {

    /**
     * Contains all the built-in entity names.
     * */
    private static final List<String> entities = new ArrayList<>(CraftrLang.entities),
    /**
     * Contains special cases for blockstates.
     * */
        blockstate_specials = Collections.singletonList("default");

    /**
     * Character used to force-exit out of a blockstate (For use in parameter lists).
     * */
    private static final String blockstate_end = "|";

    static {
        entities.addAll(CraftrLang.abstract_entities);
    }

    /**
     * Creates a Craftr Analysis Profile.
     * */
    public CraftrScannerProfile() {

        //String
        ScannerContext stringContext = new ScannerContext() {

            String delimiters = "\"'`";
            char multiLineDelimiter = '`';

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

                        if(c == '\n' && startingCharacter != multiLineDelimiter) {
                            ScannerContextResponse response = new ScannerContextResponse(true, token.toString(), end, CraftrLang.STRING_LITERAL, escapedChars);
                            response.setError("Illegal line end in string literal", i, 1);
                            return response;
                        }
                        token.append(c);
                        if(c == '\\') {
                            token.append(str.charAt(i+1));
                            escapedChars.put(new TokenSection(i,2), "string_literal.escape");
                            i++;
                        } else if(c == startingCharacter) {
                            return new ScannerContextResponse(true, token.toString(), end, CraftrLang.STRING_LITERAL, escapedChars);
                        }
                    }
                    //Unexpected end of input
                    ScannerContextResponse response = new ScannerContextResponse(true, token.toString(), end, CraftrLang.STRING_LITERAL, escapedChars);
                    response.setError("Unexpected end of input", str.length()-1, 1);
                    return response;
                } else return new ScannerContextResponse(false);
            }
        };

        //Function Comment
        ScannerContext functionCommentContext = new ScannerContext() {

            String functionCommentStart = "/#";

            @Override
            public ScannerContextResponse analyze(String str) {
                if(str.length() <= 0) return new ScannerContextResponse(false);
                if(str.startsWith(functionCommentStart)) {
                    int end = str.indexOf('\n');
                    String fullComment = str;
                    if(end >= 0) fullComment = str.substring(0, end);

                    return new ScannerContextResponse(true, fullComment, CraftrLang.FUNCTION_COMMENT);
                }
                return new ScannerContextResponse(false);
            }
        };

        //Comment
        ScannerContext commentContext = new ScannerContext() {

            String singleLineComment = "//";
            String multiLineCommentStart = "/*";
            String multiLineCommentEnd = "*/";

            @Override
            public ScannerContextResponse analyze(String str) {
                if(str.length() <= 0) return new ScannerContextResponse(false);
                boolean multiline;
                if(str.startsWith(singleLineComment) || str.startsWith(multiLineCommentStart)) {
                    multiline = str.startsWith(multiLineCommentStart);

                    String fullComment;

                    ScannerContextResponse response;

                    if(multiline) {
                        int end = str.substring(multiLineCommentStart.length()).indexOf(multiLineCommentEnd) + multiLineCommentStart.length() + multiLineCommentEnd.length();
                        boolean unclosed = false;
                        if(end < multiLineCommentStart.length() + multiLineCommentEnd.length()) {
                            unclosed = true;
                            end = str.length();
                        }
                        fullComment = str.substring(0,end);
                        StringLocation endLoc = new StringLocation(multiLineCommentStart.length(),0,multiLineCommentStart.length());

                        for(char c : fullComment.substring(multiLineCommentStart.length()).toCharArray()) {
                            if(c == '\n') {
                                endLoc.line++;
                                endLoc.column = 0;
                            } else {
                                endLoc.column++;
                            }
                            endLoc.index++;
                        }

                        response = new ScannerContextResponse(true, fullComment, endLoc, CraftrLang.COMMENT, new HashMap<>());
                        if(unclosed) {
                            response.setError("Unclosed comment", end-1, 1);
                        }
                    } else {
                        int end = str.substring(singleLineComment.length()).indexOf("\n") + singleLineComment.length();
                        if(end < singleLineComment.length()) end = str.length();
                        fullComment = str.substring(0,end);
                        response = new ScannerContextResponse(true, fullComment, CraftrLang.COMMENT, new HashMap<>());
                    }

                    for(int i = 0; i < fullComment.length()-5; i++) {
                        String substr = fullComment.substring(i,i+5);
                        if(substr.equalsIgnoreCase("TODO:")) {
                            Console.info.println("TODO found at " + i);
                            int length = fullComment.indexOf('\n',i) - i;
                            if(length < 0) {
                                length = fullComment.length() - i - ((multiline) ? 2 : 0);
                            }
                            response.subSections.put(new TokenSection(i, length), "comment.todo");
                        }
                    }

                    return response;
                }
                return new ScannerContextResponse(false);
            }
        };

        //Misc
        ScannerContext miscellaneousContext = new ScannerContext() {

            String[] patterns = { "->", ";", ".", ",", ":", "@", "#", "(", ")", "[", "]", "{", "}" };
            TokenType[] types = { CraftrLang.LAMBDA_ARROW, CraftrLang.END_OF_STATEMENT, CraftrLang.DOT, CraftrLang.COMMA, CraftrLang.COLON, CraftrLang.ANNOTATION_MARKER, CraftrLang.BLOCKSTATE_MARKER, CraftrLang.BRACE, CraftrLang.BRACE, CraftrLang.BRACE, CraftrLang.BRACE, CraftrLang.BRACE, CraftrLang.BRACE };

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

        //Operators
        ScannerContext operatorContext = new ScannerContext() {
            private List<String> identifier_operators = Arrays.asList("++", "--");
            private List<String> operators = Arrays.asList("+=", "+", "-=", "-", "*=", "*", "/=", "/", "%=", "%", "<=", ">=", "!=", "==", "=", "<", ">", "&&", "||", "&=", "|=", "instanceof");
            private String logical_negation_operator = "!";

            @Override
            public ScannerContextResponse analyze(String str) {
                if(str.length() <= 0) return new ScannerContextResponse(false);
                for(String o : identifier_operators) {
                    if(str.startsWith(o)) {
                        return new ScannerContextResponse(true, o, CraftrLang.IDENTIFIER_OPERATOR);
                    }
                }
                for(String o : operators) {
                    if(str.startsWith(o)) {
                        return new ScannerContextResponse(true, o, CraftrLang.OPERATOR);
                    }
                }
                if(str.startsWith(logical_negation_operator)) {
                    return new ScannerContextResponse(true, logical_negation_operator, CraftrLang.LOGICAL_NEGATION_OPERATOR);
                }
                return new ScannerContextResponse(false);
            }
        };

        //Numbers
        ScannerContext numberContext = new ScannerContext() {

            private Pattern regex = Pattern.compile("(\\d+(\\.\\d+)?[bdfsL]?)");

            @Override
            public ScannerContextResponse analyze(String str) {
                Matcher matcher = regex.matcher(str);

                if(matcher.lookingAt()) {
                    int length = matcher.end();
                    return new ScannerContextResponse(true, str.substring(0,length), CraftrLang.NUMBER);
                } else return new ScannerContextResponse(false);
            }

            @Override
            public ContextCondition getCondition() {
                return ContextCondition.LEADING_WHITESPACE;
            }
        };

        ArrayList<ScannerContext> craftrContexts = new ArrayList<>();
        craftrContexts.add(stringContext);
        craftrContexts.add(functionCommentContext);
        craftrContexts.add(commentContext);
        craftrContexts.add(miscellaneousContext);
        craftrContexts.add(operatorContext);
        craftrContexts.add(numberContext);
        this.contexts = craftrContexts;
    }

    @Override
    public boolean canMerge(char ch0, char ch1) {
        return Character.isJavaIdentifierPart(ch0) && Character.isJavaIdentifierPart(ch1);
    }

    @Override
    public boolean filter(Token token) {
        this.classifyKeyword(token);
        this.giveAttributes(token);

        return analyzeBlockstate(token) | analyzeAnnotation(token);
    }

    /**
     * Holds information for multi-token analysis.
     * */
    private HashMap<String, String> bufferData = new HashMap<>();
    /**
     * Holds previous tokens for multi-token analysis.
     * */
    private ArrayList<Token> tokenBuffer = new ArrayList<>();

    {
        bufferData.put("IS_ANNOTATION", FALSE);
        bufferData.put("ANNOTATION_PHASE", "NONE");

        bufferData.put("IS_BLOCKSTATE", FALSE);
        bufferData.put("BLOCKSTATE_PHASE", "NONE");
    }

    /**
     * Combines various tokens into one blockstate-type token.
     *
     * @param token The token to be analyzed.
     *
     * @return true if the token should be skipped, false otherwise.
     * */
    private boolean analyzeBlockstate(Token token) {

        boolean cancel = false;

        if(token.type == CraftrLang.BLOCKSTATE_MARKER && bufferData.get("BLOCKSTATE_PHASE").equals("NONE")) {
            bufferData.put("IS_BLOCKSTATE", TRUE);
            bufferData.put("BLOCKSTATE_PHASE", "KEY_FIRST");
            tokenBuffer.add(token);
            cancel = true;
        } else if((token.type == CraftrLang.IDENTIFIER && bufferData.get("BLOCKSTATE_PHASE").startsWith("KEY")) || (blockstate_specials.contains(token.value) && bufferData.get("BLOCKSTATE_PHASE").equals("KEY_FIRST"))) {
            tokenBuffer.add(token);
            cancel = true;
            if(blockstate_specials.contains(token.value)) {
                //Is special (#default...)
                token.type = CraftrLang.IDENTIFIER;
                this.stream.write(Token.merge(CraftrLang.BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
                tokenBuffer.clear();

                bufferData.put("IS_BLOCKSTATE", FALSE);
                bufferData.put("BLOCKSTATE_PHASE", "NONE");
            } else {
                //Not special (#variant...)

                bufferData.put("BLOCKSTATE_PHASE", "EQUALS");
            }
        } else if(token.value.equals("=") && bufferData.get("BLOCKSTATE_PHASE").equals("EQUALS")) {
            tokenBuffer.add(token);
            bufferData.put("BLOCKSTATE_PHASE", "VALUE");
            cancel = true;
        } else if((token.type == CraftrLang.IDENTIFIER || token.type == CraftrLang.BOOLEAN) && bufferData.get("BLOCKSTATE_PHASE").equals("VALUE")) {
            tokenBuffer.add(token);
            bufferData.put("BLOCKSTATE_PHASE", "NEXT");
            cancel = true;
        } else if(bufferData.get("BLOCKSTATE_PHASE").equals("NEXT")) {
            if(token.type == CraftrLang.COMMA) {
                tokenBuffer.add(token);
                bufferData.put("BLOCKSTATE_PHASE", "KEY");
                cancel = true;
            } else if(token.value.equals(blockstate_end)) {
                tokenBuffer.add(token);
                stream.write(Token.merge(CraftrLang.BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
                tokenBuffer.clear();

                bufferData.put("IS_BLOCKSTATE", FALSE);
                bufferData.put("BLOCKSTATE_PHASE", "NONE");
                cancel = true;
            } else {
                stream.write(Token.merge(CraftrLang.BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
                tokenBuffer.clear();

                bufferData.put("IS_BLOCKSTATE", FALSE);
                bufferData.put("BLOCKSTATE_PHASE", "NONE");
            }
        } else if(bufferData.get("IS_BLOCKSTATE") == TRUE) {
            //Whoops something went wrong

            stream.write(Token.merge(CraftrLang.BLOCKSTATE, tokenBuffer.toArray(new Token[0])),true);
            tokenBuffer.clear();

            bufferData.put("IS_BLOCKSTATE", FALSE);
            bufferData.put("BLOCKSTATE_PHASE", "NONE");

        }

        return cancel;
    }

    /**
     * Analyzes tokens and gives them annotation attributes.
     *
     * @param token The token to be analyzed.
     *
     * @return true if the token should be skipped, false otherwise.
     * */
    private boolean analyzeAnnotation(Token token) {
        if(token.type == CraftrLang.ANNOTATION_MARKER && bufferData.get("ANNOTATION_PHASE").equals("NONE")) {
            bufferData.put("IS_ANNOTATION", TRUE);
            bufferData.put("ANNOTATION_PHASE", "ANNOTATION");
        }
        if(bufferData.get("IS_ANNOTATION") == TRUE) {
            token.attributes.put(CraftrTokenAttributes.IS_ANNOTATION, true);
            switch(bufferData.get("ANNOTATION_PHASE")) {
                case "ANNOTATION": {
                    bufferData.put("ANNOTATION_PHASE", "IDENTIFIER");
                    break;
                } case "IDENTIFIER": {
                    if(token.type == CraftrLang.IDENTIFIER) {
                        token.attributes.put(CraftrTokenAttributes.IS_ANNOTATION_HEADER, true);
                        bufferData.put("ANNOTATION_PHASE", "BRACE_OPEN");
                    } else {
                        bufferData.put("IS_ANNOTATION", FALSE);
                        bufferData.put("ANNOTATION_PHASE", "NONE");
                    }
                    break;
                } case "BRACE_OPEN": {
                    if(token.type == CraftrLang.BRACE && token.attributes.get("BRACE_STYLE") == CraftrTokenAttributes.PARENTHESES && token.attributes.get("BRACE_TYPE") == CraftrTokenAttributes.OPENING_BRACE) {
                        bufferData.put("ANNOTATION_PHASE", "BRACE_CLOSE");
                    } else {
                        bufferData.put("IS_ANNOTATION", FALSE);
                        bufferData.put("ANNOTATION_PHASE", "NONE");
                    }
                    break;
                } case "BRACE_CLOSE": {
                    if(token.type == CraftrLang.BRACE && token.attributes.get("BRACE_STYLE") == CraftrTokenAttributes.PARENTHESES && token.attributes.get("BRACE_TYPE") == CraftrTokenAttributes.CLOSING_BRACE) {
                        bufferData.put("IS_ANNOTATION", FALSE);
                        bufferData.put("ANNOTATION_PHASE", "NONE");
                    }
                    break;
                } case "NONE": {
                    break;
                } default: {
                    bufferData.put("IS_ANNOTATION", FALSE);
                    bufferData.put("ANNOTATION_PHASE", "NONE");
                }
            }
        }
        return false;
    }

    /**
     * Analyzes the given token and gives it its relevant attributes.
     *
     * @param token The token to be analyzed.
     * */
    private void giveAttributes(Token token) {
        token.attributes.put(CraftrTokenAttributes.IS_PSEUDO_KEYWORD, CraftrLang.pseudo_keywords.contains(token.value));

        //Entities
        if(token.type == CraftrLang.IDENTIFIER) {
            token.attributes.put(CraftrTokenAttributes.IS_ENTITY, entities.contains(token.value));
        }
        //Braces
        if(token.type == CraftrLang.BRACE) {
            if("()".contains(token.value)) token.attributes.put("BRACE_STYLE", CraftrTokenAttributes.PARENTHESES);
            if("{}".contains(token.value)) token.attributes.put("BRACE_STYLE", CraftrTokenAttributes.CURLY_BRACES);
            if("[]".contains(token.value)) token.attributes.put("BRACE_STYLE", CraftrTokenAttributes.SQUARE_BRACES);

            if("({[".contains(token.value)) token.attributes.put("BRACE_TYPE", CraftrTokenAttributes.OPENING_BRACE);
            if(")}]".contains(token.value)) token.attributes.put("BRACE_TYPE", CraftrTokenAttributes.CLOSING_BRACE);
        }
        //Modifiers
        if(token.type == CraftrLang.MODIFIER) {
            token.attributes.put("MODIFIER_VALUE", CraftrLang.Modifier.valueOf(token.value.toUpperCase()));
        }
    }

    /**
     * Analyzes an identifier token and changes its type accordingly,
     * based on its text value.
     *
     * @param token The token to be classified.
     * */
    private void classifyKeyword(Token token) {
        if(token.type != TokenType.UNKNOWN) return;
        token.type = CraftrLang.classify(token.value);
    }

    @Override
    public void putHeaderInfo(Token header) {
        header.attributes.put("TYPE","craftr");
        header.attributes.put("DESC","Craftr Unit File");
    }
}