package com.energyxxer.cbe.ui.editor;

import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.main.window.Window;
import com.energyxxer.cbe.syntax.Syntax;
import com.energyxxer.cbe.ui.Tab;
import com.energyxxer.cbe.ui.explorer.ExplorerItemLabel;
import com.energyxxer.cbe.ui.scrollbar.OverlayScrollPaneLayout;
import com.energyxxer.cbe.ui.scrollbar.ScrollbarUI;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.linenumber.TextLineNumber;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Main text editorComponent of the program. Has support for syntax highlighting, undo,
 * and is linked to abstract tabs.
 */
public class CBEEditor extends JScrollPane implements UndoableEditListener, MouseListener, ThemeChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8584609859858654496L;

    Tab associatedTab;

    public CBEEditorComponent editorComponent;
    private TextLineNumber tln;
	private Syntax syntax;

    //public long lastToolTip = new Date().getTime();

	public CBEEditor(Tab tab) {
		super();
        associatedTab = tab;

        editorComponent = new CBEEditorComponent(this);
        editorComponent.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        super.setViewportView(editorComponent);

        tln = new TextLineNumber(editorComponent, this);
        tln.setPadding(10);

		this.setBorder(BorderFactory.createEmptyBorder());

		KeyStroke undoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
		KeyStroke redoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);

		KeyStroke closeKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK);

		KeyStroke saveKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);

		//editorComponent.getInputMap().put(undoKeystroke, "undoKeystroke");
		//editorComponent.getInputMap().put(redoKeystroke, "redoKeystroke");
		editorComponent.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(closeKeystroke, "closeKeystroke");
		editorComponent.getInputMap().put(saveKeystroke, "saveKeystroke");

		editorComponent.getActionMap().put("saveKeystroke", new AbstractAction() {
			/**
			 *
			 */
			private static final long serialVersionUID = 2L;

			@Override
			public void actionPerformed(ActionEvent e) {
				Tab st = TabManager.getSelectedTab();
				if(st != null) st.save();
			}
		});

		// editorComponent.addMouseMotionListener(hints = new EditorHints(this));
		editorComponent.addMouseListener(this);

		this.setRowHeaderView(tln);
		//tln.setPreferredSize(new Dimension(10,0));

		this.getVerticalScrollBar().setUI(new ScrollbarUI(this, 20));
		this.getHorizontalScrollBar().setUI(new ScrollbarUI(this, 20));
		this.getVerticalScrollBar().setOpaque(false);
		this.getHorizontalScrollBar().setOpaque(false);

		this.setLayout(new OverlayScrollPaneLayout());

		/*linePainter.addPaintListener(() -> {
			this.getVerticalScrollBar().repaint();
			this.getHorizontalScrollBar().repaint();
		});*/

		setComponentZOrder(getVerticalScrollBar(), 0);
		setComponentZOrder(getHorizontalScrollBar(), 1);
		setComponentZOrder(getViewport(), 2);


		addThemeChangeListener();
	}

	public void startEditListeners() {
		editorComponent.getDocument().addUndoableEditListener(this);
	}

	public void setSyntax(Syntax newSyntax) {
		if (syntax != null) {
			for(Map.Entry pair : syntax.getStyles().entrySet()) {
				//System.out.println(pair.getKey() + " = " + pair.getValue());
				editorComponent.removeStyle((String) pair.getKey());
				//it.remove();
			}
		}

		syntax = newSyntax;

		for (Map.Entry<String, HashMap<String, Object>> pair : newSyntax.getStyles().entrySet()) {
			Style style = editorComponent.addStyle(pair.getKey(), null);

			for (Map.Entry pair2 : pair.getValue().entrySet()) {
				if (pair2.getKey() == "color") {
					StyleConstants.setForeground(style, (Color) pair2.getValue());
				} else if (pair2.getKey() == "bold") {
					StyleConstants.setBold(style, (boolean) pair2.getValue());
				} else if (pair2.getKey() == "italic") {
					StyleConstants.setItalic(style, (boolean) pair2.getValue());
				}
			}
		}
	}

	public void setText(String text) {
		editorComponent.setText(text);

		editorComponent.highlight();
	}

	public String getText() {
		try {
			return editorComponent.getDocument().getText(0, editorComponent.getDocument().getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		if (!e.getEdit().getPresentationName().equals("style change")) {
			editorComponent.highlight();
			associatedTab.onEdit();
			//undoManager.undoableEditHappened(e);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		ExplorerItemLabel.setNewSelected(null, false);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void themeChanged(Theme t) {
		editorComponent.setBackground(t.getColor("Editor.background",Color.WHITE));
		setBackground(editorComponent.getBackground());
		editorComponent.setForeground(t.getColor("Editor.foreground",t.getColor("General.foreground",Color.BLACK)));
		editorComponent.setCaretColor(editorComponent.getForeground());
		editorComponent.setSelectionColor(t.getColor("Editor.selection.background",new Color(50, 100, 175)));
		editorComponent.setSelectedTextColor(t.getColor("Editor.selection.foreground", editorComponent.getForeground()));
		editorComponent.setCurrentLineColor(t.getColor("Editor.currentLine.background",new Color(235, 235, 235)));
		editorComponent.setFont(new Font(t.getString("Editor.font","monospaced"), 0, 12));
		tln.setBackground(t.getColor("Editor.lineNumber.background",new Color(235, 235, 235)));
		tln.setForeground(t.getColor("Editor.lineNumber.foreground",new Color(150, 150, 150)));
		//tln current line background
		tln.setCurrentLineForeground(t.getColor("CBEEditor.lineNumber.currentLine.foreground",tln.getForeground()));
		tln.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(
								0,
								0,
								0,
								1,
								t.getColor(
										"Editor.lineNumber.border",
										t.getColor(
												"General.line",
												new Color(200, 200, 200)
										)
								)
						),
						BorderFactory.createEmptyBorder(
								0,
								0,
								0,
								15
						)
				)
		);
		tln.setFont(new Font(t.getString("CBEEditor.lineNumber.font","monospaced"),0,12));

		if (associatedTab.path.endsWith(".mcbe")) {
			setSyntax(Window.getSyntaxForTheme());
			editorComponent.highlight();
		}
	}

    public void displayCaretInfo() {
        editorComponent.displayCaretInfo();
    }

}