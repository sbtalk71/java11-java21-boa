package com.yourkit.examples;

import javax.swing.*;
import java.awt.*;

public class MyDocumentWindow extends JDialog {
  public MyDocumentWindow(final JFrame owner, final MyDocument document) {
    super(owner);
    setModal(true);

    //...Create the GUI and put it in the window...

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(createContent(document));

    setTitle(document.getDocumentName());

    setSize(300, 300);
  }

  private JComponent createContent(final MyDocument document) {
    final JTextArea textArea = new JTextArea();
    textArea.setText(document.getText());
    return new JScrollPane(textArea);
  }
}
