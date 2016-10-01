package com.energyxxer.cbe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.energyxxer.cbe.parsing.CBEParser;
import com.energyxxer.ui.TextAreaOutputStream;
import com.energyxxer.ui.ToolbarButton;
import com.energyxxer.ui.ToolbarSeparator;
import com.energyxxer.ui.common.MenuItems;
import com.energyxxer.ui.explorer.Explorer;
import com.energyxxer.util.ImageManager;

/**
 * Literally what it sounds like.
 * */
public class Window {
	
	public static final boolean useConsole = true;

	public static JFrame jframe;
	
	public static JMenuBar menuBar;
	
	public static Explorer explorer;
	public static JPanel tabList;

	public static JPanel edit_area;
	
	public static Dimension defaultSize = new Dimension(1200,800);

	public static Color defaultColor = new Color(215,215,215);
	public static Color toolbarColor = new Color(235,235,235);
	
	public static OutputStream consoleout = System.out;
	public static final int CONSOLE_HEIGHT = 200;
	
	public Window() {
		jframe = new JFrame("Command Block Engine");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jframe.getContentPane().setBackground(defaultColor);
		
		
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace(new PrintStream(consoleout));
		}
		
		menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);

        menuBar.setPreferredSize(new Dimension(0, 20));
		jframe.setJMenuBar(menuBar);
		
		if(true) {
			{
				JMenu menu = new JMenu(" File ");
				menu.setMnemonic(KeyEvent.VK_F);
				
				//--------------------------------------------------
				
				JMenu newMenu = MenuItems.newMenu("New                    ");
				menu.add(newMenu);
				
				//--------------------------------------------------
				
				menu.addSeparator();
	
				//--------------------------------------------------
	
				JMenuItem saveItem = new JMenuItem("Save", new ImageIcon(ImageManager.load("/assets/icons/save.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(saveItem);
				saveItem.setAccelerator(KeyStroke.getKeyStroke(
				        KeyEvent.VK_S, ActionEvent.CTRL_MASK));
				
				//--------------------------------------------------
	
				JMenuItem saveAsItem = new JMenuItem("Save As", new ImageIcon(ImageManager.load("/assets/icons/save_as.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(saveAsItem);
				saveAsItem.setAccelerator(KeyStroke.getKeyStroke(
				        KeyEvent.VK_S, 3));
	
				//--------------------------------------------------
	
				JMenuItem saveAllItem = new JMenuItem("Save All", new ImageIcon(ImageManager.load("/assets/icons/save_all.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(saveAllItem);
				saveAllItem.setAccelerator(KeyStroke.getKeyStroke(
				        KeyEvent.VK_S, 10));
	
				//--------------------------------------------------
					
				menu.addSeparator();
	
				//--------------------------------------------------
	
				JMenuItem closeItem = new JMenuItem("Close");
				menu.add(closeItem);
				closeItem.setAccelerator(KeyStroke.getKeyStroke(
				        KeyEvent.VK_W, 2));
	
				//--------------------------------------------------
	
				JMenuItem closeAllItem = new JMenuItem("Close All");
				menu.add(closeAllItem);
				closeAllItem.setAccelerator(KeyStroke.getKeyStroke(
				        KeyEvent.VK_W, 3));
				
				//--------------------------------------------------
				
				menu.addSeparator();
	
				//--------------------------------------------------
	
				JMenuItem moveItem = new JMenuItem("Move");
				menu.add(moveItem);
	
				//--------------------------------------------------
	
				JMenuItem renameItem = new JMenuItem("Rename", new ImageIcon(ImageManager.load("/assets/icons/rename.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(renameItem);
	
				//--------------------------------------------------
	
				JMenuItem refreshItem = new JMenuItem("Refresh", new ImageIcon(ImageManager.load("/assets/icons/reload.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				refreshItem.addActionListener(new ActionListener() {
	
					@Override
					public void actionPerformed(ActionEvent e) {
						Window.explorer.generateProjectList();
					} 
					
				});
				menu.add(refreshItem);
				
				//--------------------------------------------------
				
				menu.addSeparator();
	
				//--------------------------------------------------
	
				JMenuItem exitItem = new JMenuItem("Exit");
				exitItem.addActionListener(new ActionListener() {
	
					@Override
					public void actionPerformed(ActionEvent e) {
						jframe.dispatchEvent(new WindowEvent(jframe, WindowEvent.WINDOW_CLOSING));
					} 
					
				});
				menu.add(exitItem);
				
				
	
				//--------------------------------------------------
				
				menuBar.add(menu);
			}
			
			{
				JMenu menu = new JMenu(" Edit ");
				menu.setMnemonic(KeyEvent.VK_E);
				
				//--------------------------------------------------
	
				JMenuItem undoItem = new JMenuItem("Undo                    ",new ImageIcon(ImageManager.load("/assets/icons/undo.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(undoItem);
				undoItem.setAccelerator(KeyStroke.getKeyStroke(
				        KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
				
				
				//--------------------------------------------------
	
				JMenuItem redoItem = new JMenuItem("Redo",new ImageIcon(ImageManager.load("/assets/icons/redo.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				menu.add(redoItem);
				redoItem.setAccelerator(KeyStroke.getKeyStroke(
				        KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
				
				//--------------------------------------------------
				
				menu.addSeparator();
	
				//--------------------------------------------------
	
				JMenuItem copyItem = new JMenuItem("Copy");
				menu.add(copyItem);
				copyItem.setAccelerator(KeyStroke.getKeyStroke(
				        KeyEvent.VK_C, ActionEvent.CTRL_MASK));
				
				//--------------------------------------------------
	
				JMenuItem cutItem = new JMenuItem("Cut");
				menu.add(cutItem);
				cutItem.setAccelerator(KeyStroke.getKeyStroke(
				        KeyEvent.VK_X, ActionEvent.CTRL_MASK));
				
				//--------------------------------------------------
	
				JMenuItem pasteItem = new JMenuItem("Paste");
				menu.add(pasteItem);
				pasteItem.setAccelerator(KeyStroke.getKeyStroke(
				        KeyEvent.VK_V, ActionEvent.CTRL_MASK));
				
				//--------------------------------------------------
				
				menu.addSeparator();
	
				//--------------------------------------------------
	
				JMenuItem deleteItem = new JMenuItem("Delete");
				menu.add(deleteItem);
				deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
	
				//--------------------------------------------------
				
				menuBar.add(menu);
			}
			
			{
				JMenu menu = new JMenu(" Project ");
				menu.setMnemonic(KeyEvent.VK_P);
				
				//--------------------------------------------------
	
				JMenuItem generateItem = new JMenuItem("Generate                    ");
				menu.add(generateItem);
				
				//--------------------------------------------------
				
				menu.addSeparator();
	
				//--------------------------------------------------
	
				JMenuItem propertiesItem = new JMenuItem("Properties");
				menu.add(propertiesItem);
	
				//--------------------------------------------------
				
				menuBar.add(menu);
			}
		}
		
		jframe.setLayout(new BorderLayout());
		
		{
			JPanel toolbar = new JPanel();
			toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
			toolbar.setPreferredSize(new Dimension(1,30));
			toolbar.setBackground(toolbarColor);
			toolbar.setBorder(BorderFactory.createMatteBorder(0,0,1,0,new Color(160,160,160)));
			jframe.getContentPane().add(toolbar,BorderLayout.NORTH);
			
			toolbar.add(new ToolbarSeparator());
			
			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/project_new.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("New Project");
				
				toolbar.add(button);
				
			}
			
			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/save.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("Save File");
				
				toolbar.add(button);
				
			}
			
			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/save_all.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("Save All Files");
				
				toolbar.add(button);
				
			}

			toolbar.add(new ToolbarSeparator());
			
			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/undo.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("Undo");
				
				toolbar.add(button);
				
			}
			
			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/redo.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("Redo");
				
				toolbar.add(button);
				
			}

			toolbar.add(new ToolbarSeparator());
			
			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/entity_new.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("New Entity");
				
				toolbar.add(button);
				
			}
			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/item_new.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("New Item");
				
				toolbar.add(button);
				
			}

			toolbar.add(new ToolbarSeparator());
			
			{
				ToolbarButton button = new ToolbarButton();
				button.setIcon(new ImageIcon(ImageManager.load("/assets/icons/export.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
				button.setToolTipText("Generate Structure");
				button.addActionListener(new AbstractAction() {
					/**
					 * 
					 */
					private static final long serialVersionUID = -6494932842671220561L;

					public void actionPerformed(ActionEvent arg0) {
						if(Explorer.selectedLabel == null) return;
						CBEParser.parse(new File(Explorer.selectedLabel.parent.path));
					}
				});
				toolbar.add(button);
				
			}
			
		}
		
		{
			JPanel sidebar = new JPanel();
			sidebar.setLayout(new BorderLayout());
			sidebar.setPreferredSize(new Dimension(350,500));
			sidebar.setBorder(BorderFactory.createMatteBorder(0,0,0,1,new Color(160,160,160)));
			sidebar.setBackground(Color.WHITE);
			jframe.getContentPane().add(sidebar,BorderLayout.WEST);
			
			
			
			JPanel header = new JPanel(new BorderLayout());
			header.setBackground(Color.WHITE);
			
			JLabel label = new JLabel("    Project Explorer");
			label.setFont(new Font("Tahoma",Font.PLAIN,14));
			label.setPreferredSize(new Dimension(500,25));
			header.add(label,BorderLayout.WEST);
			
			ToolbarButton refresh = new ToolbarButton();
			refresh.setIcon(new ImageIcon(ImageManager.load("/assets/icons/reload.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			refresh.setToolTipText("Refresh Explorer");
			
			refresh.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Window.explorer.generateProjectList();
				} 
				
			});
			
			header.add(refresh,BorderLayout.EAST);
			sidebar.add(header, BorderLayout.NORTH);
			
			JScrollPane sp = new JScrollPane();
			sp.getViewport().setBackground(Color.BLACK);
			
			sp.getViewport().add(explorer = new Explorer());
			sp.setBackground(Color.WHITE);
			sp.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));

			sidebar.add(sp,BorderLayout.CENTER);
		}
		
		edit_area = new JPanel(new BorderLayout());
		edit_area.setPreferredSize(new Dimension(500,500));
		edit_area.setBackground(defaultColor);
		jframe.getContentPane().add(edit_area,BorderLayout.CENTER);
		
		tabList = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		tabList.setPreferredSize(new Dimension(1,30));
		tabList.setBackground(new Color(200,202,205));
		tabList.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(150,150,150)));
		edit_area.add(tabList, BorderLayout.NORTH);
		
		if(useConsole) {
			JPanel consoleArea = new JPanel(new BorderLayout());
			consoleArea.setPreferredSize(new Dimension(0,CONSOLE_HEIGHT));
			consoleArea.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(150,150,150)));
			
			JPanel consoleHeader = new JPanel(new BorderLayout());
			consoleHeader.setPreferredSize(new Dimension(0,25));
			consoleHeader.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
			
			JLabel consoleLabel = new JLabel("Java Console");
			consoleLabel.setFont(new Font("Tahoma",0,12));
			//consoleLabel.setPreferredSize(new Dimension(0,20));
			consoleHeader.add(consoleLabel,BorderLayout.WEST);
			
			ToolbarButton toggle = new ToolbarButton();
			toggle.setIcon(new ImageIcon(ImageManager.load("/assets/icons/ui/toggle.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH)));
			toggle.setToolTipText("Toggle Java Console");
			
			toggle.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if(consoleArea.getPreferredSize().height == 25) {
							consoleArea.setPreferredSize(new Dimension(0,CONSOLE_HEIGHT));
						} else {
							consoleArea.setPreferredSize(new Dimension(0,25));
						}
						consoleArea.revalidate();
						consoleArea.repaint();
					} catch(Exception err) {
						err.printStackTrace(new PrintStream(consoleout));
					}
				} 
				
			});
			
			consoleHeader.add(toggle,BorderLayout.EAST);
			
			consoleArea.add(consoleHeader, BorderLayout.NORTH);
			
			JEditorPane console = new JEditorPane();
			console.setEditable(false);
			console.setFont(new Font("monospaced",0,12));
			console.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			
			consoleout = new TextAreaOutputStream(console);
			System.setOut(new PrintStream(consoleout));
			
			JScrollPane consoleScrollPane = new JScrollPane(console);
			consoleScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(150,150,150)));
			
			consoleArea.add(consoleScrollPane,BorderLayout.CENTER);
			
			edit_area.add(consoleArea, BorderLayout.SOUTH);
		}

		jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jframe.setSize(defaultSize);
		jframe.setPreferredSize(defaultSize);
		jframe.setVisible(true);
		
		List<Image> icons = new ArrayList<Image>();
		icons.add(ImageManager.load("/assets/logo/logo_icon.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
		icons.add(ImageManager.load("/assets/logo/logo.png").getScaledInstance(32, 32, java.awt.Image.SCALE_SMOOTH));
		jframe.setIconImages(icons);
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Point center = env.getCenterPoint();
		center.x -= jframe.getWidth()/2;
		center.y -= jframe.getHeight()/2;
		jframe.setLocation(center);
		
		
	}
}
