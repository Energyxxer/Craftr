package com.energyxxer.util.out;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;

import com.energyxxer.html.HTMLFile;

/**
 * An output stream. Used in the java console visible in the window.
 * */
public class TextAreaOutputStream extends OutputStream {

   private final JEditorPane textArea;
   private final StringBuilder sb = new StringBuilder();
   
   private final HTMLFile html = new HTMLFile();

   public TextAreaOutputStream(final JEditorPane textArea) {
      this.textArea = textArea;
      sb.append("");
   }

   @Override
   public void flush() {
   }

   @Override
   public void close() {
   }

   @Override
   public void write(int b) throws IOException {

      if (b == '\r')
         return;

      if (b == '\n') {
         final String text = sb.toString() + "\n";
         
         html.append(text);
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               textArea.setText(html.getText());
            }
         });
         sb.setLength(0);
         sb.append("");
         return;
      }

      sb.append((char) b);
   }
}