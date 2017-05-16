package com.energyxxer.craftrlang.compiler.parsing;


import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenType;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenGroupMatch;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenItemMatch;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenListMatch;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.matching.TokenStructureMatch;

/**
 * Class containing the definitions of each production of the Craftr language.
 * It holds certain token structure patterns to match against a list of
 * tokens (It's called a production, 2016 me).
 *
 * //Apparently this is the entire parser. Yay, I guess?
 * */
public class CraftrProductions {

    public static final TokenStructureMatch FILE;

	public static final TokenStructureMatch PACKAGE;
	public static final TokenStructureMatch IMPORT;

	public static final TokenStructureMatch UNIT;
    public static final TokenStructureMatch UNIT_DECLARATION;
    public static final TokenStructureMatch UNIT_BODY;
    public static final TokenStructureMatch UNIT_COMPONENT;

    public static final TokenStructureMatch FIELD;
    public static final TokenStructureMatch METHOD;

    public static final TokenStructureMatch VARIABLE_DECLARATION;

	public static final TokenStructureMatch IDENTIFIER;
	public static final TokenStructureMatch CONSTRUCTOR_KEYWORD;

    public static final TokenStructureMatch DATA_TYPE;
	public static final TokenStructureMatch METHOD_CALL;
	public static final TokenStructureMatch VALUE;
	public static final TokenStructureMatch EXPRESSION;
	public static final TokenStructureMatch STATEMENT;
	public static final TokenStructureMatch CODE_BLOCK;
	public static final TokenStructureMatch CONSTRUCTOR_BODY;
	public static final TokenStructureMatch ANNOTATION;

	public static final TokenStructureMatch LAMBDA;

	public static final TokenGroupMatch IF_STATEMENT;
	public static final TokenGroupMatch FOR_STATEMENT;
	public static final TokenGroupMatch WHILE_STATEMENT;
	public static final TokenGroupMatch SWITCH_STATEMENT;
	public static final TokenGroupMatch RETURN_EXPRESSION;

	public static final TokenStructureMatch NESTED_POINTER;
	public static final TokenStructureMatch POINTER;

	public static final TokenStructureMatch JSON_OBJECT;

	public static final TokenGroupMatch COMPOUND;
	public static final TokenGroupMatch LIST;

