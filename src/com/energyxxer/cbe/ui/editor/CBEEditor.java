package com.energyxxer.cbe.ui.editor;

import com.energyxxer.cbe.compile.analysis.Analyzer;
import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenStream;
import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.syntax.Syntax;
import com.energyxxer.cbe.ui.Tab;
import com.energyxxer.cbe.ui.explorer.ExplorerItemLabel;
import com.energyxxer.cbe.ui.theme.Theme;
import com.energyxxer.cbe.ui.theme.change.ThemeChangeListener;
import com.energyxxer.cbe.util.LinePainter;
import com.energyxxer.cbe.util.linenumber.TextLineNumber;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * Main text editor of the program. Has support for syntax highlighting, undo,
 * and is linked to abstract tabs.
 */
public class CBEEditor extends JScrollPane implements UndoableEditListener, ActionListener, MouseListener, ThemeChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8584609859858654496L;

	public JTextPane editor;
	private TextLineNumber tln;
	private Tab associatedTab;
	private StyledDocument sd;
	private Syntax syntax;
	UndoManagerFix undoManager;
	LinePainter linePainter;

	private long lastEdit;
	public long lastToolTip = new Date().getTime();
	private Timer timer;

	public CBEEditor(Tab tab) {
		super();
		editor = new JTextPane(new DefaultStyledDocument());
		editor.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
		super.setViewportView(editor);

		this.getVerticalScrollBar().setBlockIncrement(1);

		linePainter = new LinePainter(editor);

		tln = new TextLineNumber(editor, this);
		tln.setPadding(10);

		associatedTab = tab;
		this.setBorder(BorderFactory.createEmptyBorder());

		sd = editor.getStyledDocument();

		undoManager = new UndoManagerFix();

		KeyStroke undoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
		KeyStroke redoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);

		KeyStroke closeKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK);

		KeyStroke saveKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);

		editor.getInputMap().put(undoKeystroke, "undoKeystroke");
		editor.getInputMap().put(redoKeystroke, "redoKeystroke");
		editor.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(closeKeystroke, "closeKeystroke");
		editor.getInputMap().put(saveKeystroke, "saveKeystroke");

		editor.getActionMap().put("undoKeystroke", new AbstractAction() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (undoManager.canUndo())
					undoManager.undo();
			}
		});
		editor.getActionMap().put("redoKeystroke", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (undoManager.canRedo())
					undoManager.redo();
			}
		});
		editor.getActionMap().put("closeKeystroke", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2L;

			@Override
			public void actionPerformed(ActionEvent e) {
				TabManager.closeTab(TabManager.getSelectedTab());
			}
		});
		editor.getActionMap().put("saveKeystroke", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2L;

			@Override
			public void actionPerformed(ActionEvent e) {
				TabManager.getSelectedTab().save();
			}
		});

		// editor.addMouseMotionListener(hints = new EditorHints(this));
		editor.addMouseListener(this);

		this.setRowHeaderView(tln);
		//tln.setPreferredSize(new Dimension(10,0));


		timer = new Timer(20, this);
		timer.start();

		addThemeChangeListener();
	}

	public void startEditListeners() {
		editor.getDocument().addUndoableEditListener(this);
	}

	@SuppressWarnings("rawtypes")
	public void setSyntax(Syntax newSyntax) {
		if (syntax != null) {
			Iterator it = syntax.getStyles().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				//System.out.println(pair.getKey() + " = " + pair.getValue());
				editor.removeStyle((String) pair.getKey());
				//it.remove();
			}
		}

		syntax = newSyntax;

		Iterator it = newSyntax.getStyles().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			Style style = editor.addStyle((String) pair.getKey(), null);

			@SuppressWarnings("unchecked")
			Iterator it2 = ((HashMap<String, HashMap<String, Object>>) pair.getValue()).entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry pair2 = (Map.Entry) it2.next();
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
		editor.setText(text);

		try {
			highlightSyntax();
		} catch (BadLocationException e) {
			e.printStackTrace(new PrintWriter(Window.consoleOut));
		}
	}

	public String getText() {
		try {
			return editor.getDocument().getText(0, editor.getDocument().getLength());
		} catch (BadLocationException e) {
			e.printStackTrace(new PrintWriter(Window.consoleOut));
			return null;
		}
	}
	
	public void highlightSyntax() throws BadLocationException {
		
		sd.putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");
		
		Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		sd.setCharacterAttributes(0, sd.getLength(), defaultStyle, true);

		String text = getText();
		
		new Analyzer(new File(associatedTab.path), text, new TokenStream(true) {
			@Override
			public void onWrite(Token token) {
				Style style = editor.getStyle(token.type.toLowerCase());
				if(style != null)
					sd.setCharacterAttributes(token.loc.index, token.value.length(), style, true);
				
				Set<String> set = token.attributes.keySet();
				Iterator<String> setI = set.iterator();
				while(setI.hasNext()) {
					String key = setI.next();
					if(!token.attributes.get(key).equals(Boolean.valueOf(true))) continue;
					Style attrStyle = editor.getStyle("#" + key.toLowerCase());
					if(attrStyle == null) continue;
					sd.setCharacterAttributes(token.loc.index, token.value.length(), attrStyle, false);
				}
			}
		});
	}

	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		if (e.getEdit().getPresentationName() != "style change") {
			highlight();
			associatedTab.onEdit();
			undoManager.undoableEditHappened(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (lastEdit > -1 && (new Date().getTime()) - lastEdit > 500 && associatedTab.isActive()) {
			try {
				highlightSyntax();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			lastEdit = -1;
		}
	}

	public void setTabs(int charactersPerTab) {
		FontMetrics fm = editor.getFontMetrics(editor.getFont());
		int charWidth = fm.charWidth(' ');
		int tabWidth = charWidth * charactersPerTab;

		TabStop[] tabs = new TabStop[5];

		for (int j = 0; j < tabs.length; j++) {
			int tab = j + 1;
			tabs[j] = new TabStop(tab * tabWidth);
		}

		TabSet tabSet = new TabSet(tabs);
		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setTabSet(attributes, tabSet);
		int length = editor.getDocument().getLength();
		editor.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
	}

	public void highlight() {
		lastEdit = new Date().getTime();
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
		setBackground(Color.RED);
		editor.setBackground(t.e1);
		editor.setForeground(t.t1);
		editor.setCaretColor(t.t1);
		editor.setSelectionColor(t.h3);
		editor.setSelectedTextColor(t.h4);
		linePainter.setColor(t.h1);
		editor.setFont(new Font(t.font2, 0, 12));
		tln.setBackground(t.e2);
		tln.setForeground(t.t3);
		//tln.setCurrentLineForeground(t.h3);
		tln.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, t.l1), BorderFactory.createEmptyBorder(0, 0, 0, 15)));
		tln.setFont(editor.getFont());
		setTabs(4);

		if (associatedTab.path.endsWith(".mcbe")) {
			setSyntax(Window.getSyntaxForTheme());
			highlight();
		}
	}

	/*@Override
	public int getNumberLines() {
		return getText().split("\n").length;
	}

	@Override
	public Rectangle getLineRect(int line) {
		try {
			int start = Utilities.getRowStart(editor, line);
			int end = Utilities.getRowEnd(editor, line);
			return new Rectangle(0, start,0, end-start);
		} catch (BadLocationException e) {
			return new Rectangle();
		}
	}*/
}

class UndoManagerFix extends UndoManager {

	private static final long serialVersionUID = 5335352180435980549L;

	@Override
	public synchronized void undo() throws CannotUndoException {
		do {
			UndoableEdit edit = editToBeUndone();
			if (edit instanceof AbstractDocument.DefaultDocumentEvent) {
				AbstractDocument.DefaultDocumentEvent event = (AbstractDocument.DefaultDocumentEvent) edit;
				if (event.getType() == EventType.CHANGE) {
					super.undo();
					continue;
				}
			}
			break;
		} while (true);

		super.undo();
	}

	@Override
	public synchronized void redo() throws CannotRedoException {
		super.redo();
		// int caretPosition = getCaretPosition();

		do {
			UndoableEdit edit = editToBeRedone();
			if (edit instanceof AbstractDocument.DefaultDocumentEvent) {
				AbstractDocument.DefaultDocumentEvent event = (AbstractDocument.DefaultDocumentEvent) edit;
				if (event.getType() == EventType.CHANGE) {
					super.redo();
					continue;
				}
			}
			break;
		} while (true);

		// setCaretPosition(caretPosition);
	}

}