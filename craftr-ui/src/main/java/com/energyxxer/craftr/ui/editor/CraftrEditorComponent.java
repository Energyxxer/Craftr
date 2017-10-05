package com.energyxxer.craftr.ui.editor;

import com.energyxxer.craftr.main.window.CraftrWindow;
import com.energyxxer.craftr.ui.editor.behavior.AdvancedEditor;
import com.energyxxer.craftr.ui.editor.behavior.editmanager.CharacterDriftHandler;
import com.energyxxer.craftr.ui.editor.inspector.Inspector;
import com.energyxxer.craftrlang.compiler.lexical_analysis.Lang;
import com.energyxxer.craftrlang.compiler.lexical_analysis.Scanner;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.Token;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenSection;
import com.energyxxer.craftrlang.compiler.lexical_analysis.token.TokenStream;
import com.energyxxer.craftrlang.compiler.parsing.CraftrProductions;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.TokenMatchResponse;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by User on 1/1/2017.
 */
public class CraftrEditorComponent extends AdvancedEditor implements KeyListener, CaretListener, ActionListener {

    private CraftrEditor parent;

    private StyledDocument sd;

    private Inspector inspector = null;

    private long lastEdit;


    CraftrEditorComponent(CraftrEditor parent) {
        super(new DefaultStyledDocument());
        this.parent = parent;

        sd = this.getStyledDocument();

        if(Lang.getLangForFile(parent.associatedTab.path) != null) this.inspector = new Inspector(this);

        this.addCaretListener(this);

        Timer timer = new Timer(20, this);
        timer.start();

        //this.setOpaque(false);
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        displayCaretInfo();
    }

    private void highlightSyntax() throws BadLocationException {
        if(parent.syntax == null) return;

        sd.putProperty(DefaultEditorKit.EndOfLineStringProperty, "\n");

        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        String text = getText();

        Scanner sc = new Scanner(new File(parent.associatedTab.path), text, new TokenStream(true));
        ArrayList<Notice> newNotices = new ArrayList<>();
        newNotices.addAll(sc.getNotices());

        boolean doParsing = false;

        ArrayList<Token> tokens = new ArrayList<>(sc.getStream().tokens);
        if(tokens.get(0).attributes.get("TYPE").equals("craftr")) doParsing = true;
        tokens.remove(0);

        TokenMatchResponse match = null;

        if(doParsing) {
            ArrayList<Token> f = new ArrayList<>(tokens);
            f.removeIf(t -> !t.isSignificant());

            match = CraftrProductions.FILE.match(f);

            match.pattern.validate();
        }

        for(Token token : tokens) {
            Style style = CraftrEditorComponent.this.getStyle(token.type.toLowerCase());
            if(style != null)
                sd.setCharacterAttributes(token.loc.index, token.value.length(), style, true);
            else
                sd.setCharacterAttributes(token.loc.index, token.value.length(), defaultStyle, true);

            for(Map.Entry<String, Object> entry : token.attributes.entrySet()) {
                if(!entry.getValue().equals(true)) continue;
                Style attrStyle = CraftrEditorComponent.this.getStyle("~" + entry.getKey().toLowerCase());
                if(attrStyle == null) continue;
                sd.setCharacterAttributes(token.loc.index, token.value.length(), attrStyle, false);
            }
            for(Map.Entry<TokenSection, String> entry : token.subSections.entrySet()) {
                TokenSection section = entry.getKey();
                Style attrStyle = CraftrEditorComponent.this.getStyle("~" + entry.getValue().toLowerCase());
                if(attrStyle == null) continue;
                sd.setCharacterAttributes(token.loc.index + section.start, section.length, attrStyle, false);
            }
            for(String tag : token.tags) {
                Style attrStyle = CraftrEditorComponent.this.getStyle("$" + tag.toLowerCase());
                if(attrStyle == null) continue;
                sd.setCharacterAttributes(token.loc.index, token.value.length(), attrStyle, true);
            }

            if(doParsing) {
                ps: for(Map.Entry<String, String[]> entry : parent.parserStyles.entrySet()) {
                    String[] tagList = entry.getValue();
                    int startIndex = token.tags.indexOf(tagList[0]);
                    if(startIndex < 0) continue;
                    for(int i = 0; i < tagList.length; i++) {
                        if(startIndex+i >= token.tags.size() || !tagList[i].equalsIgnoreCase(token.tags.get(startIndex+i))) continue ps;
                    }
                    Style attrStyle = CraftrEditorComponent.this.getStyle(entry.getKey());
                    if(attrStyle == null) continue;
                    sd.setCharacterAttributes(token.loc.index, token.value.length(), attrStyle, true);
                }
            }
        }

        if(match != null && !match.matched) {
            CraftrWindow.setStatus(match.getErrorMessage());
            newNotices.add(new Notice(NoticeType.ERROR, match.getErrorMessage(), match.faultyToken.getFormattedPath()));
            sd.setCharacterAttributes(match.faultyToken.loc.index, match.faultyToken.value.length(), CraftrEditorComponent.this.getStyle("error"), true);
        }

        if(this.inspector != null) {
            this.inspector.inspect(sc.getStream());
            this.inspector.insertNotices(newNotices);
        }
    }

    void highlight() {
        lastEdit = new Date().getTime();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (lastEdit > -1 && (new Date().getTime()) - lastEdit > 500 && parent.associatedTab.isActive()) {
            lastEdit = -1;
            new Thread(() -> {
                try {
                    highlightSyntax();
                } catch(BadLocationException e) {
                    e.printStackTrace();
                }
            },"Text Highlighter").start();
        }
    }

    @Override
    public void registerCharacterDrift(CharacterDriftHandler h) {
        super.registerCharacterDrift(h);

        this.inspector.registerCharacterDrift(h);
    }

    @Override
    public void repaint() {
        if(this.getParent() instanceof JViewport && this.getParent().getParent() instanceof JScrollPane) {
            this.getParent().getParent().repaint();
        } else super.repaint();
    }

    void displayCaretInfo() {
        CraftrWindow.statusBar.setCaretInfo(getCaretInfo());
        CraftrWindow.statusBar.setSelectionInfo(getSelectionInfo());
    }

    @Override
    public String getText() {
        try {
            return getDocument().getText(0, getDocument().getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
