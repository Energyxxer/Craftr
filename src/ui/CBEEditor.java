package ui;

import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
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

import cbe.Tab;
import cbe.TabManager;
import syntax.CBESyntax;
import syntax.Syntax;
import syntax.SyntaxConstants;
import util.StringUtil;
import util.TextLineNumber;

public class CBEEditor extends JScrollPane implements UndoableEditListener, ActionListener {
	
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
    private Timer timer;
	
	public CBEEditor(Tab tab) {
		super(new JTextPane(new DefaultStyledDocument()));
		editor = (JTextPane) this.getViewport().getView();
		tln = new TextLineNumber(editor);
		associatedTab = tab;
		this.setBorder(BorderFactory.createEmptyBorder());

		setSyntax(CBESyntax.INSTANCE);
		
		sd = editor.getStyledDocument();
		
		undoManager = new UndoManagerFix();
		
		editor.setFont(new Font("Consolas",0,12));
		setTabs(4);
		
		tln.setForeground(new Color(150,150,150));
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

			public void actionPerformed(ActionEvent e) {
				if(undoManager.canUndo()) undoManager.undo();
		    }
		});
		editor.getActionMap().put("redoKeystroke", new AbstractAction() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 2L;

			public void actionPerformed(ActionEvent e) {
		    	if(undoManager.canRedo()) undoManager.redo();
		    }
		});
		editor.getActionMap().put("closeKeystroke", new AbstractAction() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 2L;

			public void actionPerformed(ActionEvent e) {
		    	TabManager.closeTab(TabManager.getSelectedTab());
		    }
		});
		editor.getActionMap().put("saveKeystroke", new AbstractAction() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 2L;

			public void actionPerformed(ActionEvent e) {
		    	TabManager.getSelectedTab().save();
		    }
		});

		this.setRowHeaderView( tln );
		
		timer = new Timer(20, this);
		timer.start();
	}

	public void startEditListeners() {
		editor.getDocument().addUndoableEditListener(this);
	}
	
	@SuppressWarnings("rawtypes")
	public void setSyntax(Syntax newSyntax) {
		if(syntax != null) {
			Iterator it = syntax.getStyles().entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(pair.getKey() + " = " + pair.getValue());
		        editor.removeStyle((String) pair.getKey());
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		}
		
		syntax = newSyntax;

		Iterator it = newSyntax.getStyles().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Style style = editor.addStyle((String) pair.getKey(), null);
	        
	        @SuppressWarnings("unchecked")
			Iterator it2 = ((HashMap<String, HashMap<String, Object>>) pair.getValue()).entrySet().iterator();
	        while(it2.hasNext()) {
	        	Map.Entry pair2 = (Map.Entry)it2.next();
	        	if((String) pair2.getKey() == "color") {
	        		StyleConstants.setForeground(style, (Color) pair2.getValue());
	        	} else if((String) pair2.getKey() == "bold") {
	        		StyleConstants.setBold(style, (boolean) pair2.getValue());
	        	} else if((String) pair2.getKey() == "italic") {
	        		StyleConstants.setItalic(style, (boolean) pair2.getValue());
	        	}
	        	//it2.remove(); // avoids a ConcurrentModificationException
	        }
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public void setText(String text) {
		editor.setText(text);
		
		try {
			highlightSyntax();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void highlightSyntax() throws BadLocationException {
		editor.getDocument().removeUndoableEditListener(this);
		Style defaultStyle = StyleContext.
				   getDefaultStyleContext().
				   getStyle(StyleContext.DEFAULT_STYLE);
		
		sd.setCharacterAttributes(0, sd.getLength(), defaultStyle, true);
		
		
		String text = sd.getText(0, sd.getLength());
		
		boolean inString = false;
		boolean inComment = false;
		boolean inMultiLineComment = false;
		
		String lastStyle = null;
		
		mainIteration: for(int i = 0; i < text.length(); i++) {
			if(inString) {
				if(text.substring(i,i+1).intern() == "\\") {
	        		lastStyle = "escape";
					sd.setCharacterAttributes(i, 2, editor.getStyle("escape"), true);
					i++;
					continue;
				}
				ArrayList<String> stringPatterns = syntax.getPatterns().get("string");
				for(int j = 0; j < stringPatterns.size(); j++) {
					if(text.substring(i).startsWith(stringPatterns.get(j))) {
						inString = false;
		        		lastStyle = "string";
						sd.setCharacterAttributes(i, stringPatterns.get(j).length(), editor.getStyle("string"), true);
						continue mainIteration;
					}
				}
			}
			if(text.substring(i,i+1).intern() == "\n") {
				if(inComment && !inMultiLineComment) {
					inComment = false;
				}
			}
			if(text.substring(i,i+1).intern() == ".") {
				if(!inComment && !inString && lastStyle == "digit") {
					sd.setCharacterAttributes(i, 1, editor.getStyle("digit"), true);
					continue mainIteration;
				}
			}
			if(inComment && inMultiLineComment) {
				if(text.substring(i).startsWith(syntax.getPatterns().get("multilinecomment").get(1))) {
					inComment = inMultiLineComment = false;
					sd.setCharacterAttributes(i, syntax.getPatterns().get("multilinecomment").get(1).length(), editor.getStyle("multilinecomment"), true);
					i += syntax.getPatterns().get("multilinecomment").get(1).length();
				}
			}
			Iterator it = syntax.getPatterns().entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        
		        @SuppressWarnings("unchecked")
				ArrayList<String> patterns = (ArrayList<String>)pair.getValue();
		        HashMap<String,Object> rules = syntax.getStyles().get((String)pair.getKey());
		        
		        
		        for(int j = 0; j < patterns.size(); j++) {
		        	if(!inComment && !inString) {
			        	if(text.substring(i).startsWith(patterns.get(j))) {
			        		if((String)pair.getKey() == "comment") {
			        			inComment = true;
			        		}
			        		if((String)pair.getKey() == "multilinecomment" && j == 0) {
			        			inComment = inMultiLineComment = true;
			        		}
			        		if((String)pair.getKey() == "string") {
			        			inString = !inString;
			        		}
			        		if(rules.containsKey("whole") && (boolean) rules.get("whole") == true) {
				        		if(!Arrays.asList(SyntaxConstants.alphanumeric.split("")).contains(StringUtil.substring(text,i-1,i)) && !Arrays.asList(SyntaxConstants.alphanumeric.split("")).contains(StringUtil.substring(text,i+patterns.get(j).length(),i+patterns.get(j).length()+1))) {
				        			lastStyle = (String) pair.getKey();
				        			sd.setCharacterAttributes(i, patterns.get(j).length(), editor.getStyle((String) pair.getKey()), true);
				        			i += patterns.get(j).length()-1;
				        		}
				        	} else {
				        		lastStyle = (String) pair.getKey();
				        		sd.setCharacterAttributes(i, patterns.get(j).length(), editor.getStyle((String) pair.getKey()), true);
			        			i += patterns.get(j).length()-1;
				        	}
			        	}
		        	} else if(inString) {
		        		lastStyle = "string";
		        		sd.setCharacterAttributes(i, 1, editor.getStyle("string"), true);
		        	} else if(inComment && !inMultiLineComment) {
		        		lastStyle = "comment";
		        		sd.setCharacterAttributes(i, 1, editor.getStyle("comment"), true);
		        	} else if(inComment && inMultiLineComment) {
		        		lastStyle = "multilinecomment";
		        		sd.setCharacterAttributes(i, 1, editor.getStyle("multilinecomment"), true);
		        	}
		        }
		    }
        }
		editor.getDocument().addUndoableEditListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		tln.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(150,150,150)));
		super.paintComponent(g);
	}

	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		if(e.getEdit().getPresentationName() != "style change") {
			lastEdit = new Date().getTime();
			associatedTab.onEdit();
			undoManager.undoableEditHappened(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(lastEdit > -1 && (new Date().getTime()) - lastEdit > 500) {
			try {
				highlightSyntax();
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lastEdit = -1;
		}
	}
	
	public void setTabs(int charactersPerTab)
    {
        FontMetrics fm = editor.getFontMetrics( editor.getFont() );
        int charWidth = fm.charWidth( ' ' );
        int tabWidth = charWidth * charactersPerTab;

        TabStop[] tabs = new TabStop[5];

        for (int j = 0; j < tabs.length; j++)
        {
            int tab = j + 1;
            tabs[j] = new TabStop( tab * tabWidth );
        }

        TabSet tabSet = new TabSet(tabs);
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setTabSet(attributes, tabSet);
        int length = editor.getDocument().getLength();
        editor.getStyledDocument().setParagraphAttributes(0, length, attributes, false);
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
        //int caretPosition = getCaretPosition();

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

        //setCaretPosition(caretPosition);
    }

}