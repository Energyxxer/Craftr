package com.energyxxer.craftrlang.compiler.parsing;

import com.energyxxer.craftrlang.CraftrLang;
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
    public static final TokenStructureMatch ENUM_UNIT_DECLARATION;
    public static final TokenStructureMatch UNIT_BODY;
    public static final TokenStructureMatch ENUM_UNIT_BODY;
    public static final TokenStructureMatch WORLD_UNIT_BODY;
    public static final TokenStructureMatch UNIT_COMPONENT;

    public static final TokenStructureMatch VARIABLE;
    public static final TokenStructureMatch METHOD;
	public static final TokenStructureMatch SETUP_STATEMENT;
    public static final TokenStructureMatch OVERRIDE;
	public static final TokenStructureMatch OVERRIDE_BLOCK;

    public static final TokenStructureMatch ENUM_ELEMENT;

    public static final TokenStructureMatch METHOD_BODY;

    public static final TokenStructureMatch FORMAL_PARAMETER;

	public static final TokenStructureMatch IDENTIFIER;

    public static final TokenStructureMatch DATA_TYPE;
	public static final TokenStructureMatch METHOD_CALL;
	public static final TokenStructureMatch VALUE;
	public static final TokenStructureMatch EXPRESSION;
	public static final TokenStructureMatch STATEMENT;
	public static final TokenStructureMatch CODE_BLOCK;
	public static final TokenStructureMatch DELIMITED_CODE_BLOCK;
	public static final TokenStructureMatch ANNOTATION;

	public static final TokenStructureMatch LAMBDA;

	public static final TokenGroupMatch IF_STATEMENT;
	public static final TokenGroupMatch FOR_STATEMENT;
	public static final TokenGroupMatch WHILE_STATEMENT;
	public static final TokenGroupMatch SWITCH_STATEMENT;
	public static final TokenGroupMatch RETURN_STATEMENT;

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
        ENUM_UNIT_DECLARATION = new TokenStructureMatch("UNIT_DECLARATION");
        UNIT_BODY = new TokenStructureMatch("UNIT_BODY");
        ENUM_UNIT_BODY = new TokenStructureMatch("UNIT_BODY");
        WORLD_UNIT_BODY = new TokenStructureMatch("UNIT_BODY");
        UNIT_COMPONENT = new TokenStructureMatch("UNIT_COMPONENT");

        VARIABLE = new TokenStructureMatch("VARIABLE");
        METHOD = new TokenStructureMatch("METHOD");
		SETUP_STATEMENT = new TokenStructureMatch("SETUP_STATEMENT");
		OVERRIDE = new TokenStructureMatch("OVERRIDE");
		OVERRIDE_BLOCK = new TokenStructureMatch("OVERRIDE_BLOCK");

        ENUM_ELEMENT = new TokenStructureMatch("ENUM_ELEMENT");

        METHOD_BODY = new TokenStructureMatch("METHOD_BODY");

        FORMAL_PARAMETER = new TokenStructureMatch("FORMAL_PARAMETER");

		IDENTIFIER = new TokenStructureMatch("IDENTIFIER");

		DATA_TYPE = new TokenStructureMatch("DATA_TYPE");
		EXPRESSION = new TokenStructureMatch("EXPRESSION");
		VALUE = new TokenStructureMatch("VALUE");
		METHOD_CALL = new TokenStructureMatch("METHOD_CALL");
		STATEMENT = new TokenStructureMatch("STATEMENT");
		CODE_BLOCK = new TokenStructureMatch("CODE_BLOCK");
		DELIMITED_CODE_BLOCK = new TokenStructureMatch("DELIMITED_CODE_BLOCK");
		ANNOTATION = new TokenStructureMatch("ANNOTATION");

		LAMBDA = new TokenStructureMatch("LAMBDA");

		NESTED_POINTER = new TokenStructureMatch("NESTED_POINTER");
		POINTER = new TokenStructureMatch("POINTER");
		JSON_OBJECT = new TokenStructureMatch("JSON_OBJECT");

		COMPOUND = new TokenGroupMatch().setName("COMPOUND");
		LIST = new TokenGroupMatch().setName("LIST");

		{

			PACKAGE.add(new TokenGroupMatch(true)
					.append(new TokenItemMatch(CraftrLang.KEYWORD,"package"))
					.append(new TokenGroupMatch().append(IDENTIFIER).setName("PACKAGE_PATH"))
					.append(new TokenItemMatch(CraftrLang.END_OF_STATEMENT)));
		}

		{
			IDENTIFIER.add(new TokenListMatch(CraftrLang.IDENTIFIER, CraftrLang.DOT));
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

            g.append(ENUM_UNIT_DECLARATION);
            g.append(ENUM_UNIT_BODY);

            UNIT.add(g);
        }
        {
            TokenGroupMatch g = new TokenGroupMatch();

            g.append(UNIT_DECLARATION);
            g.append(UNIT_BODY);

            UNIT.add(g);
        }

        {
            //Override extends
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenItemMatch(CraftrLang.UNIT_TYPE));
            g.append(IDENTIFIER);
			{
				TokenStructureMatch s = new TokenStructureMatch("OVERRIDE_ACTION");

				{
					TokenGroupMatch g2 = new TokenGroupMatch().setName("PURE_UNIT_MODIFICATION");
					g2.append(new TokenItemMatch(CraftrLang.UNIT_ACTION).setName("OVERRIDE_ACTION_TYPE"));
					g2.append(new TokenListMatch(new TokenGroupMatch().append(IDENTIFIER).setName("OVERRIDE_ACTION_REFERENCE"),new TokenItemMatch(CraftrLang.COMMA),true)).setName("OVERRIDE_ACTION_REFERENCE_LIST");
					s.add(g2);
				}
				{
					TokenGroupMatch g2 = new TokenGroupMatch().setName("PURE_UNIT_REPLACEMENT");
					g2.append(new TokenItemMatch(CraftrLang.OPERATOR,"="));
					g2.append(IDENTIFIER);
					s.add(g2);
				}

				g.append(s);
			}
			g.append(new TokenItemMatch(CraftrLang.END_OF_STATEMENT));
            OVERRIDE.add(g);
        }

		{
			//Override Block
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenItemMatch(CraftrLang.KEYWORD,"overrides"));
			g.append(new TokenItemMatch(CraftrLang.BRACE, "{"));
			g.append(new TokenListMatch(OVERRIDE));
			g.append(new TokenItemMatch(CraftrLang.BRACE, "}"));
			OVERRIDE_BLOCK.add(g);
		}

        {
            UNIT_COMPONENT.add(VARIABLE);
            UNIT_COMPONENT.add(METHOD);
			UNIT_COMPONENT.add(new TokenGroupMatch().setName("ANONYMOUS_BLOCK")
							.append(new TokenItemMatch(CraftrLang.MODIFIER,"static", true))
							.append(new TokenItemMatch(CraftrLang.BRACE,"{"))
							.append(new TokenListMatch(STATEMENT, true))
							.append(new TokenItemMatch(CraftrLang.BRACE,"}"))
			);
			UNIT_COMPONENT.add(OVERRIDE_BLOCK);
        }

        {
            TokenGroupMatch g2 = new TokenGroupMatch().setName("VARIABLE_INNER");
            g2.append(new TokenGroupMatch(true).append(ANNOTATION));
            g2.append(new TokenListMatch(CraftrLang.MODIFIER,true).setName("MODIFIER_LIST"));
            g2.append(DATA_TYPE);
            {
                TokenGroupMatch g3 = new TokenGroupMatch().setName("VARIABLE_DECLARATION");

                g3.append(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("VARIABLE_NAME"));

                TokenGroupMatch g4 = new TokenGroupMatch(true).setName("VARIABLE_INITIALIZATION");
                g4.append(new TokenItemMatch(CraftrLang.OPERATOR,"="));
                g4.append(VALUE);
                g3.append(g4);

                g2.append(new TokenListMatch(g3,new TokenItemMatch(CraftrLang.COMMA).setName("VARIABLE_DECLARATION_SEPARATOR"),true).setName("VARIABLE_DECLARATION_LIST"));
            }

            TokenGroupMatch g = new TokenGroupMatch();
            g.append(g2);
            g.append(new TokenItemMatch(CraftrLang.END_OF_STATEMENT));

            VARIABLE.add(g);
            STATEMENT.add(VARIABLE);
        }

        {
            ENUM_ELEMENT.add(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("ENUM_ELEMENT_NAME"));
            ENUM_ELEMENT.add(METHOD_CALL);
        }

		//--------------------
		//  START OF METHODS
		//--------------------

        {
            //Method Body
            METHOD_BODY.add(DELIMITED_CODE_BLOCK);
            METHOD_BODY.add(new TokenItemMatch(CraftrLang.END_OF_STATEMENT).setName("OMITTED_BODY"));
        }

        {
            //Formal Parameter
            TokenGroupMatch g = new TokenGroupMatch().setName("FORMAL_PARAMETER");
            g.append(DATA_TYPE);
            g.append(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("PARAMETER_NAME"));

            {
                TokenGroupMatch g2 = new TokenGroupMatch(true).setName("PARAMETER_INITIALIZER");
                g2.append(new TokenItemMatch(CraftrLang.OPERATOR,"="));
                g2.append(VALUE);

                g.append(g2);
            }

            FORMAL_PARAMETER.add(g);
        }

		{
			//Method
			TokenGroupMatch g = new TokenGroupMatch().setName("METHOD_METHOD");
			g.append(new TokenGroupMatch(true).append(ANNOTATION));
			g.append(new TokenListMatch(CraftrLang.MODIFIER,true).setName("MODIFIER_LIST"));
			g.append(new TokenGroupMatch().append(DATA_TYPE).setName("RETURN_TYPE"));
			g.append(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("METHOD_NAME").addTags("method_name"));

            g.append(new TokenItemMatch(CraftrLang.BRACE,"("));
            g.append(new TokenListMatch(FORMAL_PARAMETER,new TokenItemMatch(CraftrLang.COMMA),true).setName("PARAMETER_LIST"));
			g.append(new TokenItemMatch(CraftrLang.BRACE,")"));

			{
				TokenGroupMatch g2 = new TokenGroupMatch(true).setName("THREAD_MARKER");

				g2.append(new TokenItemMatch(CraftrLang.COLON));
				g2.append(IDENTIFIER);
				g.append(g2);
			}

			g.append(METHOD_BODY);

			METHOD.add(g);
		}

        {
        	//Constructor
            TokenGroupMatch g = new TokenGroupMatch().setName("METHOD_CONSTRUCTOR");
            g.append(new TokenGroupMatch(true).append(ANNOTATION));
            g.append(new TokenListMatch(CraftrLang.MODIFIER,true).setName("MODIFIER_LIST"));
            g.append(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("METHOD_NAME").addTags("method_name"));

            g.append(new TokenItemMatch(CraftrLang.BRACE,"("));
            g.append(new TokenListMatch(FORMAL_PARAMETER,new TokenItemMatch(CraftrLang.COMMA),true).setName("PARAMETER_LIST"));
            g.append(new TokenItemMatch(CraftrLang.BRACE,")"));

			{
				TokenGroupMatch g2 = new TokenGroupMatch(true).setName("THREAD_MARKER");

				g2.append(new TokenItemMatch(CraftrLang.COLON));
				g2.append(IDENTIFIER);
				g.append(g2);
			}

            g.append(METHOD_BODY);

            METHOD.add(g);
        }

		{
			//Operator Overload
			TokenGroupMatch g = new TokenGroupMatch().setName("METHOD_OPERATOR");
			g.append(new TokenGroupMatch(true).append(ANNOTATION));
			g.append(new TokenItemMatch(CraftrLang.KEYWORD,"operator"));
			g.append(new TokenGroupMatch().append(DATA_TYPE).setName("RETURN_TYPE"));
			{
				TokenStructureMatch s = new TokenStructureMatch("OPERATOR_REFERENCE");

				s.add(new TokenItemMatch(CraftrLang.OPERATOR).setName("METHOD_NAME"));
				s.add(new TokenItemMatch(CraftrLang.IDENTIFIER, "compare").setName("METHOD_NAME"));
				g.append(s);
			}

			g.append(new TokenItemMatch(CraftrLang.BRACE,"("));
            g.append(new TokenListMatch(FORMAL_PARAMETER,new TokenItemMatch(CraftrLang.COMMA),true).setName("PARAMETER_LIST"));
			g.append(new TokenItemMatch(CraftrLang.BRACE,")"));

			{
				TokenGroupMatch g2 = new TokenGroupMatch(true).setName("THREAD_MARKER");

				g2.append(new TokenItemMatch(CraftrLang.COLON));
				g2.append(IDENTIFIER);
				g.append(g2);
			}

			g.append(METHOD_BODY);

			METHOD.add(g);
		}

		{
			//Event
			TokenGroupMatch g = new TokenGroupMatch().setName("METHOD_EVENT");
			g.append(new TokenGroupMatch(true).append(ANNOTATION));
			g.append(new TokenItemMatch(CraftrLang.KEYWORD, "event"));
			g.append(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("METHOD_NAME").addTags("method_name"));

			g.append(new TokenItemMatch(CraftrLang.BRACE,"("));
			g.append(new TokenListMatch(FORMAL_PARAMETER,new TokenItemMatch(CraftrLang.COMMA),true).setName("PARAMETER_LIST"));
			g.append(new TokenItemMatch(CraftrLang.BRACE,")"));

			{
				TokenGroupMatch g2 = new TokenGroupMatch(true).setName("THREAD_MARKER");

				g2.append(new TokenItemMatch(CraftrLang.COLON));
				g2.append(IDENTIFIER);
				g.append(g2);
			}

            g.append(METHOD_BODY);

			METHOD.add(g);
		}

		//--------------------
		//   END OF METHODS
		//--------------------

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenItemMatch(CraftrLang.BRACE,"{"));

            g.append(new TokenListMatch(UNIT_COMPONENT,true).setName("UNIT_COMPONENT_LIST"));

            g.append(new TokenItemMatch(CraftrLang.BRACE,"}"));

            UNIT_BODY.add(g);
        }

        {
            TokenGroupMatch g = new TokenGroupMatch();
            g.append(new TokenItemMatch(CraftrLang.BRACE,"{"));

            g.append(new TokenListMatch(ENUM_ELEMENT, new TokenItemMatch(CraftrLang.COMMA),true).setName("ENUM_ELEMENT_LIST"));

            {
                TokenGroupMatch g2 = new TokenGroupMatch(true).setName("UNIT_COMPONENT_LIST_WRAPPER");
                g2.append(new TokenItemMatch(CraftrLang.END_OF_STATEMENT));
                g2.append(new TokenListMatch(UNIT_COMPONENT,true).setName("UNIT_COMPONENT_LIST"));

                g.append(g2);
            }

            g.append(new TokenItemMatch(CraftrLang.BRACE,"}"));

            ENUM_UNIT_BODY.add(g);
        }

		{
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenItemMatch(CraftrLang.BRACE,"{"));

			g.append(new TokenItemMatch(CraftrLang.BRACE,"}"));

			UNIT_BODY.add(g);
		}

		{
            TokenStructureMatch s = new TokenStructureMatch("IMPORT_POINTER");

            TokenGroupMatch identifierSegment = new TokenGroupMatch().setName("IMPORT_IDENTIFIER").append(new TokenItemMatch(CraftrLang.IDENTIFIER)).append(new TokenGroupMatch(true).append(new TokenItemMatch(CraftrLang.DOT)).append(s));

            s.add(identifierSegment);
            s.add(new TokenItemMatch(CraftrLang.OPERATOR,"*"));


			TokenGroupMatch g = new TokenGroupMatch();

			g.append(new TokenItemMatch(CraftrLang.KEYWORD,"import"));
            g.append(identifierSegment);
			g.append(new TokenItemMatch(CraftrLang.END_OF_STATEMENT));

			IMPORT.add(g);
		}

		{
			DATA_TYPE.add(new TokenItemMatch(CraftrLang.DATA_TYPE));
			DATA_TYPE.add(new TokenItemMatch(CraftrLang.IDENTIFIER));
			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(DATA_TYPE);
				g.append(new TokenItemMatch(CraftrLang.BRACE,"["));
				g.append(new TokenItemMatch(CraftrLang.BRACE,"]"));
				DATA_TYPE.add(g);
			}
		}
		
		{
			// <ANNOTATION> <IDENTIFIER> <BRACE: (> [-VALUE-...] <BRACE: )>
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(new TokenItemMatch(CraftrLang.ANNOTATION_MARKER));
			g.append(METHOD_CALL);
			ANNOTATION.add(g);
		}

		{
			// <-ANNOTATION-> <MODIFIER> [UNIT_TYPE:entity] [IDENTIFIER] <[UNIT_ACTION:base] [IDENTIFIER]>
			TokenGroupMatch g = new TokenGroupMatch().setName("UNIT_DECLARATION");
			g.append(new TokenGroupMatch(true).append(ANNOTATION));
			g.append(new TokenListMatch(new TokenItemMatch(CraftrLang.MODIFIER).setName("UNIT_MODIFIER"),true).setName("UNIT_MODIFIERS"));
			g.append(new TokenItemMatch(CraftrLang.UNIT_TYPE).setName("UNIT_TYPE"));
			g.append(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("UNIT_NAME").addTags("UNIT_NAME"));
			
			{
				TokenGroupMatch g2 = new TokenGroupMatch().setName("UNIT_ACTION");
				g2.append(new TokenItemMatch(CraftrLang.UNIT_ACTION).setName("UNIT_ACTION_TYPE"));
				g2.append(new TokenListMatch(new TokenGroupMatch().append(IDENTIFIER).setName("UNIT_ACTION_REFERENCE"),new TokenItemMatch(CraftrLang.COMMA)));
				g.append(new TokenListMatch(g2,true));
			}

			UNIT_DECLARATION.add(g);
		}

        {
            // <-ANNOTATION-> <MODIFIER> [UNIT_TYPE:entity] [IDENTIFIER] <[UNIT_ACTION:base] [IDENTIFIER]>
            TokenGroupMatch g = new TokenGroupMatch().setName("UNIT_DECLARATION");
            g.append(new TokenGroupMatch(true).append(ANNOTATION));
            g.append(new TokenListMatch(new TokenItemMatch(CraftrLang.MODIFIER).setName("UNIT_MODIFIER"),true).setName("UNIT_MODIFIERS"));
            g.append(new TokenItemMatch(CraftrLang.UNIT_TYPE,"enum").setName("UNIT_TYPE"));
            g.append(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("UNIT_NAME"));

            {
                TokenGroupMatch g2 = new TokenGroupMatch().setName("UNIT_ACTION");
                g2.append(new TokenItemMatch(CraftrLang.UNIT_ACTION).setName("UNIT_ACTION_TYPE"));
                g2.append(new TokenListMatch(new TokenGroupMatch().append(IDENTIFIER).setName("UNIT_ACTION_REFERENCE"),new TokenItemMatch(CraftrLang.COMMA),true));
                g.append(new TokenListMatch(g2,true));
            }

            ENUM_UNIT_DECLARATION.add(g);
        }
		
		{
			TokenGroupMatch g = new TokenGroupMatch().setName("METHOD_CALL_INNER");
			g.append(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("METHOD_CALL_NAME"));
			g.append(new TokenItemMatch(CraftrLang.BRACE,"("));
			{
				TokenGroupMatch g2 = new TokenGroupMatch().setName("PARAMETER");
				{
					TokenGroupMatch g3 = new TokenGroupMatch(true).setName("PARAMETER_LABEL_WRAPPER");
					g3.append(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("PARAMETER_LABEL"));
					g3.append(new TokenItemMatch(CraftrLang.COLON));
					g2.append(g3);
				}
				g2.append(VALUE);
				g.append(new TokenListMatch(g2, new TokenItemMatch(CraftrLang.COMMA),true).setName("PARAMETER_LIST"));
			}
			g.append(new TokenItemMatch(CraftrLang.BRACE,")"));

			METHOD_CALL.add(g);

		}

		{

			// [VALUE OPERATOR...]
			{
				TokenGroupMatch g = new TokenGroupMatch().setName("OPERATION");
				g.append(new TokenListMatch(VALUE, new TokenItemMatch(CraftrLang.OPERATOR).setName("OPERATOR")).setName("OPERATION_LIST"));
				EXPRESSION.add(g);
			}

			{
				TokenGroupMatch g = new TokenGroupMatch().setName("PARENTHESIZED_EXPRESSION");
				g.append(new TokenItemMatch(CraftrLang.BRACE,"("));
				g.append(EXPRESSION);
				g.append(new TokenItemMatch(CraftrLang.BRACE,")"));
				EXPRESSION.add(g);
			}
			
			{
				TokenGroupMatch g = new TokenGroupMatch().setName("UNARY_OPERATION_R");
				g.append(new TokenItemMatch(CraftrLang.IDENTIFIER));
				g.append(new TokenItemMatch(CraftrLang.IDENTIFIER_OPERATOR));
				EXPRESSION.add(g);
			}
			
			{
				TokenGroupMatch g = new TokenGroupMatch().setName("UNARY_OPERATION_L");
				g.append(new TokenItemMatch(CraftrLang.IDENTIFIER_OPERATOR));
				g.append(new TokenItemMatch(CraftrLang.IDENTIFIER));
				EXPRESSION.add(g);
			}
		}
		

		{
			// [IDENTIFIER]
			VALUE.add(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("SINGLE_IDENTIFIER"));
			// [NUMBER]
			VALUE.add(new TokenItemMatch(CraftrLang.NUMBER).setName("NUMBER"));
			// [BOOLEAN]
			VALUE.add(new TokenItemMatch(CraftrLang.BOOLEAN).setName("BOOLEAN"));
			// [STRING_LITERAL]
			VALUE.add(new TokenItemMatch(CraftrLang.STRING_LITERAL).setName("STRING"));
			// [NULL]
			VALUE.add(new TokenItemMatch(CraftrLang.NULL).setName("NULL"));
			{
				// [NEGATION_OPERATOR][-VALUE-]
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(CraftrLang.LOGICAL_NEGATION_OPERATOR));
				g.append(VALUE);
				VALUE.add(g);
			}
			{
				// [NEGATION_OPERATOR][-VALUE-]
				TokenGroupMatch g = new TokenGroupMatch();
				{
					TokenStructureMatch s = new TokenStructureMatch("SIGN");
					s.add(new TokenItemMatch(CraftrLang.OPERATOR,"+"));
					s.add(new TokenItemMatch(CraftrLang.OPERATOR,"-"));
					g.append(s);
				}
				g.append(VALUE);
				VALUE.add(g);
			}
			// [-EXPRESSION-]
			VALUE.add(EXPRESSION);
			VALUE.add(POINTER);
			VALUE.add(new TokenGroupMatch().setName("INSTANTIATION").append(new TokenItemMatch(CraftrLang.KEYWORD,"new")).append(METHOD_CALL).append(new TokenGroupMatch(true).append(UNIT_BODY)));
            VALUE.add(METHOD_CALL);
			VALUE.add(LAMBDA);

		}

		{
			{
				TokenGroupMatch g = new TokenGroupMatch().setName("NESTED_POINTER_INNER");
				{
					TokenStructureMatch s = new TokenStructureMatch("POINTER_SEGMENT");
					s.add(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("SINGLE_IDENTIFIER"));
					{

						//TokenGroupMatch g2 = new TokenGroupMatch().setName("POINTER_IDENTIFIER");
						//g2.append(new TokenItemMatch(CraftrLang.IDENTIFIER));
						//TODO: BLOCKSTATES PLS: g2.append(new TokenItemMatch(CraftrLang.BLOCKSTATE,true));
						//s.add(g2);
					}
					s.add(METHOD_CALL);
					g.append(s);
				}
				{
					TokenGroupMatch g2 = new TokenGroupMatch(true).setName("POINTER_NEXT");
					g2.append(new TokenItemMatch(CraftrLang.DOT));
					g2.append(NESTED_POINTER);
					g.append(g2);
				}
				NESTED_POINTER.add(g);
			}
		}

		{
			TokenGroupMatch g = new TokenGroupMatch();
			g.append(VALUE);
			g.append(new TokenItemMatch(CraftrLang.DOT));
			g.append(NESTED_POINTER);
			POINTER.add(g);
		}



		{
			TokenGroupMatch g = new TokenGroupMatch();
			{
				TokenStructureMatch h = new TokenStructureMatch("LAMBDA_HEADER");
				h.add(new TokenListMatch(CraftrLang.IDENTIFIER, CraftrLang.COMMA));
				h.add(new TokenGroupMatch()
						.append(new TokenItemMatch(CraftrLang.BRACE, "("))
						.append(new TokenListMatch(CraftrLang.IDENTIFIER, CraftrLang.COMMA,true))
						.append(new TokenItemMatch(CraftrLang.BRACE, ")"))
				);
				g.append(h);
			}
			g.append(new TokenItemMatch(CraftrLang.LAMBDA_ARROW));
			{
				TokenStructureMatch b = new TokenStructureMatch("LAMBDA_BLOCK");
				b.add(DELIMITED_CODE_BLOCK);
				b.add(EXPRESSION);
				g.append(b);
			}
			LAMBDA.add(g);
		}
		
		
		{
			{
				TokenGroupMatch g = new TokenGroupMatch().setName("EMPTY_STATEMENT");
				g.append(new TokenItemMatch(CraftrLang.END_OF_STATEMENT));
				STATEMENT.add(g);
			}
			{
				TokenGroupMatch g = new TokenGroupMatch().setName("EXPRESSION_STATEMENT");
				g.append(EXPRESSION);
				
				g.append(new TokenItemMatch(CraftrLang.END_OF_STATEMENT));
				STATEMENT.add(g);
			}
			{
				TokenGroupMatch g = new TokenGroupMatch().setName("ACTION_STATEMENT");
				g.append(new TokenItemMatch(CraftrLang.ACTION_KEYWORD));
				g.append(new TokenItemMatch(CraftrLang.END_OF_STATEMENT));
				STATEMENT.add(g);
			}
		}


		{
			{
				TokenGroupMatch g = new TokenGroupMatch().setName("MULTI_STATEMENT");
				g.append(new TokenItemMatch(CraftrLang.BRACE,"{"));
				g.append(new TokenListMatch(STATEMENT,true).setName("STATEMENT_LIST"));
				g.append(new TokenItemMatch(CraftrLang.BRACE,"}"));

				CODE_BLOCK.add(g);
				DELIMITED_CODE_BLOCK.add(g);
			}
			{
				TokenGroupMatch g = new TokenGroupMatch().setName("SINGLE_STATEMENT");
				g.append(STATEMENT);

				CODE_BLOCK.add(g);
			}

			STATEMENT.add(DELIMITED_CODE_BLOCK);
		}

		{
			TokenStructureMatch SETUP_STATEMENT_KEYWORD = new TokenStructureMatch("SETUP_STATEMENT_KEYWORD");
			SETUP_STATEMENT_KEYWORD.add(new TokenItemMatch(CraftrLang.IDENTIFIER, "nbt"));
			SETUP_STATEMENT_KEYWORD.add(new TokenItemMatch(CraftrLang.IDENTIFIER, "equipment"));
			SETUP_STATEMENT_KEYWORD.add(new TokenItemMatch(CraftrLang.IDENTIFIER, "stack"));
			SETUP_STATEMENT_KEYWORD.add(new TokenItemMatch(CraftrLang.IDENTIFIER, "passengers"));
			SETUP_STATEMENT_KEYWORD.add(new TokenItemMatch(CraftrLang.IDENTIFIER, "multipart"));

			{
				TokenGroupMatch g = new TokenGroupMatch();
				g.append(new TokenItemMatch(CraftrLang.KEYWORD, "setup"));
				g.append(SETUP_STATEMENT_KEYWORD);
				g.append(JSON_OBJECT);
				g.append(new TokenItemMatch(CraftrLang.END_OF_STATEMENT));
				SETUP_STATEMENT.add(g);
			}

			STATEMENT.add(SETUP_STATEMENT);
		}
		
		IF_STATEMENT = new TokenGroupMatch();
		TokenStructureMatch ELSE_STATEMENT = new TokenStructureMatch("ELSE_STATEMENT",true);
		
		{
			TokenGroupMatch g = new TokenGroupMatch(true);
			g.append(new TokenItemMatch(CraftrLang.KEYWORD,"else"));
			g.append(CODE_BLOCK);
			ELSE_STATEMENT.add(g);
		}
		{
			TokenGroupMatch g = new TokenGroupMatch(true);
			g.append(new TokenItemMatch(CraftrLang.KEYWORD,"else"));
			g.append(IF_STATEMENT);
			ELSE_STATEMENT.add(g);
		}
		
		
		{
			IF_STATEMENT.append(new TokenItemMatch(CraftrLang.KEYWORD,"if"));
			IF_STATEMENT.append(new TokenItemMatch(CraftrLang.BRACE,"("));
			IF_STATEMENT.append(EXPRESSION);
			IF_STATEMENT.append(new TokenItemMatch(CraftrLang.BRACE,")"));
			IF_STATEMENT.append(CODE_BLOCK);
			IF_STATEMENT.append(ELSE_STATEMENT);
		}
		STATEMENT.add(IF_STATEMENT);

        {
            RETURN_STATEMENT = new TokenGroupMatch().setName("RETURN_STATEMENT");
            RETURN_STATEMENT.append(new TokenItemMatch(CraftrLang.ACTION_KEYWORD,"return"));
            RETURN_STATEMENT.append(VALUE);
            RETURN_STATEMENT.append(new TokenItemMatch(CraftrLang.END_OF_STATEMENT));

            STATEMENT.add(RETURN_STATEMENT);
        }
		
		FOR_STATEMENT = new TokenGroupMatch();
		{
			//for
			FOR_STATEMENT.append(new TokenItemMatch(CraftrLang.KEYWORD,"for"));
			//(
			FOR_STATEMENT.append(new TokenItemMatch(CraftrLang.BRACE,"("));
			//int i = 0;
			FOR_STATEMENT.append(STATEMENT);
			//i < str.length()
			FOR_STATEMENT.append(EXPRESSION);
			//;
			FOR_STATEMENT.append(new TokenItemMatch(CraftrLang.END_OF_STATEMENT));
			//i++
			FOR_STATEMENT.append(EXPRESSION);
			//)
			FOR_STATEMENT.append(new TokenItemMatch(CraftrLang.BRACE,")"));
			//{...}
			FOR_STATEMENT.append(CODE_BLOCK);
			STATEMENT.add(FOR_STATEMENT);
		}
		
		WHILE_STATEMENT = new TokenGroupMatch();
		{
			WHILE_STATEMENT.append(new TokenItemMatch(CraftrLang.KEYWORD,"while"));
			WHILE_STATEMENT.append(new TokenItemMatch(CraftrLang.BRACE,"("));
			WHILE_STATEMENT.append(EXPRESSION);
			WHILE_STATEMENT.append(new TokenItemMatch(CraftrLang.BRACE,")"));
			WHILE_STATEMENT.append(CODE_BLOCK);
		}
		STATEMENT.add(WHILE_STATEMENT);
		
		SWITCH_STATEMENT = new TokenGroupMatch();
		{
			SWITCH_STATEMENT.append(new TokenItemMatch(CraftrLang.KEYWORD,"switch"));
			SWITCH_STATEMENT.append(new TokenItemMatch(CraftrLang.BRACE,"("));
			SWITCH_STATEMENT.append(VALUE);
			SWITCH_STATEMENT.append(new TokenItemMatch(CraftrLang.BRACE,")"));
			SWITCH_STATEMENT.append(new TokenItemMatch(CraftrLang.BRACE,"{"));
			{
				TokenStructureMatch s = new TokenStructureMatch("SWITCH_CASE");
				
				{
					TokenGroupMatch g = new TokenGroupMatch();
					g.append(new TokenItemMatch(CraftrLang.KEYWORD,"case"));
					g.append(VALUE);
					g.append(new TokenItemMatch(CraftrLang.COLON));
					g.append(CODE_BLOCK);
					s.add(g);
				}
				{
					TokenGroupMatch g = new TokenGroupMatch();
					g.append(new TokenItemMatch(CraftrLang.KEYWORD,"default"));
					g.append(new TokenItemMatch(CraftrLang.COLON));
					g.append(CODE_BLOCK);
					s.add(g);
				}
				
				SWITCH_STATEMENT.append(new TokenListMatch(s));
			}
			SWITCH_STATEMENT.append(new TokenItemMatch(CraftrLang.BRACE,"}"));

		}
		STATEMENT.add(SWITCH_STATEMENT);


		//JSON Structures
		TokenStructureMatch JSON_VALUE = new TokenStructureMatch("JSON_VALUE");

		TokenGroupMatch COMPOUND_ENTRY = new TokenGroupMatch();
		{
			COMPOUND_ENTRY.append(new TokenItemMatch(CraftrLang.IDENTIFIER).setName("JSON_COMPOUND_NAME"));
			COMPOUND_ENTRY.append(new TokenItemMatch(CraftrLang.COLON));
			COMPOUND_ENTRY.append(JSON_VALUE);
		}

		{
			COMPOUND.append(new TokenItemMatch(CraftrLang.BRACE,"{"));
			COMPOUND.append(new TokenListMatch(COMPOUND_ENTRY, new TokenItemMatch(CraftrLang.COMMA), true));
			COMPOUND.append(new TokenItemMatch(CraftrLang.BRACE,"}"));
			JSON_OBJECT.add(COMPOUND);
		}

		{
			LIST.append(new TokenItemMatch(CraftrLang.BRACE,"["));
			LIST.append(new TokenListMatch(JSON_VALUE, new TokenItemMatch(CraftrLang.COMMA), true));
			LIST.append(new TokenItemMatch(CraftrLang.BRACE,"]"));
			JSON_OBJECT.add(LIST);
		}

		JSON_VALUE.add(VALUE);
		JSON_VALUE.add(JSON_OBJECT);
	}
}
