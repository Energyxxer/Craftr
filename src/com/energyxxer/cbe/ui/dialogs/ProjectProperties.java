package com.energyxxer.cbe.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.energyxxer.cbe.logic.Project;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.ui.components.ComponentResizer;
import com.energyxxer.cbe.ui.components.XButton;
import com.energyxxer.cbe.ui.components.XFileForm;
import com.energyxxer.cbe.ui.components.XList;
import com.energyxxer.cbe.ui.components.XTextForm;
import com.energyxxer.cbe.util.ImageManager;

public class ProjectProperties {
	
	//private static final Font FIELD_FONT = new Font("Consolas", 0, 12);

	public static void show(Project project) {
		
		XTextForm cPrefix; 
		XFileForm cWorld;
		
		JPanel pane = new JPanel(new BorderLayout());
		//JButton okay = new JButton("OK");
		//JButton cancel = new JButton("Cancel");
		
		pane.setPreferredSize(new Dimension(900,600));
		pane.setBackground(Window.theme.p2);
		
		{
			JPanel sidebar = new JPanel(new BorderLayout());
			sidebar.setBackground(Window.theme.p1);
			sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Window.theme.l1));
			
			ComponentResizer sidebarResizer = new ComponentResizer(sidebar);
			sidebarResizer.setResizable(false, false, false, true);
			
			String[] sections = new String[] { "General", "Compiler", "Editor", "Code Style", "Resources" };
			
			XList<String> navigator = new XList<String>(sections);
			navigator.setPreferredSize(new Dimension(200,500));
			navigator.setBackground(Window.theme.p1);
			navigator.setForeground(Window.theme.t1);
			navigator.setFont(navigator.getFont().deriveFont(14.0f));
			
			navigator.setCellBackground(Window.theme.p3);
			
			navigator.setSelectedCellBackground(Window.theme.p2);
			navigator.setSelectedCellBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Window.theme.l1));
			
			navigator.setRolloverCellBackground(Window.theme.p2);
			navigator.setRolloverCellBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Window.theme.l1));
			
			sidebar.add(navigator, BorderLayout.CENTER);
			
			pane.add(sidebar, BorderLayout.WEST);
		}
		
		JPanel contentPane = new JPanel(new BorderLayout());
		pane.add(contentPane, BorderLayout.CENTER);
		
		JPanel contentCompiler = new JPanel(new BorderLayout());
		{
			contentCompiler.setBackground(Window.theme.p2);
			contentPane.add(contentCompiler, BorderLayout.CENTER);
			
			{
				JPanel header = new JPanel(new BorderLayout());
				header.setBackground(Window.theme.p2);
				header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Window.theme.l1));
				header.setPreferredSize(new Dimension(0,40));
				contentCompiler.add(header, BorderLayout.NORTH);
				
				{
					JPanel padding = new JPanel();
					padding.setOpaque(false);
					padding.setPreferredSize(new Dimension(25,25));
					header.add(padding, BorderLayout.WEST);
				}
				
				JLabel label = new JLabel("General");
				label.setForeground(Window.theme.t1);
				label.setFont(label.getFont().deriveFont(20f).deriveFont(Font.BOLD));
				header.add(label, BorderLayout.CENTER);
			}
			
			{
				JPanel padding_left = new JPanel();
				padding_left.setOpaque(false);
				padding_left.setPreferredSize(new Dimension(50,25));
				contentCompiler.add(padding_left, BorderLayout.WEST);
			}
			{
				JPanel padding_right = new JPanel();
				padding_right.setOpaque(false);
				padding_right.setPreferredSize(new Dimension(50,25));
				contentCompiler.add(padding_right, BorderLayout.EAST);
			}
			
			{
				
				JPanel content = new JPanel();
				content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
				content.setOpaque(false);
				contentCompiler.add(content, BorderLayout.CENTER);
				
				{
					JPanel padding = new JPanel();
					padding.setOpaque(false);
					padding.setMinimumSize(new Dimension(1,20));
					padding.setMaximumSize(new Dimension(1,20));
					content.add(padding);
				}
				
				{
					cPrefix = new XTextForm("Prefix", project.getPrefix(), 75);
					cPrefix.setForeground(Window.theme.t1);
					cPrefix.setMaximumSize(new Dimension(150,25));
					cPrefix.setAlignmentX(Component.LEFT_ALIGNMENT);
	
					cPrefix.field.setBackground(Window.theme.b3);
					cPrefix.field.setBorderColor(Window.theme.l1);
					cPrefix.field.setForeground(Window.theme.t1);
					
					content.add(cPrefix);
				}
				
				{
					cWorld = new XFileForm("World Output", new File(project.getWorld()), 75);
					cWorld.setForeground(Window.theme.t1);
					cWorld.setMaximumSize(new Dimension(cWorld.getMaximumSize().width,25));
					cWorld.setAlignmentX(Component.LEFT_ALIGNMENT);
	
					cWorld.field.setBackground(Window.theme.b3);
					cWorld.field.setBorderColor(Window.theme.l1);
					cWorld.field.setForeground(Window.theme.t1);
					
					content.add(cWorld);
				}
				
			}
			
		}
		
		{
			JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
			buttons.setPreferredSize(new Dimension(0,50));
			buttons.setBackground(Window.theme.p2);
			
			{
				JPanel padding = new JPanel();
				padding.setOpaque(false);
				padding.setPreferredSize(new Dimension(25,25));
				buttons.add(padding);
			}
			
			{
				XButton okay = new XButton("OK");
				okay.setPreferredSize(new Dimension(75, 25));
				okay.setBackground(Window.theme.p3);
				okay.setForeground(Window.theme.t1);
				okay.setBorderColor(Window.theme.l1);
				okay.setRolloverColor(Window.theme.p4);
				okay.setPressedColor(Window.theme.p1);
				buttons.add(okay);
			}
			
			{
				XButton cancel = new XButton("Cancel");
				cancel.setPreferredSize(new Dimension(75, 25));
				cancel.setBackground(Window.theme.p3);
				cancel.setForeground(Window.theme.t1);
				cancel.setBorderColor(Window.theme.l1);
				cancel.setRolloverColor(Window.theme.p4);
				cancel.setPressedColor(Window.theme.p1);
				buttons.add(cancel);
			}
			
			contentPane.add(buttons, BorderLayout.SOUTH);
		}
		
		
		/*okay.setFocusPainted(false);
		okay.setPreferredSize(new Dimension(75, 25));
		okay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//optionPane.setValue(okay);
			}
		});
		cancel.setFocusPainted(false);
		cancel.setPreferredSize(new Dimension(75, 25));
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//optionPane.setValue(cancel);
			}
		});*/
		
		JDialog dialog = new JDialog(Window.jframe);
		dialog.setVisible(true);
		dialog.setContentPane(pane);
		dialog.pack();
		//dialog.setResizable(false);
		
		dialog.setTitle("Editing properties for project \"" + project.getName() + "\"");
		dialog.setIconImage(ImageManager.load("/assets/logo/logo_icon.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
		
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		center.x -= dialog.getWidth()/2;
		center.y -= dialog.getHeight()/2;
		
		dialog.setLocation(center);
		
		//JOptionPane.showOptionDialog(Window.jframe, optionPane, "Editing properties for project \"" + project.getName() + "\"", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[] { okay, cancel }, null);
	}
}
