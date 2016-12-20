package com.energyxxer.cbe.ui.editor;

import com.energyxxer.cbe.compile.analysis.Analyzer;
import com.energyxxer.cbe.compile.analysis.token.Token;
import com.energyxxer.cbe.compile.analysis.token.TokenStream;
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
import com.energyxxer.cbe.util.linepainter.LinePainter;

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
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
	private UndoManagerFix undoManager;
	private LinePainter linePainter;

	private long lastEdit;
	public long lastToolTip = new Date().getTime();
	private Timer timer;

	public CBEEditor(Tab tab) {
		super();

		editor = new JTextPane(new DefaultStyledDocument());
		editor.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
		super.setViewportView(editor);

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

		this.getVerticalScrollBar().setUI(new ScrollbarUI(this, 20));
		this.getHorizontalScrollBar().setUI(new ScrollbarUI(this, 20));
		this.getVerticalScrollBar().setOpaque(false);
		this.getHorizontalScrollBar().setOpaque(false);

		this.setLayout(new OverlayScrollPaneLayout());

		linePainter.addPaintListener(() -> {
			this.getVerticalScrollBar().repaint();
			this.getHorizontalScrollBar().repaint();
		});

		timer = new Timer(20, this);
		timer.start();

		setComponentZOrder(getVerticalScrollBar(), 0);
		setComponentZOrder(getHorizontalScrollBar(), 1);
		setComponentZOrder(getViewport(), 2);


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

		highlight();
	}

	public String getText() {
		try {
			return editor.getDocument().getText(0, editor.getDocument().getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void highlightSyntax() throws BadLocationException {
		
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
				for(String key : set) {
					if(!token.attributes.get(key).equals(true)) continue;
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
			new Thread(() -> {
				try {
					this.highlightSyntax();
				} catch(BadLocationException e) {
					e.printStackTrace();
				}
			},"Text Highlighter").start();
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
		editor.setBackground(t.getColor("Editor.background",Color.WHITE));
		setBackground(editor.getBackground());
		editor.setForeground(t.getColor("Editor.foreground",t.getColor("General.foreground",Color.BLACK)));
		editor.setCaretColor(editor.getForeground());
		editor.setSelectionColor(t.getColor("Editor.selection.background",new Color(50, 100, 175)));
		editor.setSelectedTextColor(t.getColor("Editor.selection.foreground",editor.getForeground()));
		linePainter.setColor(t.getColor("Editor.currentLine.background",new Color(235, 235, 235)));
		editor.setFont(new Font(t.getString("Editor.font","monospaced"), 0, 12));
		tln.setBackground(t.getColor("Editor.lineNumber.background",new Color(235, 235, 235)));
		tln.setForeground(t.getColor("Editor.lineNumber.foreground",new Color(150, 150, 150)));
		//tln current line background
		tln.setCurrentLineForeground(t.getColor("Editor.lineNumber.currentLine.foreground",tln.getForeground()));
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
		tln.setFont(new Font(t.getString("Editor.lineNumber.font","monospaced"),0,12));
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