package com.energyxxer.craftr.main;

import com.energyxxer.craftr.global.Preferences;
import com.energyxxer.craftr.global.Resources;
import com.energyxxer.craftr.global.TabManager;
import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.util.Version;
import com.energyxxer.craftrlang.projects.ProjectManager;
import com.energyxxer.util.ImageManager;

import javax.swing.*;
import java.awt.*;

public class Craftr {
	public static Craftr craftr;
	public static final Version VERSION = new Version(0,0,0);

	public static CraftrWindow window;

	private Craftr() {
		window = new CraftrWindow();
	}

	public static void main(String[] args) {
		JFrame splash = new JFrame();
		splash.setSize(new Dimension(700, 410));
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		center.x -= 700/2;
		center.y -= 410/2;
		splash.setLocation(center);
		splash.setUndecorated(true);
		splash.setVisible(true);
		splash.setContentPane(new JComponent() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(ImageManager.load("/assets/logo/splash.png"), 0,0,this.getWidth(),this.getHeight(), null);

				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(new Color(187, 187, 187));
				g.setFont(g.getFont().deriveFont(32f));
				g.drawString("v." + VERSION.toString(), 470, 320);
				g.dispose();
			}
		});
		splash.revalidate();
		splash.setIconImage(ImageManager.load("/assets/logo/logo.png").getScaledInstance(32, 32, Image.SCALE_SMOOTH));

		Resources.load();

		craftr = new Craftr();

		CraftrWindow.setVisible(true);

        splash.setVisible(false);
        splash.dispose();

        if(Preferences.get("workspace_dir", null) == null) {
            WorkspaceDialog.prompt();
        }

        ProjectManager.loadWorkspace();

		CraftrWindow.welcomePane.tipScreen.start(1000);
		TabManager.openSavedTabs();
		CraftrWindow.projectExplorer.openExplorerTree();
	}

}
