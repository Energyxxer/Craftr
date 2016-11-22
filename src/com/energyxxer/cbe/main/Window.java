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
import com.energyxxer.cbe.ui.explorer.Explorer;
import com.energyxxer.cbe.ui.theme.DarkTheme;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.util.ImageManager;
import com.energyxxer.cbe.util.out.MultiOutputStream;
import com.energyxxer.cbe.util.out.TextAreaOutputStream;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Literally what it sounds like.
 */
public class Window {

	public static final boolean useConsole = true;

	public static JFrame jframe;

	public static JMenuBar menuBar;

	public static Explorer explorer;
	public static JPanel tabList;

	public static JPanel edit_area;
	
	public static Theme theme = DarkTheme.getInstance();

	public static Dimension defaultSize = new Dimension(1200, 800);

	public static PrintStream consoleOut = new PrintStream(System.out);
	public static TextAreaOutputStream textConsoleOut = null;
	public static JEditorPane console;
	public static final int CONSOLE_HEIGHT = 200;

	public Window() {
		jframe = new JFrame("Command Block Engine");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jframe.getContentPane().setBackground(theme.p3);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		menuBar = new JMenuBar();
		menuBar.setBackground(theme.p1);

		menuBar.setPreferredSize(new Dimension(0, 20));
		jframe.setJMenuBar(menuBar);

		if (true) {
			{
				JMenu menu = new JMenu(" File ");
				menu.setMnemonic(KeyEvent.VK_F);

				// --------------------------------------------------

				JMenu newMenu = MenuItems.newMenu("New                    ");
				menu.add(newMenu);

				// --------------------------------------------------

				menu.addSeparator();

				// --------------------------------------------------

				JMenuItem saveItem = new JMenuItem("Save", new ImageIcon(ImageManager.load("/assets/icons/light_theme/save.png")
						.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(saveItem);
				saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

				// --------------------------------------------------

				JMenuItem saveAsItem = new JMenuItem("Save As", new ImageIcon(ImageManager
						.load("/assets/icons/light_theme/save_as.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(saveAsItem);
				saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 3));

				// --------------------------------------------------

				JMenuItem saveAllItem = new JMenuItem("Save All", new ImageIcon(ImageManager
						.load("/assets/icons/light_theme/save_all.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(saveAllItem);
				saveAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 10));

				// --------------------------------------------------

				menu.addSeparator();

				// --------------------------------------------------

				JMenuItem closeItem = new JMenuItem("Close");
				menu.add(closeItem);
				closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, 2));

				// --------------------------------------------------

				JMenuItem closeAllItem = new JMenuItem("Close All");
				menu.add(closeAllItem);
				closeAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, 3));

				// --------------------------------------------------

				menu.addSeparator();

				// --------------------------------------------------

				JMenuItem moveItem = new JMenuItem("Move");
				menu.add(moveItem);

				// --------------------------------------------------

				JMenuItem renameItem = new JMenuItem("Rename", new ImageIcon(ImageManager
						.load("/assets/icons/light_theme/rename.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(renameItem);

				// --------------------------------------------------

				JMenuItem refreshItem = new JMenuItem("Refresh", new ImageIcon(ImageManager
						.load("/assets/icons/light_theme/reload.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				refreshItem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Window.explorer.generateProjectList();
					}

				});
				menu.add(refreshItem);

				// --------------------------------------------------

				menu.addSeparator();

				// --------------------------------------------------

				JMenuItem exitItem = new JMenuItem("Exit");
				exitItem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						jframe.dispatchEvent(new WindowEvent(jframe, WindowEvent.WINDOW_CLOSING));
					}

				});
				menu.add(exitItem);

				// --------------------------------------------------

				menuBar.add(menu);
			}

			{
				JMenu menu = new JMenu(" Edit ");
				menu.setMnemonic(KeyEvent.VK_E);

				// --------------------------------------------------

				JMenuItem undoItem = new JMenuItem("Undo                    ", new ImageIcon(ImageManager
						.load("/assets/icons/light_theme/undo.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(undoItem);
				undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));

				// --------------------------------------------------

				JMenuItem redoItem = new JMenuItem("Redo", new ImageIcon(ImageManager.load("/assets/icons/light_theme/redo.png")
						.getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(redoItem);
				redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));

				// --------------------------------------------------

				menu.addSeparator();

				// --------------------------------------------------

				JMenuItem copyItem = new JMenuItem("Copy");
				menu.add(copyItem);
				copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

				// --------------------------------------------------

				JMenuItem cutItem = new JMenuItem("Cut");
				menu.add(cutItem);
				cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

				// --------------------------------------------------

				JMenuItem pasteItem = new JMenuItem("Paste");
				menu.add(pasteItem);
				pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

				// --------------------------------------------------

				menu.addSeparator();

				// --------------------------------------------------

				JMenuItem deleteItem = new JMenuItem("Delete");
				menu.add(deleteItem);
				deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

				// --------------------------------------------------

				menuBar.add(menu);
			}

			{
				JMenu menu = new JMenu(" Project ");
				menu.setMnemonic(KeyEvent.VK_P);

				// --------------------------------------------------

				JMenuItem generateItem = new JMenuItem("Generate                    ");
				menu.add(generateItem);

				// --------------------------------------------------

				menu.addSeparator();

				// --------------------------------------------------

				JMenuItem propertiesItem = new JMenuItem("Properties");
				menu.add(propertiesItem);

				propertiesItem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if(ProjectManager.getSelected() != null) ProjectManager.getSelected().showPropertiesDialog();
					}

				});

				// --------------------------------------------------

				menuBar.add(menu);
			}

