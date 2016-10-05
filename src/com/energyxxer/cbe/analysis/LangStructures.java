package com.energyxxer.cbe.analysis;

import com.energyxxer.cbe.analysis.token.TokenType;
import com.energyxxer.cbe.analysis.token.structures.TokenGroup;
import com.energyxxer.cbe.analysis.token.structures.TokenItem;
import com.energyxxer.cbe.analysis.token.structures.TokenList;
import com.energyxxer.cbe.analysis.token.structures.TokenStructure;

public class LangStructures {
	public final static TokenStructure UNIT_DECLARATION;
	public final static TokenStructure ARRAY_DECLARATION;
	
	static {
		UNIT_DECLARATION = new TokenStructure();
		
		{
			//<QUALIFIER> [UNIT_TYPE:entity] [IDENTIFIER] <[UNIT_ACTION:base] [IDENTIFIER]>
			TokenGroup g = new TokenGroup();
			g.append(new TokenList(TokenType.QUALIFIER,true));
			g.append(new TokenItem(TokenType.UNIT_TYPE,"entity"));
			g.append(new TokenItem(TokenType.IDENTIFIER));
			
			TokenGroup g2 = new TokenGroup(true);
			g2.append(new TokenItem(TokenType.UNIT_ACTION,"base"));
			g2.append(new TokenItem(TokenType.IDENTIFIER));
			g.append(g2);
			
			UNIT_DECLARATION.add(g);
		}

		ARRAY_DECLARATION = new TokenStructure();
		
		{
			//[BRACE:[] <NUMBER COMMA...> [BRACE:]]
			TokenGroup g = new TokenGroup();
			g.append(new TokenItem(TokenType.BRACE,"["));
			g.append(new TokenList(TokenType.NUMBER,TokenType.COMMA,true));
			g.append(new TokenItem(TokenType.BRACE,"]"));
			
			ARRAY_DECLARATION.add(g);
		}
	}
}
