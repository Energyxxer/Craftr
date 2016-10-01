package com.energyxxer.ui.editor;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.PrintStream;
import java.lang.reflect.Field;

import javax.swing.ToolTipManager;
import javax.swing.text.BadLocationException;

import com.energyxxer.cbe.Window;
import com.energyxxer.syntax.SyntaxConstants;

/**
 * This is supposed to show a tooltip when you hover over words in the file, but the cooldown isn't really working.
 * */
public class EditorHints implements MouseMotionListener {
	
	CBEEditor editor;
	String lastToken = "";
	
	public EditorHints(CBEEditor editor) {
		this.editor = editor;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int pointed = editor.editor.viewToModel(new Point(e.getX(),e.getY()));
		String text;
		try {
			text = editor.editor.getDocument().getText(0, editor.editor.getDocument().getLength());
			try {
				text.charAt(pointed);
			} catch(StringIndexOutOfBoundsException err) {
				lastToken = null;
				editor.editor.setToolTipText(null);
				return;
			}
			if(text.charAt(pointed) == '\n') {
				lastToken = null;
				editor.editor.setToolTipText(null);
				return;
			}
			if(!SyntaxConstants.alphanumeric.contains(text.substring(pointed,pointed+1))) {
				lastToken = null;
				editor.editor.setToolTipText(null);
				return;
			}
			
			/*Get full word*/
			
			int start = pointed;
			int end = pointed;
			
			while(start-1 >= 0 && SyntaxConstants.alphanumeric.contains(text.substring(start-1,start))) {
				start--;
			}
			while(end+1 <= text.length() && SyntaxConstants.alphanumeric.contains(text.substring(end,end+1))) {
				end++;
			}
			
			String token = text.substring(start,end);
			if(lastToken != token) {
				lastToken = token;
				
				if(isToolTipVisible()) {
					//System.out.println("tooltip enabled");
					//editor.editor.setToolTipText(null);
					new java.util.Timer().schedule( 
					        new java.util.TimerTask() {
					            @Override
					            public void run() {
					            	editor.editor.setToolTipText(token);
					            }
					        }, 
					        1000
					);
				} else {
					editor.editor.setToolTipText(token);
				}
				//editor.lastToolTip = new Date().getTime();
			}
			
		} catch (BadLocationException e1) {
			e1.printStackTrace(new PrintStream(Window.consoleout));
		}
	}
	
	private static boolean isToolTipVisible() {
		// Going to do some nasty reflection to get at this private field.  Don't try this at home!
		ToolTipManager ttManager = ToolTipManager.sharedInstance();
		try {
			Field f = ttManager.getClass().getDeclaredField("tipShowing");
			f.setAccessible(true);

			boolean tipShowing = f.getBoolean(ttManager);

			return tipShowing;

		} catch (Exception e) {
			// We'll keep silent about this for now, but obviously we don't want to hit this
			// e.printStackTrace();
			return false;
		}
	}
}
