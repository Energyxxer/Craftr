package com.energyxxer.cbe.main;

import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.main.window.Window;
import com.energyxxer.cbe.minecraft.schematic.block.nbt.TagCompound;
import com.energyxxer.cbe.minecraft.schematic.block.nbt.TagInt;
import com.energyxxer.cbe.minecraft.schematic.block.nbt.TagList;
import com.energyxxer.cbe.minecraft.schematic.block.nbt.TagString;
import com.energyxxer.cbe.ui.theme.LightTheme;
import com.energyxxer.cbe.ui.theme.ThemeManager;

public class CBE {
	public static CBE generator;

	public static Window window;

	CBE() {
		window = new Window();
	}

	public static void main(String[] args) {
		LightTheme.getInstance();

		ThemeManager.loadAll();

		generator = new CBE();
		
		ProjectManager.loadWorkspace();
		Window.explorer.generateProjectList();
		
		/*String s = "name=Test\n";
		s += "prefix=t";
		
		new Project(ProjectFrom.CONFIG,s);*/
		
		TagCompound root = new TagCompound("");
		
		{
			root.add(new TagString("Block","redstone_block"));
			root.add(new TagInt("Time",1));
			TagList pl1 = new TagList("Passengers");
			root.add(pl1);
			{
				TagCompound p1 = new TagCompound("");
				pl1.add(p1);
				p1.add(new TagString("id","falling_block"));
				p1.add(new TagString("Block","activator_rail"));
				p1.add(new TagInt("Time",1));
				TagList pl2 = new TagList("Passengers");
				p1.add(pl2);
				{
					TagCompound p2 = new TagCompound("");
					pl2.add(p2);
					p2.add(new TagString("id","commandblock_minecart"));
					p2.add(new TagString("Command","setblock ~1 ~-2 ~ command_block 3 _ {auto:1,TrackOutput:0b,Command:say Hello World!}"));
				}
				{
					TagCompound p2 = new TagCompound("");
					pl2.add(p2);
					p2.add(new TagString("id","commandblock_minecart"));
					p2.add(new TagString("Command","tellraw @a {\"text\":\"\\\"Sample Project\\\" has been successfully installed!\",\"color\":\"green\"}"));
				}
				{
					TagCompound p2 = new TagCompound("");
					pl2.add(p2);
					p2.add(new TagString("id","commandblock_minecart"));
					p2.add(new TagString("Command","setblock ~ ~1 ~ command_block 0 _ {auto:1b,Command:fill ~ ~-3 ~ ~ ~ ~ air}"));
				}
				{
					TagCompound p2 = new TagCompound("");
					pl2.add(p2);
					p2.add(new TagString("id","commandblock_minecart"));
					p2.add(new TagString("Command","kill @e[type=commandblock_minecart,dx=0]"));
				}
			}
		}
		
		//System.out.println(root.toAnonymousString());
		
/*
		ArrayList<Token> sampleStream = new ArrayList<Token>();


		File file = new File(Preferences.get("workspace_dir") + File.separator + "Jetpack Brawl.txt" + File.separator
				+ "obsidian_boat.mcbe");

		sampleStream.add(new Token("[", file, new StringLocation(1,1,1)));
		sampleStream.add(new Token("1", TokenType.NUMBER, file, new StringLocation(1,1,1)));
		sampleStream.add(new Token(",", file, new StringLocation(1,1,1)));
		sampleStream.add(new Token("3.2", TokenType.NUMBER, file, new StringLocation(1,1,1)));
		sampleStream.add(new Token(",", file, new StringLocation(1,1,1)));
		sampleStream.add(new Token("92", TokenType.NUMBER, file, new StringLocation(1,1,1)));
		sampleStream.add(new Token(",",file,new StringLocation(1,1,1)));
		sampleStream.add(new Token("]", file, new StringLocation(1,10,15)));

		
		for(int i = 0; i < sampleStream.size(); i++) {
		System.out.print(sampleStream.get(i)); if(i < sampleStream.size()-1)
		System.out.print(" "); }
		

		 System.out.println(" ...matches...");

		 System.out.println(StringUtil.escapeHTML(LangStructures.ARRAY_DECLARATION.toString()));

		TokenMatchResponse match = LangStructures.ARRAY_DECLARATION.match(sampleStream);

		System.out.println(" > " + match);

		System.out.println(match.getFormattedErrorMessage());*/
	}

}
