package com.energyxxer.craftr.main;

import com.energyxxer.craftr.global.ProjectManager;
import com.energyxxer.craftr.global.Resources;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.util.ImageManager;
import com.energyxxer.craftr.util.Version;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;

public class Craftr {
	public static Craftr craftr;
	public static final Version VERSION = new Version(0,0,0);
	public static final boolean DEV = true;

	public static CraftrWindow window;

	private Craftr() {
		window = new CraftrWindow();
	}

	public static void main(String[] args) {
		JFrame splash = new JFrame();
		splash.setSize(new Dimension(512, 256));
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		center.x -= 256;
		center.y -= 128;
		splash.setLocation(center);
		splash.setUndecorated(true);
		splash.setContentPane(new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(ImageManager.load("/assets/logo/splash.png").getScaledInstance(512, 256, Image.SCALE_SMOOTH), 0,0,this.getWidth(),this.getHeight(), null);
			}
		});
		splash.setVisible(true);
		splash.setIconImage(ImageManager.load("/assets/logo/logo.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH));

		Resources.load();

		craftr = new Craftr();
		
		ProjectManager.loadWorkspace();

		splash.setVisible(false);
		splash.dispose();
		
		/*String s = "name=Test\n";
		s += "prefix=t";
		
		new Project(ProjectFrom.CONFIG,s);*/
		
		/*TagCompound root = new TagCompound("");
		
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
		}*/
		
		//System.out.println(root.toAnonymousString());
		
/*
		ArrayList<Token> sampleStream = new ArrayList<Token>();


		File file = new File(Preferences.get("workspace_dir") + File.separator + "Jetpack Brawl.txt" + File.separator
				+ "obsidian_boat.craftr");

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

		 System.out.println(StringUtil.escapeHTML(CraftrStructures.ARRAY_DECLARATION.toString()));

		TokenMatchResponse match = CraftrStructures.ARRAY_DECLARATION.match(sampleStream);

		System.out.println(" > " + match);

		System.out.println(match.getFormattedErrorMessage());*/
	}

}
