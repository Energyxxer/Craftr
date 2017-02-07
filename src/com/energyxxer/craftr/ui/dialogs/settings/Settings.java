package com.energyxxer.craftr.ui.dialogs.settings;

import com.energyxxer.craftr.main.window.Window;
import com.energyxxer.craftr.ui.components.ComponentResizer;
import com.energyxxer.craftr.ui.styledcomponents.StyledButton;
import com.energyxxer.craftr.ui.styledcomponents.StyledList;
import com.energyxxer.craftr.ui.theme.change.ThemeChangeListener;
import com.energyxxer.craftr.util.ImageManager;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class Settings {

	private static JDialog dialog = new JDialog(Window.jframe);
	//static Theme t;

	private static ArrayList<Runnable> openEvents = new ArrayList<>();
	private static ArrayList<Runnable> applyEvents = new ArrayList<>();

	private static JPanel currentSection;

	static {
		//ThemeChangeListener.addThemeChangeListener(th -> t = th);

		JPanel pane = new JPanel(new BorderLayout());
		pane.setPreferredSize(new Dimension(900,600));
		ThemeChangeListener.addThemeChangeListener(t ->
				pane.setBackground(t.getColor("Settings.background", new Color(235, 235, 235)))
		);

		JPanel contentPane = new JPanel(new BorderLayout());
		HashMap<String, JPanel> sectionPanes = new HashMap<>();

		{
			JPanel sidebar = new JPanel(new BorderLayout());

			ComponentResizer sidebarResizer = new ComponentResizer(sidebar);
			sidebarResizer.setResizable(false, false, false, true);

			String[] sections = new String[] { "General", "Appearance", "Editor", "Resources", "In-game Compiler" };

			StyledList<String> navigator = new StyledList<>(sections, "Settings");
			sidebar.setBackground(navigator.getBackground());
			ThemeChangeListener.addThemeChangeListener(t ->
					sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, t.getColor("Settings.content.border",new Color(200, 200, 200))))
			);
			navigator.setPreferredSize(new Dimension(200,500));

			navigator.addListSelectionListener(o -> {
				contentPane.remove(currentSection);
				currentSection = sectionPanes.get(sections[o.getFirstIndex()]);
				contentPane.add(currentSection, BorderLayout.CENTER);
				contentPane.repaint();
			});

			sidebar.add(navigator, BorderLayout.CENTER);

			pane.add(sidebar, BorderLayout.WEST);
		}

		ThemeChangeListener.addThemeChangeListener(t ->
				contentPane.setBackground(t.getColor("Settings.content.background", new Color(235, 235, 235)))
		);
		pane.add(contentPane, BorderLayout.CENTER);

		SettingsGeneral contentGeneral = new SettingsGeneral();
		sectionPanes.put("General", contentGeneral);

		SettingsAppearance contentAppearance = new SettingsAppearance();
		sectionPanes.put("Appearance", contentAppearance);

		sectionPanes.put("Editor", new JPanel());
		sectionPanes.put("Resources", new JPanel());
		sectionPanes.put("In-game Compiler", new JPanel());

		contentPane.add(contentGeneral, BorderLayout.CENTER);
		currentSection = contentGeneral;

		{
			JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
			buttons.setPreferredSize(new Dimension(0,50));
			ThemeChangeListener.addThemeChangeListener(t -> buttons.setBackground(contentPane.getBackground()));

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
					applyEvents.forEach(Runnable::run);
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
		dialog.setContentPane(pane);
		dialog.pack();

		dialog.setTitle("Settings");
		dialog.setIconImage(ImageManager.load("/assets/icons/ui/settings.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		center.x -= dialog.getWidth()/2;
		center.y -= dialog.getHeight()/2;

		dialog.setLocation(center);

		dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
	}

	public static void show() {
		openEvents.forEach(Runnable::run);

		dialog.setVisible(true);
	}

	static void addOpenEvent(Runnable r) {
		openEvents.add(r);
		r.run();
	}

	static void addApplyEvent(Runnable r) {
		applyEvents.add(r);
	}
}
