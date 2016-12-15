package com.energyxxer.cbe.ui.dialogs;

import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.ui.components.ComponentResizer;
import com.energyxxer.cbe.ui.components.XDropdownMenu;
import com.energyxxer.cbe.ui.styledcomponents.*;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.ThemeManager;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.ImageManager;

import javax.swing.*;
import java.awt.*;

public class Settings {

	private static Theme t;

	public static void show() {

		JDialog dialog = new JDialog(Window.jframe);

		//Ready resources
		ThemeManager.loadAll();

		ThemeChangeListener.addThemeChangeListener(th -> t = th);

		XDropdownMenu<Theme> themeDropdown;

		JPanel pane = new JPanel(new BorderLayout());
		//JButton okay = new JButton("OK");
		//JButton cancel = new JButton("Cancel");

		pane.setPreferredSize(new Dimension(900,600));
		pane.setBackground(t.getColor("Settings.background", new Color(235, 235, 235)));

		{
			JPanel sidebar = new JPanel(new BorderLayout());

			ComponentResizer sidebarResizer = new ComponentResizer(sidebar);
			sidebarResizer.setResizable(false, false, false, true);

			String[] sections = new String[] { "Appearance", "Editor", "Resources", "In-game Compiler" };

			StyledList<String> navigator = new StyledList<>(sections, "Settings");
			sidebar.setBackground(navigator.getBackground());
			sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, t.getColor("Settings.content.border",new Color(200, 200, 200))));
			navigator.setPreferredSize(new Dimension(200,500));

			sidebar.add(navigator, BorderLayout.CENTER);

			pane.add(sidebar, BorderLayout.WEST);
		}

		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBackground(t.getColor("Settings.content.background", new Color(235, 235, 235)));
		pane.add(contentPane, BorderLayout.CENTER);

		JPanel contentAppearance = new JPanel(new BorderLayout());
		{
			contentAppearance.setBackground(contentPane.getBackground());
			contentPane.add(contentAppearance, BorderLayout.CENTER);

			{
				JPanel header = new JPanel(new BorderLayout());
				header.setBackground(t.getColor("Settings.content.header.background", new Color(235, 235, 235)));
				header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, t.getColor("Settings.content.header.border", new Color(200, 200, 200))));
				header.setPreferredSize(new Dimension(0,40));
				contentAppearance.add(header, BorderLayout.NORTH);

				{
					JPanel padding = new JPanel();
					padding.setOpaque(false);
					padding.setPreferredSize(new Dimension(25,25));
					header.add(padding, BorderLayout.WEST);
				}

				JLabel label = new JLabel("Appearance");
				label.setForeground(t.getColor("Settings.content.header.foreground", Color.BLACK));
				label.setFont(new Font(t.getString("Settings.content.header.font",t.getString("General.font","Tahoma")),1,20));
				header.add(label, BorderLayout.CENTER);
			}

			{
				JPanel padding_left = new JPanel();
				padding_left.setOpaque(false);
				padding_left.setPreferredSize(new Dimension(50,25));
				contentAppearance.add(padding_left, BorderLayout.WEST);
			}
			{
				JPanel padding_right = new JPanel();
				padding_right.setOpaque(false);
				padding_right.setPreferredSize(new Dimension(50,25));
				contentAppearance.add(padding_right, BorderLayout.EAST);
			}

			{

				JPanel content = new JPanel();
				content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
				content.setOpaque(false);
				contentAppearance.add(content, BorderLayout.CENTER);

				{
					JPanel padding = new JPanel();
					padding.setOpaque(false);
					padding.setMinimumSize(new Dimension(1,20));
					padding.setMaximumSize(new Dimension(1,20));
					content.add(padding);
				}

				{
					JLabel label = new StyledLabel("Color Scheme:","Settings.content");
					content.add(label);
				}
				{
					themeDropdown = new XDropdownMenu<>(ThemeManager.getThemesAsArray());
					themeDropdown.setPopupFactory(StyledPopupMenu::new);
					themeDropdown.setPopupItemFactory(StyledMenuItem::new);
					System.out.println(Window.getTheme());
					themeDropdown.setValue(Window.getTheme());
					content.add(themeDropdown);
				}

			}

		}

		{
			JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
			buttons.setPreferredSize(new Dimension(0,50));
			buttons.setBackground(contentPane.getBackground());

			{
				JPanel padding = new JPanel();
				padding.setOpaque(false);
				padding.setPreferredSize(new Dimension(25,25));
				buttons.add(padding);
			}

			{
				StyledButton okay = new StyledButton("OK", "Settings");
				okay.setPreferredSize(new Dimension(75, 25));
				buttons.add(okay);

				okay.addActionListener(e -> {
					dialog.setVisible(false);
					dialog.dispose();
					ThemeManager.setTheme(themeDropdown.getValue().getName());
				});
			}

			{
				StyledButton cancel = new StyledButton("Cancel", "Settings");
				cancel.setPreferredSize(new Dimension(75, 25));
				buttons.add(cancel);

				cancel.addActionListener(e -> {
					dialog.setVisible(false);
					dialog.dispose();
				});
			}

			contentPane.add(buttons, BorderLayout.SOUTH);
		}

		dialog.setVisible(true);
		dialog.setContentPane(pane);
		dialog.pack();
		//dialog.setResizable(false);

		dialog.setTitle("Settings");
		dialog.setIconImage(ImageManager.load("/assets/logo/logo_icon.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		center.x -= dialog.getWidth()/2;
		center.y -= dialog.getHeight()/2;

		dialog.setLocation(center);
	}
}
