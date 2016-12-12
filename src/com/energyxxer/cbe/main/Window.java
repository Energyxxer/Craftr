package com.energyxxer.cbe.main;

import com.energyxxer.cbe.compile.Compiler;
import com.energyxxer.cbe.global.Preferences;
import com.energyxxer.cbe.global.ProjectManager;
import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.syntax.CBESyntax;
import com.energyxxer.cbe.syntax.CBESyntaxDark;
import com.energyxxer.cbe.syntax.Syntax;
import com.energyxxer.cbe.ui.ToolbarButton;
import com.energyxxer.cbe.ui.ToolbarSeparator;
import com.energyxxer.cbe.ui.common.MenuItems;
import com.energyxxer.cbe.ui.components.themechange.TCMenu;
import com.energyxxer.cbe.ui.components.themechange.TCMenuItem;
import com.energyxxer.cbe.ui.explorer.Explorer;
import com.energyxxer.cbe.ui.theme.DarkTheme;
import com.energyxxer.cbe.ui.theme.LightTheme;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.ImageManager;
import com.energyxxer.cbe.util.out.MultiOutputStream;
import com.energyxxer.cbe.util.out.TextAreaOutputStream;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Literally what it sounds like.
 */
public class Window {

	private static final boolean useConsole = true;

	public static JFrame jframe;

	private static JMenuBar menuBar;

	public static Explorer explorer;
	public static JPanel tabList;

	public static JPanel edit_area;
	
	public static Theme theme = LightTheme.getInstance();

	private static final Dimension defaultSize = new Dimension(1200, 800);

	public static PrintStream consoleOut = new PrintStream(System.out);
	private static TextAreaOutputStream textConsoleOut = null;
	private static final int CONSOLE_HEIGHT = 200;

	public Window() {
		jframe = new JFrame("Command Block Engine");
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		ThemeChangeListener.addThemeChangeListener(t -> jframe.getContentPane().setBackground(t.p3));

		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}*/

		menuBar = new JMenuBar();
		ThemeChangeListener.addThemeChangeListener(t -> {
			menuBar.setBackground(t.p3);
			menuBar.setBorder(BorderFactory.createMatteBorder(0,0,1,0,t.l2));
		});

		menuBar.setPreferredSize(new Dimension(0, 20));
		jframe.setJMenuBar(menuBar);

