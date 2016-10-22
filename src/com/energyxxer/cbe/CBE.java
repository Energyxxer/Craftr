package com.energyxxer.cbe;

import java.io.File;
import java.util.ArrayList;

import javax.swing.Timer;

import com.energyxxer.cbe.analysis.LangStructures;
import com.energyxxer.cbe.analysis.token.Token;
import com.energyxxer.cbe.analysis.token.TokenMatchResponse;
import com.energyxxer.cbe.analysis.token.TokenType;
import com.energyxxer.util.StringLocation;

public class CBE {
	public static CBE generator;

	public static Window window;

	public static Timer timer;

	CBE() {
		window = new Window();
	}

	public static void main(String[] args) {

		generator = new CBE();

		Window.explorer.generateProjectList();

		ArrayList<Token> sampleStream = new ArrayList<Token>();
		/*
		 * sampleStream.add(new Token("public","null",0,0));
		 * sampleStream.add(new Token("static","null",0,0));
		 * sampleStream.add(new Token("synchronized","null",0,0));
		 * sampleStream.add(new Token("entity","null",0,0));
		 * sampleStream.add(new Token("phyg","null",0,0)); sampleStream.add(new
		 * Token("base","null",0,0)); sampleStream.add(new
		 * Token("pig","null",0,0)); sampleStream.add(new
		 * Token("{...","null",0,0));
		 */

		File file = new File(Preferences.get("workspace_dir") + File.separator + "Jetpack Brawl.txt" + File.separator
				+ "obsidian_boat.mcbe");

		sampleStream.add(new Token("[", file, new StringLocation(1,1,1)));
		sampleStream.add(new Token("1", TokenType.NUMBER, file, new StringLocation(1,1,1)));
		sampleStream.add(new Token(",", file, new StringLocation(1,1,1)));
		sampleStream.add(new Token("3.2", TokenType.NUMBER, file, new StringLocation(1,1,1)));
		sampleStream.add(new Token(",", file, new StringLocation(1,1,1)));
		sampleStream.add(new Token("92", TokenType.NUMBER, file, new StringLocation(1,1,1)));
		// sampleStream.add(new Token(",",file,1,1));
		sampleStream.add(new Token("]", file, new StringLocation(1,10,15)));

		/*
		 * for(int i = 0; i < sampleStream.size(); i++) {
		 * System.out.print(sampleStream.get(i)); if(i < sampleStream.size()-1)
		 * System.out.print(" "); }
		 */

		// System.out.println(" ...matches...");

		// System.out.println(StringUtil.escapeHTML(LangStructures.ARRAY_DECLARATION.toString()));

		TokenMatchResponse match = LangStructures.ARRAY_DECLARATION.match(sampleStream);

		// System.out.println(" > " + match);

		System.out.println(match.getFormattedErrorMessage());
	}

}
