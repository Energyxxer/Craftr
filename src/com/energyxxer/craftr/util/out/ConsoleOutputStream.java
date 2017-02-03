package com.energyxxer.craftr.util.out;

import com.energyxxer.craftr.global.Console;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by User on 1/8/2017.
 */
public class ConsoleOutputStream extends OutputStream {

    private final JTextPane component;
    public String style = "";

    private static final int NONE = 0,
                             PATH = 1,
                             LOCATION = 2,
                             LENGTH = 3,
                             TEXT = 4;

    private HashMap<String, StringBuilder> hyperLinkElements = new HashMap<>();

    private int hyperLinkStage = NONE;

    public ConsoleOutputStream(final JTextPane component) {
        this.component = component;
    }

    public ConsoleOutputStream(JTextPane component, String style) {
        this.component = component;
        this.style = style;
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public void write(int b) throws IOException {
        if (b == '\r')
            return;

        //Hyperlink Syntax:
        // \b PATH \b LINE \b COLUMN \b LENGTH \b TEXT \b

        switch(hyperLinkStage) {
            case NONE: {
                if(b == '\b') {
                    hyperLinkStage++;
                    hyperLinkElements.put("PATH",new StringBuilder());
                    hyperLinkElements.put("LOCATION",new StringBuilder());
                    hyperLinkElements.put("LENGTH",new StringBuilder());
                    hyperLinkElements.put("TEXT",new StringBuilder());
                } else {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            component.getStyledDocument().insertString(component.getStyledDocument().getLength(),((char) b) + "", (style == null) ? null : component.getStyle(style));
                        } catch(BadLocationException e) {}
                    });
                }
                break;
            }
            case PATH: {
                if(b == '\b') {
                    hyperLinkStage++;
                } else {
                    hyperLinkElements.get("PATH").append((char) b);
                }
                break;
            }
            case LOCATION: {
                if(b == '\b') {
                    hyperLinkStage++;
                } else if(Character.isDigit((char) b)) {
                    hyperLinkElements.get("LOCATION").append((char) b);
                }
                break;
            }
            case LENGTH: {
                if(b == '\b') {
                    hyperLinkStage++;
                } else if(Character.isDigit((char) b)) {
                    hyperLinkElements.get("LENGTH").append((char) b);
                }
                break;
            }
            case TEXT: {
                if(b == '\b') {
                    //Finalize
                    hyperLinkStage = NONE;

                    SwingUtilities.invokeLater(() -> {
                        String path = hyperLinkElements.get("PATH").toString();
                        String location = hyperLinkElements.get("LOCATION").toString();
                        String length = hyperLinkElements.get("LENGTH").toString();
                        Style hyperlink = component.addStyle("hyperlink:"+path+"?"+location+"&"+length, null);
                        hyperlink.addAttribute("IS_HYPERLINK",true);
                        hyperlink.addAttribute("PATH",path);
                        hyperlink.addAttribute("LOCATION",location);
                        hyperlink.addAttribute("LENGTH",length);
                        StyleConstants.setForeground(hyperlink,new Color(50, 100, 175));
                        StyleConstants.setUnderline(hyperlink, true);
                        try {
                            component.getStyledDocument().insertString(
                                    component.getStyledDocument().getLength(),
                                    hyperLinkElements.get("TEXT").toString(),
                                    hyperlink
                            );
                            Console.info.println("Adding " + hyperLinkElements.get("TEXT").toString());
                        } catch(BadLocationException e) {}
                    });

                } else {
                    hyperLinkElements.get("TEXT").append((char) b);
                }
                break;
            }
        }
    }
}
