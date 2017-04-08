package com.energyxxer.craftr.minecraft.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Selector {
	
	public boolean valid = true;
	public String header = "@a";
	public HashMap<String, String> args = new HashMap<String, String>();
	
	private enum Action {
		GET_KEY,
		GET_VALUE
	}
	
	public Selector() {}
	
	public Selector(String sel) {
		
		if(sel == null) {
			valid = false;
			return;
		}
		
		if("*".equals(sel)) {
			header = sel;
			return;
		}
        List<String> validHeaders = Arrays.asList("@a","@p","@e","@r");
        if(sel.length() >= 2 && validHeaders.contains(sel.substring(0,2))) {
            this.header = sel.substring(0,2);
        } else {
        	this.valid = false;
        	return;
        }

        sel = sel.substring(2);

        HashMap<String, String> tempArgs = new HashMap<String, String>();

        String token = "";
        String key = "";
        int argsCount = 0;

        Action action = Action.GET_KEY;

        iteration: for(int i = 0; i < sel.length(); i++) {
            char c = sel.charAt(i);

            if(i == 0) {
                if(c != '[') break; else continue;
            }

            if("]=, ".contains(""+c)) {
                switch(c) {
                    case ']':
                    case ',': {
                        if(action == Action.GET_KEY && token.length() > 0) {
                        	try {
                        		key = new String[] {"x","y","z","r"}[argsCount];
                        	} catch(ArrayIndexOutOfBoundsException e) {
                        		break iteration;
                        	}
                            tempArgs.put(key, token);
                            token = key = "";
                            argsCount++;
                            break;
                        } else if(action == Action.GET_VALUE) {
                            tempArgs.put(key, token);
                            token = key = "";
                            action = Action.GET_KEY;
                            argsCount++;
                            break;
                        }
                        if(c == ']') {
                            break iteration;
                        }
                    }
                    case '=': {
                        if(action == Action.GET_KEY) {
                            key = token;
                            token = "";
                            action = Action.GET_VALUE;
                            break;
                        } else if(action == Action.GET_VALUE) {
                            break iteration;
                        }
                    } case ' ': {
                        break iteration;
                    }
                }
            } else {
                token += c;
            }

        }
        this.args = tempArgs;

	}
	public Selector clone() {
		
		Selector ns = new Selector();
		ns.valid = this.valid;
		ns.header = this.header;
		
		Iterator<String> it = this.args.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			ns.args.put(key, this.args.get(key));
		}
		return ns;
	}

	@Override
	public String toString() {
	    if(!this.valid) return "null";
	    String s = this.header;
	    boolean hasArgs = false;
	    String argsRaw = "";

		Iterator<String> it = this.args.keySet().iterator();
		ArrayList<String> reversed = new ArrayList<String>();
		while(it.hasNext()) {
	        hasArgs = true;
			String key = it.next();
			reversed.add(0,key + "=" + this.args.get(key) + ",");
	    }
		for(String a : reversed) {
			argsRaw += a;
		}
	    if(argsRaw.charAt(argsRaw.length()-1) == ',') argsRaw = argsRaw.substring(0,argsRaw.length()-1);

	    s += (hasArgs) ? ("[" + argsRaw + "]") : "";
	    return s;
	}
}