			{
				JMenu menu = new JMenu(" Debug ");
				menu.setMnemonic(KeyEvent.VK_D);

				// --------------------------------------------------

				JMenuItem generateItem = new JMenuItem("Reset Preferences        ");
				menu.add(generateItem);

				generateItem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						int confirmation = JOptionPane.showConfirmDialog(null,
								"        Are you sure you want to reset all saved settings?        ",
								"Reset Preferences? ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (confirmation == JOptionPane.YES_OPTION) {
							Preferences.reset();
						}
					}

				});

				// --------------------------------------------------

				menuBar.add(menu);
			}
		}

		jframe.setLayout(new BorderLayout());

		{
			JPanel toolbar = new JPanel();
			toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
			toolbar.setPreferredSize(new Dimension(1, 30));
			toolbar.setBackground(theme.p2);
			toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, theme.l1));
			jframe.getContentPane().add(toolbar, BorderLayout.NORTH);

			toolbar.add(new ToolbarSeparator());

			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "project_new.png").getScaledInstance(16,
						16, java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("New Project");

				toolbar.add(button);
				
			}

			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "save.png").getScaledInstance(16, 16,
						java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("Save File");

				toolbar.add(button);

			}

			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "save_all.png").getScaledInstance(16, 16,
						java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("Save All Files");

				toolbar.add(button);

			}

			toolbar.add(new ToolbarSeparator());

			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "undo.png").getScaledInstance(16, 16,
						java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("Undo");

				toolbar.add(button);

			}

			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "redo.png").getScaledInstance(16, 16,
						java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("Redo");

				toolbar.add(button);

			}

			toolbar.add(new ToolbarSeparator());

			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "entity_new.png").getScaledInstance(16, 16,
						java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("New Entity");

				toolbar.add(button);

			}
			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "item_new.png").getScaledInstance(16, 16,
						java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("New Item");

				toolbar.add(button);

			}

			toolbar.add(new ToolbarSeparator());

			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "export.png").getScaledInstance(16, 16,
						java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("Generate Structure");
				button.addActionListener(new AbstractAction() {
					/**
					 * 
					 */
					private static final long serialVersionUID = -6494932842671220561L;

					@Override
					public void actionPerformed(ActionEvent arg0) {
						Compiler.compile();
					}
				});
				toolbar.add(button);
			}

		}

		{
			JPanel sidebar = new JPanel();
			sidebar.setLayout(new BorderLayout());
			sidebar.setPreferredSize(new Dimension(350, 500));
			sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, theme.l1));
			sidebar.setBackground(theme.p1);
			jframe.getContentPane().add(sidebar, BorderLayout.WEST);

			JPanel header = new JPanel(new BorderLayout());
			header.setBackground(theme.p1);

			JLabel label = new JLabel("    Project Explorer");
			label.setForeground(theme.t1);
			label.setFont(new Font("Tahoma", Font.PLAIN, 14));
			label.setPreferredSize(new Dimension(500, 25));
			header.add(label, BorderLayout.WEST);

			ToolbarButton refresh = new ToolbarButton();
			refresh.setIcon(new ImageIcon(ImageManager.load("/assets/icons/" + Window.theme.path + "reload.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
			refresh.setToolTipText("Refresh Explorer");

			refresh.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Window.explorer.generateProjectList();
				}

			});

			header.add(refresh, BorderLayout.EAST);
			sidebar.add(header, BorderLayout.NORTH);

			JScrollPane sp = new JScrollPane();
			sp.getViewport().setBackground(Color.BLACK);

			sp.getViewport().add(explorer = new Explorer());
			sp.setBackground(theme.p1);
			sp.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, theme.p1));

			sidebar.add(sp, BorderLayout.CENTER);
		}

		edit_area = new JPanel(new BorderLayout());
		edit_area.setPreferredSize(new Dimension(500, 500));
		edit_area.setBackground(theme.p3);
		jframe.getContentPane().add(edit_area, BorderLayout.CENTER);

		JPanel tabListHolder = new JPanel(new BorderLayout());
		tabListHolder.setPreferredSize(new Dimension(1,30));
		tabListHolder.setBackground(theme.p4);
		
		JPanel tabActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
		tabActionPanel.setOpaque(false);
		tabActionPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, theme.l1));
		
		{
			ToolbarButton more = new ToolbarButton();
			more.setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/more.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
			more.setToolTipText("View all tabs");
			more.setPreferredSize(new Dimension(25,25));
			tabActionPanel.add(more);
			
			more.addActionListener(new AbstractAction() {

				/**
				 * 
				 */
				private static final long serialVersionUID = -6051046434889434022L;

				@Override
				public void actionPerformed(ActionEvent e) {
					TabManager.getMenu().show(more, more.getX(), more.getY() + more.getHeight());
				}
				
			});
			
			
		}
		
		tabListHolder.add(tabActionPanel, BorderLayout.EAST);
		
		edit_area.add(tabListHolder, BorderLayout.NORTH);
		tabList = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		tabList.setPreferredSize(new Dimension(1, 30));
		tabList.setBackground(theme.p4);
		tabList.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, theme.l1));
		tabListHolder.add(tabList, BorderLayout.CENTER);

		if (useConsole) {
			JPanel consoleArea = new JPanel(new BorderLayout());
			consoleArea.setPreferredSize(new Dimension(0, CONSOLE_HEIGHT));
			consoleArea.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, theme.l1));

			JPanel consoleHeader = new JPanel(new BorderLayout());
			consoleHeader.setBackground(theme.p2);
			consoleHeader.setPreferredSize(new Dimension(0, 25));
			consoleHeader.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

			JLabel consoleLabel = new JLabel("Java Console");
			consoleLabel.setForeground(theme.t1);
			consoleLabel.setFont(new Font("Tahoma", 0, 12));
			// consoleLabel.setPreferredSize(new Dimension(0,20));
			consoleHeader.add(consoleLabel, BorderLayout.WEST);
			
			
			
			JPanel consoleActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
			consoleActionPanel.setOpaque(false);

			ToolbarButton toggle = new ToolbarButton(true);
			toggle.setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/toggle.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
			toggle.setToolTipText("Toggle Java Console");
			toggle.setPreferredSize(new Dimension(20,20));

			toggle.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (consoleArea.getPreferredSize().height == 25) {
						consoleArea.setPreferredSize(new Dimension(0, CONSOLE_HEIGHT));
					} else {
						consoleArea.setPreferredSize(new Dimension(0, 25));
					}
					consoleArea.revalidate();
					consoleArea.repaint();
				}

			});


			ToolbarButton clear = new ToolbarButton(true);
			clear.setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/clear.png").getScaledInstance(16, 16,
					java.awt.Image.SCALE_SMOOTH)));
			clear.setToolTipText("Clear Console");
			clear.setPreferredSize(new Dimension(20,20));
			
			clear.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					textConsoleOut.clear();
				}

			});

			consoleActionPanel.add(clear);
			consoleActionPanel.add(toggle);
			consoleHeader.add(consoleActionPanel, BorderLayout.EAST);

			consoleArea.add(consoleHeader, BorderLayout.NORTH);

			console = new JEditorPane();
			console.setBackground(theme.p1);
			console.setEditable(false);
			console.setFont(new Font("monospaced", 0, 12));
			console.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

			console.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));

			console.addHyperlinkListener(new HyperlinkListener() {
				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						String path = e.getURL().toString().substring(7, e.getURL().toString().lastIndexOf('?'));
						String location = e.getURL().toString().substring(e.getURL().toString().lastIndexOf('?') + 1);
						TabManager.openTab(path, Integer.parseInt(location.split(":")[0]),
								Integer.parseInt(location.split(":")[1]));
					}
				}
			});

			textConsoleOut = new TextAreaOutputStream(console);
			consoleOut = new PrintStream(textConsoleOut);
			System.setOut(new PrintStream(new MultiOutputStream(consoleOut, System.out)));
			System.setErr(new PrintStream(new MultiOutputStream(consoleOut, System.err)));

			JScrollPane consoleScrollPane = new JScrollPane(console);
			consoleScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, theme.l1));

			consoleArea.add(consoleScrollPane, BorderLayout.CENTER);

			edit_area.add(consoleArea, BorderLayout.SOUTH);
		}

		jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jframe.setSize(defaultSize);
		jframe.setPreferredSize(defaultSize);
		jframe.setVisible(true);

		List<Image> icons = new ArrayList<Image>();
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
}
