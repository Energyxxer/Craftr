package com.energyxxer.cbe.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.File;
import java.util.Base64;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.energyxxer.cbe.logic.Project;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.minecraft.MinecraftConstants;
import com.energyxxer.cbe.ui.components.*;
import com.energyxxer.cbe.util.ImageManager;
import com.energyxxer.cbe.util.StringUtil;

public class ProjectProperties {
	
	//private static final Font FIELD_FONT = new Font("Consolas", 0, 12);

	public static void show(Project project) {

		JDialog dialog = new JDialog(Window.jframe);
		
		XTextField cPrefix;
		XFileField cWorld;
		
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
				
				JLabel label = new JLabel("Compiler");
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
					JLabel label = new JLabel("Prefix:");
					label.setForeground(Window.theme.t1);
					label.setFont(label.getFont().deriveFont(1));
					content.add(label);
				}
				{
					JPanel prefixFields = new JPanel();
					prefixFields.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
					prefixFields.setOpaque(false);
					prefixFields.setAlignmentX(Component.LEFT_ALIGNMENT);
					{
						cPrefix = new XTextField(project.getPrefix());
						cPrefix.setForeground(Window.theme.t1);
						cPrefix.setPreferredSize(new Dimension(150,25));
						cPrefix.setAlignmentX(Component.LEFT_ALIGNMENT);

						cPrefix.setBackground(Window.theme.b3);
						cPrefix.setBorderColor(Window.theme.l1);
						cPrefix.setForeground(Window.theme.t1);

						prefixFields.add(cPrefix);
					}
					{
						XButton randomize = new XButton("Generate Random Prefix");
						randomize.setPreferredSize(new Dimension(150, 25));
						randomize.setBackground(Window.theme.p3);
						randomize.setForeground(Window.theme.t1);
						randomize.setBorderColor(Window.theme.l1);
						randomize.setRolloverColor(Window.theme.p4);
						randomize.setPressedColor(Window.theme.p1);

						randomize.addActionListener(e -> cPrefix.setText(StringUtil.getRandomString(4)));

						prefixFields.add(randomize);
					}
					{
						XButton reset = new XButton("Reset Prefix");
						reset.setPreferredSize(new Dimension(100, 25));
						reset.setBackground(Window.theme.p3);
						reset.setForeground(Window.theme.t1);
						reset.setBorderColor(Window.theme.l1);
						reset.setRolloverColor(Window.theme.p4);
						reset.setPressedColor(Window.theme.p1);

						reset.addActionListener(e -> cPrefix.setText(StringUtil.getInitials(project.getName()).toLowerCase()));

						prefixFields.add(reset);
					}

					prefixFields.setMaximumSize(new Dimension(prefixFields.getMaximumSize().width, 30));

					content.add(prefixFields);
				}

				{
					JPanel margin = new JPanel();
					margin.setMinimumSize(new Dimension(200,15));
					margin.setMaximumSize(new Dimension(200,15));
					margin.setOpaque(false);
					margin.setAlignmentX(Component.LEFT_ALIGNMENT);

					content.add(margin);
				}
				
				{
					{
						JLabel label = new JLabel("World Output:");
						label.setForeground(Window.theme.t1);
						label.setFont(label.getFont().deriveFont(1));
						content.add(label);
					}
					File file = new File(MinecraftConstants.getMinecraftDir() + File.separator + "saves");
					if(project.getWorld() != null) file = new File(project.getWorld());
					cWorld = new XFileField(file);
					cWorld.setDialogTitle("Open world...");
					cWorld.setOperation(XFileField.OPEN_DIRECTORY);
					cWorld.setForeground(Window.theme.t1);
					cWorld.setMaximumSize(new Dimension(cWorld.getMaximumSize().width,25));
					cWorld.setAlignmentX(Component.LEFT_ALIGNMENT);
	
					cWorld.setBackground(Window.theme.b3);
					cWorld.setBorderColor(Window.theme.l1);
					cWorld.setForeground(Window.theme.t1);
					
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

				okay.addActionListener(e -> {
					project.setPrefix(cPrefix.getText());
					project.setWorld(cWorld.getFile().getAbsolutePath());
					project.updateConfig();

					dialog.setVisible(false);
					dialog.dispose();
				});
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

				cancel.addActionListener(e -> {
					dialog.setVisible(false);
					dialog.dispose();
				});
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
