package com.energyxxer.cbe.ui.editor;

import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import com.energyxxer.cbe.compile.analysis.Analyzer;
import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenStream;
import com.energyxxer.cbe.global.TabManager;
import com.energyxxer.cbe.main.Window;
import com.energyxxer.cbe.syntax.Syntax;
import com.energyxxer.cbe.ui.Tab;
import com.energyxxer.cbe.ui.explorer.ExplorerItemLabel;
import com.energyxxer.cbe.util.TextLineNumber;

/**
 * Main text editor of the program. Has support for syntax highlighting, undo,
 * and is linked to abstract tabs.
 */
public class CBEEditor extends JScrollPane implements UndoableEditListener, ActionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8584609859858654496L;

	public JTextPane editor;
	public TextLineNumber tln;
	public Tab associatedTab;
	public StyledDocument sd;
	public Syntax syntax;
	UndoManagerFix undoManager;

	private long lastEdit;
	public long lastToolTip = new Date().getTime();
	private Timer timer;

	public CBEEditor(Tab tab) {
		super(new JTextPane(new DefaultStyledDocument()));
		editor = (JTextPane) this.getViewport().getView();
		editor.setBackground(Window.theme.p1);
		editor.setForeground(Window.theme.t1);
		editor.setCaretColor(Window.theme.t1);
		tln = new TextLineNumber(editor);
		associatedTab = tab;
		this.setBorder(BorderFactory.createEmptyBorder());

		if (tab.path.endsWith(".mcbi") || tab.path.endsWith(".mcbe"))
			setSyntax(Window.getSyntaxForTheme());

		sd = editor.getStyledDocument();

		undoManager = new UndoManagerFix();

		editor.setFont(new Font("Consolas", 0, 12));
		setTabs(4);

		tln.setBackground(Window.theme.p2);
		tln.setForeground(Window.theme.t3);
		tln.setCurrentLineForeground(tln.getForeground());

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

		timer = new Timer(20, this);
		timer.start();
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
				it.remove();
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
	protected void paintComponent(Graphics g) {
		tln.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Window.theme.l1));
		super.paintComponent(g);
	}

	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		if (e.getEdit().getPresentationName() != "style change") {
			lastEdit = new Date().getTime();
			associatedTab.onEdit();
			undoManager.undoableEditHappened(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (lastEdit > -1 && (new Date().getTime()) - lastEdit > 500) {
			try {
				highlightSyntax();
			} catch (BadLocationException e) {
				e.printStackTrace(new PrintWriter(Window.consoleOut));
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