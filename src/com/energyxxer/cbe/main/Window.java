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
import com.energyxxer.cbe.ui.dialogs.Settings;
import com.energyxxer.cbe.ui.explorer.Explorer;
import com.energyxxer.cbe.ui.scrollbar.ScrollbarUI;
import com.energyxxer.cbe.ui.styledcomponents.StyledMenu;
import com.energyxxer.cbe.ui.styledcomponents.StyledMenuItem;
import com.energyxxer.cbe.ui.theme.DarkTheme;
import com.energyxxer.cbe.ui.theme.LightTheme;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.ThemeManager;
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
	
	private static Theme theme = new Theme("null");

	private static final Dimension defaultSize = new Dimension(1200, 800);

	public static PrintStream consoleOut = new PrintStream(System.out);
	private static TextAreaOutputStream textConsoleOut = null;
	private static final int CONSOLE_HEIGHT = 200;

	public Window() {

		ThemeManager.loadAll();
		ThemeManager.setTheme(Preferences.get("theme"));

		jframe = new JFrame("Command Block Engine");
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		ThemeChangeListener.addThemeChangeListener(t -> jframe.getContentPane().setBackground(t.getColor("Window.background",new Color(215, 215, 215))));

		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}*/

		menuBar = new JMenuBar();
		ThemeChangeListener.addThemeChangeListener(t -> {
			menuBar.setBackground(t.getColor("MenuBar.background",new Color(215, 215, 215)));
			menuBar.setBorder(BorderFactory.createMatteBorder(0,0,1,0,t.getColor("MenuBar.border",new Color(150, 150, 150))));
		});

		menuBar.setPreferredSize(new Dimension(0, 20));
		jframe.setJMenuBar(menuBar);

		{
            StyledMenu menu = new StyledMenu(" File ");

            menu.setMnemonic(KeyEvent.VK_F);

            // --------------------------------------------------


            StyledMenu newMenu = MenuItems.newMenu("New                    ");
            menu.add(newMenu);

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Save", "save");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Save As", "save_as");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 3));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Save All", "save_all");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 10));
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Close");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Close All");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, 3));
                menu.add(item);
            }


            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Move");
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Rename", "rename");
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Refresh", "reload");
                item.addActionListener(e -> Window.explorer.generateProjectList());
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Exit");
                item.addActionListener(e ->	jframe.dispatchEvent(new WindowEvent(jframe, WindowEvent.WINDOW_CLOSING)));
                menu.add(item);
            }

            // --------------------------------------------------

            menuBar.add(menu);
        }

		{
            StyledMenu menu = new StyledMenu(" Edit ");
            menu.setMnemonic(KeyEvent.VK_E);

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Undo", "undo");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Redo", "redo");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Copy");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Cut");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Paste");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
                menu.add(item);
            }

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

            {
                StyledMenuItem item = new StyledMenuItem("Delete");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
                menu.add(item);
            }

            // --------------------------------------------------

            menuBar.add(menu);
        }

		{
            StyledMenu menu = new StyledMenu(" Project ");
            menu.setMnemonic(KeyEvent.VK_P);

            // --------------------------------------------------

			{
				StyledMenuItem item = new StyledMenuItem("Generate                    ", "export");
				item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 9));
				menu.add(item);
			}

            // --------------------------------------------------

            menu.addSeparator();

            // --------------------------------------------------

			{
				StyledMenuItem item = new StyledMenuItem("Properties");
				item.addActionListener(e -> {if(ProjectManager.getSelected() != null) ProjectManager.getSelected().showPropertiesDialog();});
				menu.add(item);
			}

            // --------------------------------------------------

            menuBar.add(menu);
        }

		{
			StyledMenu menu = new StyledMenu(" Debug ");
			menu.setMnemonic(KeyEvent.VK_D);

            // --------------------------------------------------

			{
				StyledMenuItem item = new StyledMenuItem("Reset Preferences", "warn");
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
			StyledMenu menu = new StyledMenu(" Window ");
			menu.setMnemonic(KeyEvent.VK_W);

			// --------------------------------------------------

			{
				StyledMenuItem item = new StyledMenuItem("Settings");

				item.addActionListener(e -> Settings.show());
				menu.add(item);
			}

            // --------------------------------------------------

            StyledMenu setThemeItem = new StyledMenu("Set Theme        ");
            menu.add(setThemeItem);

            // --------------------------------------------------

            StyledMenuItem setLightThemeItem = new StyledMenuItem("Light Theme");
            setThemeItem.add(setLightThemeItem);

            setLightThemeItem.addActionListener(e -> setTheme(LightTheme.getInstance()));

            // --------------------------------------------------

            StyledMenuItem setDarkThemeItem = new StyledMenuItem("Dark Theme");
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
				toolbar.setBackground(t.getColor("Toolbar.background",new Color(235, 235, 235)));
				toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.getColor("Toolbar.border",new Color(200, 200, 200))));
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
				sidebar.setBackground(t.getColor("Explorer.background",Color.WHITE));
				sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, t.getColor("Explorer.border",new Color(200, 200, 200))));
			});
			jframe.getContentPane().add(sidebar, BorderLayout.WEST);

			JPanel header = new JPanel(new BorderLayout());
			ThemeChangeListener.addThemeChangeListener(t -> header.setBackground(sidebar.getBackground()));

			JLabel label = new JLabel("    Project Explorer");
			ThemeChangeListener.addThemeChangeListener(t -> {
				label.setFont(new Font(t.getString("Explorer.header.font",t.getString("General.font","Tahoma")), Font.PLAIN, 14));
				label.setForeground(t.getColor("Explorer.header.foreground",t.getColor("General.foreground",Color.BLACK)));
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
				sp.setBackground(t.getColor("Explorer.background",Color.WHITE));
				sp.setBorder(BorderFactory.createEmptyBorder());
			});

			sidebar.add(sp, BorderLayout.CENTER);
		}

		edit_area = new JPanel(new BorderLayout());
		edit_area.setPreferredSize(new Dimension(500, 500));
		ThemeChangeListener.addThemeChangeListener(t -> edit_area.setBackground(t.getColor("Editor.background",new Color(215, 215, 215))));
		jframe.getContentPane().add(edit_area, BorderLayout.CENTER);

		JPanel tabListHolder = new JPanel(new BorderLayout());
		tabListHolder.setPreferredSize(new Dimension(1,30));
		ThemeChangeListener.addThemeChangeListener(t -> tabListHolder.setBackground(t.getColor("TabList.background",new Color(200, 202, 205))));
		
		JPanel tabActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
		tabActionPanel.setOpaque(false);
		ThemeChangeListener.addThemeChangeListener(t -> tabActionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.getColor("TabList.border",new Color(200, 200, 200)))));

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
			tabList.setBackground(t.getColor("TabList.background",new Color(200, 202, 205)));
			tabList.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.getColor("TabList.border",new Color(200, 200, 200))));
		});
		tabListHolder.add(tabList, BorderLayout.CENTER);

		if (useConsole) {
			JPanel consoleArea = new JPanel(new BorderLayout());
			consoleArea.setPreferredSize(new Dimension(0, CONSOLE_HEIGHT));
			ThemeChangeListener.addThemeChangeListener(t -> consoleArea.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, t.getColor("Console.header.border",new Color(200, 200, 200)))));

			JPanel consoleHeader = new JPanel(new BorderLayout());
			ThemeChangeListener.addThemeChangeListener(t -> consoleHeader.setBackground(t.getColor("Console.header.background",new Color(235, 235, 235))));
			consoleHeader.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
			consoleHeader.setPreferredSize(new Dimension(0, 25));

			JLabel consoleLabel = new JLabel("Java Console");
			ThemeChangeListener.addThemeChangeListener(t -> {
				consoleLabel.setForeground(t.getColor("Console.header.foreground",Color.BLACK));
				consoleLabel.setFont(new Font(t.getString("Console.header.font",t.getString("General.font","Tahoma")), 0, 12));
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
			ThemeChangeListener.addThemeChangeListener(t -> {
				console.setBackground(t.getColor("Console.background",Color.WHITE));
				console.setSelectionColor(t.getColor("Console.selection.background",t.getColor("General.textfield.selection.background",new Color(50, 100, 175))));
				console.setSelectedTextColor(t.getColor("Console.selection.foreground",t.getColor("General.textfield.selection.foreground",t.getColor("Console.foreground",t.getColor("General.foreground",Color.BLACK)))));
			});
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

			ThemeChangeListener.addThemeChangeListener(t -> textConsoleOut.update());

			consoleOut = new PrintStream(textConsoleOut);
			System.setOut(new PrintStream(new MultiOutputStream(consoleOut, System.out)));
			System.setErr(new PrintStream(new MultiOutputStream(consoleOut, System.err)));

			JScrollPane consoleScrollPane = new JScrollPane(console);

			//consoleScrollPane.setLayout(new OverlayScrollPaneLayout());

			consoleScrollPane.getVerticalScrollBar().setUI(new ScrollbarUI(consoleScrollPane, 20));
			consoleScrollPane.getHorizontalScrollBar().setUI(new ScrollbarUI(consoleScrollPane, 20));
			consoleScrollPane.getVerticalScrollBar().setOpaque(false);
			consoleScrollPane.getHorizontalScrollBar().setOpaque(false);

			ThemeChangeListener.addThemeChangeListener(t -> {
				consoleScrollPane.setBackground(console.getBackground());
				consoleScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, t.getColor("Console.header.border",new Color(200, 200, 200))));
			});

			consoleArea.add(consoleScrollPane, BorderLayout.CENTER);

			edit_area.add(consoleArea, BorderLayout.SOUTH);
		}

		jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jframe.setSize(defaultSize);
		jframe.setPreferredSize(defaultSize);
		jframe.setVisible(true);

		/*List<Image> icons = new ArrayList<>();
		icons.add(
				ImageManager.load("/assets/logo/logo_icon.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
		icons.add(ImageManager.load("/assets/logo/logo.png").getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH));
		jframe.setIconImages(icons);*/
		jframe.setIconImage(ImageManager.load("/assets/logo/logo.png"));

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
		System.out.println("Setting theme " + t);
		Window.theme = t;
		ThemeChangeListener.dispatchThemeChange(t);
	}

    public static Theme getTheme() {
        return theme;
    }
}