		{
            TCMenu menu = new TCMenu(" File ");

            menu.setMnemonic(KeyEvent.VK_F);

            // --------------------------------------------------


            TCMenu newMenu = MenuItems.newMenu("New                    ");
            menu.add(newMenu);

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Save", "save");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Save As", "save_as");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 3));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Save All", "save_all");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 10));
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Close");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Close All");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, 3));
                menu.add(item);
            }


            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Move");
                menu.add(item);
            }

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Rename", "rename");
                menu.add(item);
            }

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Refresh", "reload");
                item.addActionListener(e -> Window.explorer.generateProjectList());
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Exit");
                item.addActionListener(e ->	jframe.dispatchEvent(new WindowEvent(jframe, WindowEvent.WINDOW_CLOSING)));
                menu.add(item);
            }

            // --------------------------------------------------

            menuBar.add(menu);
        }

		{
            TCMenu menu = new TCMenu(" Edit ");
            menu.setMnemonic(KeyEvent.VK_E);

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Undo", "undo");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Redo", "redo");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Copy");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Cut");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Paste");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                TCMenuItem item = new TCMenuItem("Delete");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
                menu.add(item);
            }

            // --------------------------------------------------

            menuBar.add(menu);
        }

		{
            TCMenu menu = new TCMenu(" Project ");
            menu.setMnemonic(KeyEvent.VK_P);

            // --------------------------------------------------

			{
				TCMenuItem item = new TCMenuItem("Generate                    ", "export");
				item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 9));
				menu.add(item);
			}

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

			{
				TCMenuItem item = new TCMenuItem("Properties");
				item.addActionListener(e -> {if(ProjectManager.getSelected() != null) ProjectManager.getSelected().showPropertiesDialog();});
				menu.add(item);
			}

            // --------------------------------------------------

            menuBar.add(menu);
        }

		{
			TCMenu menu = new TCMenu(" Debug ");
			menu.setMnemonic(KeyEvent.VK_D);

            // --------------------------------------------------

			{
				TCMenuItem item = new TCMenuItem("Reset Preferences", "warn");
				item.addActionListener(e -> {
					int confirmation = JOptionPane.showConfirmDialog(null,
							"        Are you sure you want to reset all saved settings?        ",
							"Reset Preferences? ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (confirmation == JOptionPane.YES_OPTION) {
						Preferences.reset();
					}
				});
				menu.add(item);
			}

            // --------------------------------------------------

            menuBar.add(menu);
        }

		{
			TCMenu menu = new TCMenu(" Window ");
			menu.setMnemonic(KeyEvent.VK_W);

            // --------------------------------------------------

            TCMenu setThemeItem = new TCMenu("Set Theme        ");
            menu.add(setThemeItem);

            // --------------------------------------------------

            TCMenuItem setLightThemeItem = new TCMenuItem("Light Theme");
            setThemeItem.add(setLightThemeItem);

            setLightThemeItem.addActionListener(e -> setTheme(LightTheme.getInstance()));

            // --------------------------------------------------

            TCMenuItem setDarkThemeItem = new TCMenuItem("Dark Theme");
            setThemeItem.add(setDarkThemeItem);

            setDarkThemeItem.addActionListener(e -> setTheme(DarkTheme.getInstance()));

            // --------------------------------------------------

            menuBar.add(menu);
        }

		jframe.setLayout(new BorderLayout());

		{
			JPanel toolbar = new JPanel();
			toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
			toolbar.setPreferredSize(new Dimension(1, 30));
			ThemeChangeListener.addThemeChangeListener(t -> {
				toolbar.setBackground(t.p2);
				toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.l1));
			});
			jframe.getContentPane().add(toolbar, BorderLayout.NORTH);

			toolbar.add(new ToolbarSeparator());

			{
				ToolbarButton button = new ToolbarButton("project_new");
				button.setToolTipText("New Project");
				toolbar.add(button);
			}

			{
				ToolbarButton button = new ToolbarButton("save");
				button.setToolTipText("Save File");
				toolbar.add(button);
			}

			{
				ToolbarButton button = new ToolbarButton("save_all");
				button.setToolTipText("Save All Files");
				toolbar.add(button);
			}

			toolbar.add(new ToolbarSeparator());

			{
				ToolbarButton button = new ToolbarButton("undo");
				button.setToolTipText("Undo");
				toolbar.add(button);
			}

			{
				ToolbarButton button = new ToolbarButton("redo");
				button.setToolTipText("Redo");
				toolbar.add(button);
			}

			toolbar.add(new ToolbarSeparator());

			{
				ToolbarButton button = new ToolbarButton("entity_new");
				button.setToolTipText("New Entity");
				toolbar.add(button);
			}

			{
				ToolbarButton button = new ToolbarButton("item_new");
				button.setToolTipText("New Item");
				toolbar.add(button);
			}

			toolbar.add(new ToolbarSeparator());

			{
				ToolbarButton button = new ToolbarButton("export");
				button.setToolTipText("Generate Structure");
				button.addActionListener(e -> Compiler.compile());
				toolbar.add(button);
			}

		}

		{
			JPanel sidebar = new JPanel();
			sidebar.setLayout(new BorderLayout());
			sidebar.setPreferredSize(new Dimension(350, 500));
			ThemeChangeListener.addThemeChangeListener(t -> {
				sidebar.setBackground(t.p1);
				sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, t.l1));
			});
			jframe.getContentPane().add(sidebar, BorderLayout.WEST);

			JPanel header = new JPanel(new BorderLayout());
			ThemeChangeListener.addThemeChangeListener(t -> header.setBackground(t.p1));

			JLabel label = new JLabel("    Project Explorer");
			ThemeChangeListener.addThemeChangeListener(t -> {
				label.setFont(new Font(t.font1, Font.PLAIN, 14));
				label.setForeground(theme.t1);
			});
			label.setPreferredSize(new Dimension(500, 25));
			header.add(label, BorderLayout.WEST);

			ToolbarButton refresh = new ToolbarButton("reload");
			refresh.setToolTipText("Refresh Explorer");

			refresh.addActionListener(e -> Window.explorer.generateProjectList());

			header.add(refresh, BorderLayout.EAST);
			sidebar.add(header, BorderLayout.NORTH);

			JScrollPane sp = new JScrollPane();
			sp.getViewport().setBackground(Color.BLACK);

			sp.getViewport().add(explorer = new Explorer());
			ThemeChangeListener.addThemeChangeListener(t -> {
				sp.setBackground(t.p1);
				sp.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, t.p1));
			});

			sidebar.add(sp, BorderLayout.CENTER);
		}

		edit_area = new JPanel(new BorderLayout());
		edit_area.setPreferredSize(new Dimension(500, 500));
		ThemeChangeListener.addThemeChangeListener(t -> edit_area.setBackground(t.p3));
		jframe.getContentPane().add(edit_area, BorderLayout.CENTER);

		JPanel tabListHolder = new JPanel(new BorderLayout());
		tabListHolder.setPreferredSize(new Dimension(1,30));
		ThemeChangeListener.addThemeChangeListener(t -> tabListHolder.setBackground(t.p4));
		
		JPanel tabActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
		tabActionPanel.setOpaque(false);
		ThemeChangeListener.addThemeChangeListener(t -> tabActionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.l1)));

		{
			ToolbarButton more = new ToolbarButton("more");
			more.setToolTipText("View all tabs");
			more.setPreferredSize(new Dimension(25,25));
			tabActionPanel.add(more);
			
			more.addActionListener(e -> TabManager.getMenu().show(more, more.getX(), more.getY() + more.getHeight()));

		}
		
		tabListHolder.add(tabActionPanel, BorderLayout.EAST);
		
		edit_area.add(tabListHolder, BorderLayout.NORTH);
		tabList = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		tabList.setPreferredSize(new Dimension(1, 30));
		ThemeChangeListener.addThemeChangeListener(t -> {
			tabList.setBackground(t.p4);
			tabList.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.l1));
		});
		tabListHolder.add(tabList, BorderLayout.CENTER);

		if (useConsole) {
			JPanel consoleArea = new JPanel(new BorderLayout());
			consoleArea.setPreferredSize(new Dimension(0, CONSOLE_HEIGHT));
			ThemeChangeListener.addThemeChangeListener(t -> consoleArea.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, t.l1)));

			JPanel consoleHeader = new JPanel(new BorderLayout());
			ThemeChangeListener.addThemeChangeListener(t -> consoleHeader.setBackground(t.p2));
			consoleHeader.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
			consoleHeader.setPreferredSize(new Dimension(0, 25));

			JLabel consoleLabel = new JLabel("Java Console");
			ThemeChangeListener.addThemeChangeListener(t -> {
				consoleLabel.setForeground(t.t1);
				consoleLabel.setFont(new Font(t.font1, 0, 12));
			});
			consoleHeader.add(consoleLabel, BorderLayout.WEST);
			
			JPanel consoleActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
			consoleActionPanel.setOpaque(false);

			ToolbarButton toggle = new ToolbarButton("toggle", true);
			toggle.setToolTipText("Toggle Java Console");
			toggle.setPreferredSize(new Dimension(20,20));

			toggle.addActionListener(e -> {
				if (consoleArea.getPreferredSize().height == 25) {
					consoleArea.setPreferredSize(new Dimension(0, CONSOLE_HEIGHT));
				} else {
					consoleArea.setPreferredSize(new Dimension(0, 25));
				}
				consoleArea.revalidate();
				consoleArea.repaint();
			});


			ToolbarButton clear = new ToolbarButton("clear", true);
			clear.setToolTipText("Clear Console");
			clear.setPreferredSize(new Dimension(20,20));
			
			clear.addActionListener(e -> textConsoleOut.clear());

			consoleActionPanel.add(clear);
			consoleActionPanel.add(toggle);
			consoleHeader.add(consoleActionPanel, BorderLayout.EAST);

			consoleArea.add(consoleHeader, BorderLayout.NORTH);

			JEditorPane console = new JEditorPane();
			ThemeChangeListener.addThemeChangeListener(t -> console.setBackground(t.p1));
			console.setEditable(false);
			console.setFont(new Font("monospaced", 0, 12));
			console.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			console.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));

			console.addHyperlinkListener(e -> {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					String path = e.getURL().toString().substring(7, e.getURL().toString().lastIndexOf('?'));
					String location = e.getURL().toString().substring(e.getURL().toString().lastIndexOf('?') + 1);
					String length = location.substring(location.indexOf('&')+1);
					location = location.substring(0,location.indexOf('&'));
					TabManager.openTab(path, Integer.parseInt(location.split(":")[0]),
							Integer.parseInt(location.split(":")[1]), Integer.parseInt(length));
				}
			});

			textConsoleOut = new TextAreaOutputStream(console);
			consoleOut = new PrintStream(textConsoleOut);
			System.setOut(new PrintStream(new MultiOutputStream(consoleOut, System.out)));
			System.setErr(new PrintStream(new MultiOutputStream(consoleOut, System.err)));

			JScrollPane consoleScrollPane = new JScrollPane(console);
			ThemeChangeListener.addThemeChangeListener(t -> consoleScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, t.l1)));

			consoleArea.add(consoleScrollPane, BorderLayout.CENTER);

			edit_area.add(consoleArea, BorderLayout.SOUTH);
		}

		jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jframe.setSize(defaultSize);
		jframe.setPreferredSize(defaultSize);
		jframe.setVisible(true);

		List<Image> icons = new ArrayList<>();
		icons.add(
				ImageManager.load("/assets/logo/logo_icon.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
		icons.add(ImageManager.load("/assets/logo/logo.png").getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH));
		jframe.setIconImages(icons);

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point center = env.getCenterPoint();
		center.x -= jframe.getWidth() / 2;
		center.y -= jframe.getHeight() / 2;
		jframe.setLocation(center);
	}
	
	public static Syntax getSyntaxForTheme() {
		return (theme instanceof DarkTheme ? CBESyntaxDark.INSTANCE : CBESyntax.INSTANCE);
	}

	public static void setTheme(Theme t) {
		Window.theme = t;
		ThemeChangeListener.dispatchThemeChange(t);
		UIManager.put("ToolTip.background",t.p1);
		UIManager.put("ToolTip.foreground",t.t1);
		UIManager.put("ToolTip.border",BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1,1,1,1,t.l1),BorderFactory.createEmptyBorder(3,5,3,5)));
	}
}