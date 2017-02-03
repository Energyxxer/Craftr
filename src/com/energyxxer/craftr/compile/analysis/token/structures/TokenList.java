package com.energyxxer.craftr.compile.analysis.token.structures;

import com.energyxxer.craftr.compile.analysis.token.Token;
import com.energyxxer.craftr.util.StringBounds;
import com.energyxxer.craftr.util.StringLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TokenList extends TokenPattern<TokenPattern<?>[]> {
	private ArrayList<TokenPattern<?>> patterns = new ArrayList<TokenPattern<?>>();
	
	public TokenList() {}
	
	public TokenList(ArrayList<TokenPattern<?>> patterns) {
		this.patterns.addAll(patterns);
	}
	
	public void add(TokenPattern<?> pattern) {
		if(pattern != null)	patterns.add(pattern);
	}
	
	@Override
	public TokenList setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public TokenPattern<?>[] getContents() {
		return patterns.toArray(new TokenPattern<?>[0]);
	}

	@Override
	public String toString() {
		String o = "[ ";
		
		for(TokenPattern<?> p : patterns) {
			o += p.toString();
		}
		o += " ]";
		return o;
	}

	@Override
	public List<Token> search(String type) {
		ArrayList<Token> list = new ArrayList<>();
		for(TokenPattern<?> p : patterns) {
			if(p.getContents() instanceof Token) {
				if(((Token) p.getContents()).type.equals(type)) list.add((Token) p.getContents());
			}
		}
		return list;
	}

	@Override
	public List<Token> deepSearch(String type) {
		ArrayList<Token> list = new ArrayList<>();
		for(TokenPattern<?> p : patterns) {
			list.addAll(p.deepSearch(type));
		}
		return list;
	}

	@Override
	public List<TokenPattern<?>> searchByName(String name) {
		ArrayList<TokenPattern<?>> list = new ArrayList<>();
		for(TokenPattern<?> p : patterns) {
			if(p.name.equals(name)) list.add(p);
		}
		return list;
	}

	@Override
	public List<TokenPattern<?>> deepSearchByName(String name) {
		ArrayList<TokenPattern<?>> list = new ArrayList<>();
		for(TokenPattern<?> p : patterns) {
			if(p.name.equals(name)) list.add(p);
			list.addAll(p.deepSearchByName(name));
		}
		return list;
	}

	@Override
	public String flatten(boolean separate) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < patterns.size(); i++) {
			sb.append(patterns.get(i).flatten(separate));
			if(i < patterns.size()-1 && separate) sb.append(" ");
		}
		return sb.toString();
	}

	@Override
	public File getFile() {
		return (patterns != null && patterns.size() > 0) ? patterns.get(0).getFile() : null;
	}

	@Override
	public StringLocation getStringLocation() {
		if (patterns == null || patterns.size() <= 0) return null;
		StringLocation l = null;
		for (TokenPattern<?> pattern : patterns) {
			StringLocation loc = pattern.getStringLocation();
			if(l == null) {
				l = loc;
				continue;
			}
			if(loc.index < l.index) {
				l = loc;
			}
		}
		return l;
	}

	@Override
	public StringBounds getStringBounds() {
		if (patterns == null || patterns.size() <= 0) return null;
		StringLocation start = null;
		StringLocation end = null;

		//Find start
		for (TokenPattern<?> pattern : patterns) {
			StringLocation loc = pattern.getStringLocation();
			if(start == null) {
				start = loc;
				continue;
			}
			if(loc.index < start.index) {
				start = loc;
			}
		}

		//Find end
		for (TokenPattern<?> pattern : patterns) {
			StringLocation loc = pattern.getStringBounds().end;
			if(end == null) {
				end = loc;
				continue;
			}
			if(loc.index > end.index) {
				end = loc;
			}
		}
		return new StringBounds(start, end);
	}

    @Override
    public ArrayList<Token> flattenTokens() {
        ArrayList<Token> list = new ArrayList<>();
        for(TokenPattern<?> pattern : patterns) {
            list.addAll(pattern.flattenTokens());
        }
        return list;
    }

	@Override
	public String getType() {
		return "LIST";
	}
}