	static {

	    FILE = new TokenStructureMatch("Oh you couldn't even get the first token right!");

		IMPORT = new TokenStructureMatch("IMPORT");
		PACKAGE = new TokenStructureMatch("PACKAGE");

		UNIT = new TokenStructureMatch("UNIT");

		UNIT_DECLARATION = new TokenStructureMatch("UNIT_DECLARATION");
        UNIT_BODY = new TokenStructureMatch("UNIT_BODY");
        UNIT_COMPONENT = new TokenStructureMatch("UNIT_COMPONENT");

        FIELD = new TokenStructureMatch("FIELD");
        METHOD = new TokenStructureMatch("METHOD");

		IDENTIFIER = new TokenStructureMatch("IDENTIFIER");
		CONSTRUCTOR_KEYWORD = new TokenStructureMatch("CONSTRUCTOR_KEYWORD");

		VARIABLE_DECLARATION = new TokenStructureMatch("VARIABLE_DECLARATION");

		DATA_TYPE = new TokenStructureMatch("DATA_TYPE");
		EXPRESSION = new TokenStructureMatch("EXPRESSION");
		VALUE = new TokenStructureMatch("VALUE");
		METHOD_CALL = new TokenStructureMatch("METHOD_CALL");
		STATEMENT = new TokenStructureMatch("STATEMENT");
		CODE_BLOCK = new TokenStructureMatch("CODE_BLOCK");
		CONSTRUCTOR_BODY = new TokenStructureMatch("CONSTRUCTOR_BODY");
		ANNOTATION = new TokenStructureMatch("ANNOTATION");

		LAMBDA = new TokenStructureMatch("LAMBDA");

		NESTED_POINTER = new TokenStructureMatch("NESTED_POINTER");
		POINTER = new TokenStructureMatch("POINTER");
		JSON_OBJECT = new TokenStructureMatch("JSON_OBJECT");

		COMPOUND = new TokenGroupMatch().setName("COMPOUND");
		LIST = new TokenGroupMatch().setName("LIST");

		{
			PACKAGE.add(new TokenGroupMatch()
					.append(new TokenItemMatch(TokenType.KEYWORD,"package"))
					.append(new TokenGroupMatch().append(IDENTIFIER).setName("PACKAGE_PATH"))
					.append(new TokenItemMatch(TokenType.END_OF_STATEMENT)));
		}

		{
			IDENTIFIER.add(new TokenListMatch(TokenType.IDENTIFIER,TokenType.DOT));
		}

        {
			TokenGroupMatch g = new TokenGroupMatch();

			g.append(PACKAGE);
			g.append(new TokenListMatch(IMPORT,true).setName("IMPORT_LIST"));
			g.append(new TokenListMatch(UNIT).setName("UNIT_LIST"));
			g.append(new TokenItemMatch(TokenType.END_OF_FILE));

			FILE.add(g);
        }
        {
            TokenGroupMatch g = new TokenGroupMatch();

            g.append(UNIT_DECLARATION);
            g.append(UNIT_BODY);

            UNIT.add(g);
        }

        {
            UNIT_COMPONENT.add(FIELD);
            UNIT_COMPONENT.add(METHOD);
        }

        {
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenGroupMatch(true).append(ANNOTATION));
			g.append(new TokenListMatch(TokenType.MODIFIER,true));
			g.append(DATA_TYPE);
			{
				TokenGroupMatch g2 = new TokenGroupMatch();

				g2.append(new TokenItemMatch(TokenType.IDENTIFIER));
				g2.append(new TokenItemMatch(TokenType.OPERATOR,"="));
				g2.append(VALUE);

				g.append(new TokenListMatch(g2,new TokenItemMatch(TokenType.COMMA),true));
			}
			g.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));

            VARIABLE_DECLARATION.add(g);
        }

        {
            TokenGroupMatch g2 = new TokenGroupMatch().setName("INNER");
            g2.append(new TokenGroupMatch(true).append(ANNOTATION));
            g2.append(new TokenListMatch(TokenType.MODIFIER,true).setName("MODIFIER_LIST"));
            g2.append(DATA_TYPE);
            {
                TokenGroupMatch g3 = new TokenGroupMatch().setName("FIELD_DECLARATION");

                g3.append(new TokenItemMatch(TokenType.IDENTIFIER).setName("FIELD_NAME"));

                TokenGroupMatch g4 = new TokenGroupMatch(true).setName("FIELD_INITIALIZATION");
                g4.append(new TokenItemMatch(TokenType.OPERATOR,"="));
                g4.append(VALUE);
                g3.append(g4);

                g2.append(new TokenListMatch(g3,new TokenItemMatch(TokenType.COMMA).setName("FIELD_DECLARATION_SEPARATOR"),true).setName("FIELD_DECLARATION_LIST"));
            }

            TokenGroupMatch g = new TokenGroupMatch();
            g.append(g2);
            g.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));

            FIELD.add(g);
            EXPRESSION.add(g2);
        }

		{
			//Method
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenGroupMatch(true).append(ANNOTATION));
			g.append(new TokenListMatch(TokenType.MODIFIER,true));
			g.append(DATA_TYPE);
			g.append(new TokenItemMatch(TokenType.IDENTIFIER));

			g.append(new TokenItemMatch(TokenType.BRACE,"("));

			{
				TokenGroupMatch g2 = new TokenGroupMatch();

				g2.append(DATA_TYPE);
				g2.append(new TokenItemMatch(TokenType.IDENTIFIER));
				g.append(new TokenListMatch(g2,new TokenItemMatch(TokenType.COMMA),true));
			}

			g.append(new TokenItemMatch(TokenType.BRACE,")"));

			{
				TokenGroupMatch g2 = new TokenGroupMatch(true).setName("THREAD_MARKER");

				g2.append(new TokenItemMatch(TokenType.COLON));
				g2.append(VALUE);
				g.append(g2);
			}

			g.append(CODE_BLOCK);

			METHOD.add(g);
		}

        {
        	//Constructor
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenGroupMatch(true).append(ANNOTATION));
            g.append(new TokenListMatch(TokenType.MODIFIER,true));
            g.append(new TokenItemMatch(TokenType.IDENTIFIER));

            g.append(new TokenItemMatch(TokenType.BRACE,"("));

            {
                TokenGroupMatch g2 = new TokenGroupMatch();

                g2.append(DATA_TYPE);
                g2.append(new TokenItemMatch(TokenType.IDENTIFIER));
                g.append(new TokenListMatch(g2,new TokenItemMatch(TokenType.COMMA),true));
            }

            g.append(new TokenItemMatch(TokenType.BRACE,")"));

            g.append(CONSTRUCTOR_BODY);

            METHOD.add(g);
        }

		{
			//Operator Overload
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenGroupMatch(true).append(ANNOTATION));
			g.append(new TokenItemMatch(TokenType.KEYWORD,"operator"));
			g.append(DATA_TYPE);
			{
				TokenStructureMatch s = new TokenStructureMatch("OPERATOR_REFERENCE");

				s.add(new TokenItemMatch(TokenType.OPERATOR));
				s.add(new TokenItemMatch(TokenType.IDENTIFIER, "compare"));
				g.append(s);
			}

			g.append(new TokenItemMatch(TokenType.BRACE,"("));

			{
				TokenGroupMatch g2 = new TokenGroupMatch(true);

				g2.append(DATA_TYPE);
				g2.append(new TokenItemMatch(TokenType.IDENTIFIER));
				g.append(g2);
			}

			g.append(new TokenItemMatch(TokenType.BRACE,")"));

			g.append(CODE_BLOCK);

			METHOD.add(g);
		}

		{
			//Event
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenGroupMatch(true).append(ANNOTATION));
			g.append(new TokenItemMatch(TokenType.KEYWORD, "event"));
			g.append(new TokenItemMatch(TokenType.IDENTIFIER));

			g.append(new TokenItemMatch(TokenType.BRACE,"("));

			{
				TokenGroupMatch g2 = new TokenGroupMatch();

				g2.append(DATA_TYPE);
				g2.append(new TokenItemMatch(TokenType.IDENTIFIER));
				g.append(new TokenListMatch(g2,new TokenItemMatch(TokenType.COMMA),true));
			}

			g.append(new TokenItemMatch(TokenType.BRACE,")"));

			{
				TokenGroupMatch g2 = new TokenGroupMatch(true).setName("THREAD_MARKER");

				g2.append(new TokenItemMatch(TokenType.COLON));
				g2.append(VALUE);
				g.append(g2);
			}

			g.append(CODE_BLOCK);

			METHOD.add(g);
		}

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenItemMatch(TokenType.BRACE,"{"));

            g.append(new TokenListMatch(UNIT_COMPONENT,true).setName("UNIT_COMPONENT_LIST"));

            g.append(new TokenItemMatch(TokenType.BRACE,"}"));

            UNIT_BODY.add(g);
        }

		{
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenItemMatch(TokenType.BRACE,"{"));

			g.append(new TokenItemMatch(TokenType.BRACE,"}"));

			UNIT_BODY.add(g);
		}

		{
			TokenGroupMatch g = new TokenGroupMatch();

			g.append(new TokenItemMatch(TokenType.KEYWORD,"import"));
			g.append(IDENTIFIER);
			g.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));

			IMPORT.add(g);
		}

		{
			DATA_TYPE.add(new TokenItemMatch(TokenType.UNIT_TYPE));
			DATA_TYPE.add(new TokenItemMatch(TokenType.DATA_TYPE));
			DATA_TYPE.add(new TokenItemMatch(TokenType.IDENTIFIER));
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(DATA_TYPE);
				TokenGroupMatch g2 = new TokenGroupMatch();
				g2.append(new TokenItemMatch(TokenType.BRACE,"["));
				g2.append(new TokenItemMatch(TokenType.BRACE,"]"));
				g.append(new TokenListMatch(g2,true));

				DATA_TYPE.add(g);
			}
		}
		
		{
			// <ANNOTATION> <IDENTIFIER> <BRACE: (> [-VALUE-...] <BRACE: )>
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenItemMatch(TokenType.ANNOTATION_MARKER));
			g.append(METHOD_CALL);
			ANNOTATION.add(g);
		}

		{
			// <-ANNOTATION-> <MODIFIER> [UNIT_TYPE:entity] [IDENTIFIER] <[UNIT_ACTION:base] [IDENTIFIER]>
			TokenGroupMatch g = new TokenGroupMatch().setName("UNIT_DECLARATION");
			g.append(new TokenGroupMatch(true).append(ANNOTATION));
			g.append(new TokenListMatch(new TokenItemMatch(TokenType.MODIFIER).setName("UNIT_MODIFIER"),true).setName("UNIT_MODIFIERS"));
			g.append(new TokenItemMatch(TokenType.UNIT_TYPE).setName("UNIT_TYPE"));
			g.append(new TokenItemMatch(TokenType.IDENTIFIER).setName("UNIT_NAME"));
			
			{
				TokenGroupMatch g2 = new TokenGroupMatch().setName("UNIT_ACTION");
				g2.append(new TokenItemMatch(TokenType.UNIT_ACTION).setName("UNIT_ACTION_TYPE"));
				g2.append(new TokenListMatch(new TokenGroupMatch().append(IDENTIFIER).setName("UNIT_ACTION_REFERENCE"),new TokenItemMatch(TokenType.COMMA),true));
				g.append(new TokenListMatch(g2,true));
			}

			UNIT_DECLARATION.add(g);
		}
		
		{
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenItemMatch(TokenType.IDENTIFIER));
			g.append(new TokenItemMatch(TokenType.BRACE,"("));
			{
				TokenGroupMatch g2 = new TokenGroupMatch().setName("PARAMETER");
				{
					TokenGroupMatch g3 = new TokenGroupMatch(true).setName("PARAMETER_LABEL");
					g3.append(new TokenItemMatch(TokenType.IDENTIFIER));
					g3.append(new TokenItemMatch(TokenType.COLON));
					g2.append(g3);
				}
				g2.append(VALUE);
				g.append(new TokenListMatch(g2, new TokenItemMatch(TokenType.COMMA),true));
			}
			g.append(new TokenItemMatch(TokenType.BRACE,")"));

			METHOD_CALL.add(g);

		}

		{

			// [VALUE OPERATOR...]
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenListMatch(VALUE, new TokenItemMatch(TokenType.OPERATOR)));
				EXPRESSION.add(g);
			}

			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.BRACE,"("));
				g.append(EXPRESSION);
				g.append(new TokenItemMatch(TokenType.BRACE,")"));
				EXPRESSION.add(g);
			}
			
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.IDENTIFIER));
				g.append(new TokenItemMatch(TokenType.IDENTIFIER_OPERATOR));
				EXPRESSION.add(g);
			}
			
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.IDENTIFIER_OPERATOR));
				g.append(new TokenItemMatch(TokenType.IDENTIFIER));
				EXPRESSION.add(g);
			}
		}
		

		{
			// [IDENTIFIER]
			VALUE.add(new TokenItemMatch(TokenType.IDENTIFIER).setName("IDENTIFIER"));
			// [NUMBER]
			VALUE.add(new TokenItemMatch(TokenType.NUMBER).setName("NUMBER"));
			// [BOOLEAN]
			VALUE.add(new TokenItemMatch(TokenType.BOOLEAN).setName("BOOLEAN"));
			// [STRING_LITERAL]
			VALUE.add(new TokenItemMatch(TokenType.STRING_LITERAL).setName("STRING"));
			// [NULL]
			VALUE.add(new TokenItemMatch(TokenType.NULL).setName("NULL"));
			{
				// [NEGATION_OPERATOR][-VALUE-]
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.LOGICAL_NEGATION_OPERATOR));
				g.append(VALUE);
				VALUE.add(g);
			}
			{
				// [NEGATION_OPERATOR][-VALUE-]
				TokenGroupMatch g = new TokenGroupMatch();
				{
					TokenStructureMatch s = new TokenStructureMatch("SIGN");
					s.add(new TokenItemMatch(TokenType.OPERATOR,"+"));
					s.add(new TokenItemMatch(TokenType.OPERATOR,"-"));
					g.append(s);
				}
				g.append(VALUE);
				VALUE.add(g);
			}
			// [-EXPRESSION-]
			VALUE.add(EXPRESSION);
			VALUE.add(POINTER);
			VALUE.add(new TokenGroupMatch().append(new TokenItemMatch(TokenType.KEYWORD,"new")).append(METHOD_CALL));
			VALUE.add(DATA_TYPE);

			VALUE.add(LAMBDA);

		}

		{
			{
				TokenGroupMatch g = new TokenGroupMatch();
				{
					TokenStructureMatch s = new TokenStructureMatch("POINTER_SEGMENT");
					{
						TokenGroupMatch g2 = new TokenGroupMatch();
						g2.append(new TokenItemMatch(TokenType.IDENTIFIER));
						g2.append(new TokenItemMatch(TokenType.BLOCKSTATE,true));
						s.add(g2);
					}
					s.add(METHOD_CALL);
					g.append(s);
				}
				{
					TokenGroupMatch g2 = new TokenGroupMatch(true);
					g2.append(new TokenItemMatch(TokenType.DOT));
					g2.append(NESTED_POINTER);
					g.append(g2);
				}
				NESTED_POINTER.add(g);
			}
		}

		{
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(VALUE);
			g.append(new TokenItemMatch(TokenType.DOT));
			g.append(NESTED_POINTER);
			POINTER.add(g);
		}



		{
			TokenGroupMatch g = new TokenGroupMatch();
			{
				TokenStructureMatch h = new TokenStructureMatch("LAMBDA_HEADER");
				h.add(new TokenListMatch(TokenType.IDENTIFIER,TokenType.COMMA));
				h.add(new TokenGroupMatch()
						.append(new TokenItemMatch(TokenType.BRACE, "("))
						.append(new TokenListMatch(TokenType.IDENTIFIER,TokenType.COMMA,true))
						.append(new TokenItemMatch(TokenType.BRACE, ")"))
				);
				g.append(h);
			}
			g.append(new TokenItemMatch(TokenType.LAMBDA_ARROW));
			{
				TokenStructureMatch b = new TokenStructureMatch("LAMBDA_BLOCK");
				{
					TokenGroupMatch g2 = new TokenGroupMatch();
					g2.append(new TokenItemMatch(TokenType.BRACE,"{"));
					g2.append(new TokenListMatch(STATEMENT,true));
					g2.append(new TokenItemMatch(TokenType.BRACE,"}"));

					b.add(g2);
				}/*
				{
					TokenGroupMatch g2 = new TokenGroupMatch();
					g.append(new TokenItemMatch(TokenType.BRACE,"{"));
					g.append(new TokenItemMatch(TokenType.BRACE,"}"));

					b.add(g);
				}*/
				{
					TokenGroupMatch g2 = new TokenGroupMatch();
					g2.append(EXPRESSION);

					b.add(g2);
				}
				g.append(b);
			}
			LAMBDA.add(g);
		}
		
		
		{
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(EXPRESSION);
				
				g.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));
				STATEMENT.add(g);
			}
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.ACTION_KEYWORD));
			
				g.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));
				STATEMENT.add(g);
			}
		}


		{
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.BRACE,"{"));
				g.append(new TokenListMatch(STATEMENT,true));
				g.append(new TokenItemMatch(TokenType.BRACE,"}"));

				CODE_BLOCK.add(g);
			}
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(STATEMENT);

				CODE_BLOCK.add(g);
			}

			STATEMENT.add(CODE_BLOCK);
		}
		{
			CONSTRUCTOR_KEYWORD.add(new TokenItemMatch(TokenType.IDENTIFIER, "stack"));
			CONSTRUCTOR_KEYWORD.add(new TokenItemMatch(TokenType.IDENTIFIER, "nbt"));
			CONSTRUCTOR_KEYWORD.add(new TokenItemMatch(TokenType.IDENTIFIER, "equipment"));
			CONSTRUCTOR_KEYWORD.add(new TokenItemMatch(TokenType.IDENTIFIER, "multipart"));
		}
		{
			TokenStructureMatch CONSTRUCTOR_STATEMENT = new TokenStructureMatch("CONSTRUCTOR_STATEMENT");
			CONSTRUCTOR_STATEMENT.add(STATEMENT);
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(CONSTRUCTOR_KEYWORD);
				g.append(JSON_OBJECT);
				CONSTRUCTOR_STATEMENT.add(g);
			}

			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(TokenType.BRACE,"{"));
				g.append(new TokenListMatch(CONSTRUCTOR_STATEMENT,true));
				g.append(new TokenItemMatch(TokenType.BRACE,"}"));

				CONSTRUCTOR_BODY.add(g);
			}
		}
		
		IF_STATEMENT = new TokenGroupMatch();
		TokenStructureMatch ELSE_STATEMENT = new TokenStructureMatch("ELSE_STATEMENT",true);
		
		{
			TokenGroupMatch g = new TokenGroupMatch(true);
			g.append(new TokenItemMatch(TokenType.KEYWORD,"else"));
			g.append(CODE_BLOCK);
			ELSE_STATEMENT.add(g);
		}
		{
			TokenGroupMatch g = new TokenGroupMatch(true);
			g.append(new TokenItemMatch(TokenType.KEYWORD,"else"));
			g.append(IF_STATEMENT);
			ELSE_STATEMENT.add(g);
		}
		
		
		{
			IF_STATEMENT.append(new TokenItemMatch(TokenType.KEYWORD,"if"));
			IF_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"("));
			IF_STATEMENT.append(EXPRESSION);
			IF_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,")"));
			IF_STATEMENT.append(CODE_BLOCK);
			IF_STATEMENT.append(ELSE_STATEMENT);
		}
		STATEMENT.add(IF_STATEMENT);

        {
            RETURN_EXPRESSION = new TokenGroupMatch();
            RETURN_EXPRESSION.append(new TokenItemMatch(TokenType.ACTION_KEYWORD,"return"));
            RETURN_EXPRESSION.append(VALUE);
            //RETURN_EXPRESSION.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));

            EXPRESSION.add(RETURN_EXPRESSION);
        }
		
		FOR_STATEMENT = new TokenGroupMatch();
		{
			//for
			FOR_STATEMENT.append(new TokenItemMatch(TokenType.KEYWORD,"for"));
			//(
			FOR_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"("));
			//int i = 0
			FOR_STATEMENT.append(EXPRESSION);
			//;
			FOR_STATEMENT.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));
			//i < str.length();
			FOR_STATEMENT.append(EXPRESSION);
			//;
			FOR_STATEMENT.append(new TokenItemMatch(TokenType.END_OF_STATEMENT));
			//i++
			FOR_STATEMENT.append(EXPRESSION);
			//)
			FOR_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,")"));
			//{...}
			FOR_STATEMENT.append(CODE_BLOCK);
			STATEMENT.add(FOR_STATEMENT);
		}
		
		WHILE_STATEMENT = new TokenGroupMatch();
		{
			WHILE_STATEMENT.append(new TokenItemMatch(TokenType.KEYWORD,"while"));
			WHILE_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"("));
			WHILE_STATEMENT.append(EXPRESSION);
			WHILE_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,")"));
			WHILE_STATEMENT.append(CODE_BLOCK);
		}
		STATEMENT.add(WHILE_STATEMENT);
		
		SWITCH_STATEMENT = new TokenGroupMatch();
		{
			SWITCH_STATEMENT.append(new TokenItemMatch(TokenType.KEYWORD,"switch"));
			SWITCH_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"("));
			SWITCH_STATEMENT.append(VALUE);
			SWITCH_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,")"));
			SWITCH_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"{"));
			{
				TokenStructureMatch s = new TokenStructureMatch("switch_case");
				
				{
					TokenGroupMatch g = new TokenGroupMatch();
					g.append(new TokenItemMatch(TokenType.KEYWORD,"case"));
					g.append(VALUE);
					g.append(new TokenItemMatch(TokenType.COLON));
					g.append(CODE_BLOCK);
					s.add(g);
				}
				{
					TokenGroupMatch g = new TokenGroupMatch();
					g.append(new TokenItemMatch(TokenType.KEYWORD,"default"));
					g.append(new TokenItemMatch(TokenType.COLON));
					g.append(CODE_BLOCK);
					s.add(g);
				}
				
				SWITCH_STATEMENT.append(new TokenListMatch(s));
			}
			SWITCH_STATEMENT.append(new TokenItemMatch(TokenType.BRACE,"}"));
		}
		STATEMENT.add(SWITCH_STATEMENT);


		//JSON Structures
		TokenStructureMatch JSON_VALUE = new TokenStructureMatch("JSON_VALUE");

		TokenGroupMatch COMPOUND_ENTRY = new TokenGroupMatch();
		{
			COMPOUND_ENTRY.append(new TokenItemMatch(TokenType.IDENTIFIER));
			COMPOUND_ENTRY.append(new TokenItemMatch(TokenType.COLON));
			COMPOUND_ENTRY.append(JSON_VALUE);
		}

		{
			COMPOUND.append(new TokenItemMatch(TokenType.BRACE,"{"));
			COMPOUND.append(new TokenListMatch(COMPOUND_ENTRY, new TokenItemMatch(TokenType.COMMA), true));
			COMPOUND.append(new TokenItemMatch(TokenType.BRACE,"}"));
			JSON_OBJECT.add(COMPOUND);
		}

		{
			LIST.append(new TokenItemMatch(TokenType.BRACE,"["));
			LIST.append(new TokenListMatch(JSON_VALUE, new TokenItemMatch(TokenType.COMMA), true));
			LIST.append(new TokenItemMatch(TokenType.BRACE,"]"));
			JSON_OBJECT.add(LIST);
		}

		JSON_VALUE.add(VALUE);
		JSON_VALUE.add(JSON_OBJECT);
	}
}
