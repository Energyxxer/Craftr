package com.energyxxer.cbe.main.window;

import com.energyxxer.cbe.global.Preferences;
import com.energyxxer.cbe.global.Status;
import com.energyxxer.cbe.main.CBE;
import com.energyxxer.cbe.main.window.sections.EditArea;
import com.energyxxer.cbe.main.window.sections.MenuBar;
import com.energyxxer.cbe.main.window.sections.Sidebar;
import com.energyxxer.cbe.main.window.sections.StatusBar;
import com.energyxxer.cbe.main.window.sections.Toolbar;
import com.energyxxer.cbe.syntax.CBESyntax;
import com.energyxxer.cbe.syntax.CBESyntaxDark;
import com.energyxxer.cbe.syntax.Syntax;
import com.energyxxer.cbe.ui.explorer.Explorer;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.ThemeManager;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.ImageManager;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Literally what it sounds like.
 */
public class Window {

	public static JFrame jframe;

	public static Explorer explorer;

	public static MenuBar menuBar;
	public static Toolbar toolbar;
	public static Sidebar sidebar;
	public static EditArea editArea;
	public static StatusBar statusBar;
	
	private static Theme theme = new Theme("null");

	private static final Dimension defaultSize = new Dimension(1200, 800);


	public Window() {
		ThemeManager.loadAll();
		ThemeManager.setTheme(Preferences.get("theme"));

		jframe = new JFrame();
		setTitle("");
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		ThemeChangeListener.addThemeChangeListener(t -> jframe.getContentPane().setBackground(t.getColor("Window.background",new Color(215, 215, 215))));

		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}*/

		jframe.setJMenuBar(menuBar = new MenuBar());

		jframe.setLayout(new BorderLayout());

		jframe.getContentPane().add(toolbar = new Toolbar(), BorderLayout.NORTH);

		jframe.getContentPane().add(sidebar = new Sidebar(), BorderLayout.WEST);

		jframe.getContentPane().add(editArea = new EditArea(), BorderLayout.CENTER);

		jframe.getContentPane().add(statusBar = new StatusBar(), BorderLayout.SOUTH);

		jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jframe.setSize(defaultSize);
		jframe.setPreferredSize(defaultSize);
		jframe.setVisible(true);

		List<Image> icons = new ArrayList<>();
		icons.add(
				ImageManager.load("/assets/logo/logo_icon.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
		icons.add(ImageManager.load("/assets/logo/logo.png").getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH));
		jframe.setIconImages(icons);
		//jframe.setIconImage(ImageManager.load("/assets/logo/logo.png"));

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point center = env.getCenterPoint();
		center.x -= jframe.getWidth() / 2;
		center.y -= jframe.getHeight() / 2;
		jframe.setLocation(center);

		ThemeChangeListener.addThemeChangeListener(t -> {
			UIManager.put("ToolTip.background",t.getColor("Tooltip.background",Color.WHITE));
			UIManager.put("ToolTip.foreground",t.getColor("Tooltip.foreground",Color.BLACK));
			UIManager.put("ToolTip.border",BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,1,1,t.getColor("Tooltip.border",Color.BLACK)),BorderFactory.createEmptyBorder(3,5,3,5)));
		});

	}
	
	public static Syntax getSyntaxForTheme() {
		if(theme == null) return CBESyntax.INSTANCE;
		switch(theme.getString("Syntax", "Light")) {
			case "Dark": return CBESyntaxDark.INSTANCE;
			case "Light": return CBESyntax.INSTANCE;
			default: return CBESyntax.INSTANCE;
		}
	}

	public static void setTheme(Theme t) {
		if(statusBar != null && !t.equals(theme)) {
			Status themeSetStatus = new Status("Theme set to: " + t.getName());

			setStatus(themeSetStatus);

			new Timer().schedule(new TimerTask() {
				public void run() {
					Window.dismissStatus(themeSetStatus);
				}
			}, 5000);
		}

		theme = t;
		ThemeChangeListener.dispatchThemeChange(t);
	}

    public static Theme getTheme() {
        return theme;
    }

	public static void setStatus(String text) {
		statusBar.setStatus(text);
	}

	public static void setStatus(Status status) {
		statusBar.setStatus(status);
	}

	public static void setTitle(String title) {
		jframe.setTitle(title + ((title.length() > 0) ? " - " : "") + "Command Block Engine " + CBE.VERSION + ((CBE.DEV) ? " DEV" : ""));
	}

	public static void clearTitle() {
		setTitle("");
	}

	public static void dismissStatus(Status status) {
		statusBar.dismissStatus(status);
	}

    public static void close() {
		jframe.dispatchEvent(new WindowEvent(jframe, WindowEvent.WINDOW_CLOSING));
	}
}