package com.yourkit.examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MyMainFrame extends JFrame {
  private ArrayList<MyDocument> documents;
  private int nextDocumentNumber;

  public MyMainFrame() {
    super("Example 3");

    documents = new ArrayList<MyDocument>();

    final JButton button = new JButton("Open Document");
    button.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
          openDocument();
        }
      }
    );

    final JPanel panel = new JPanel(new GridBagLayout());
    panel.add(
      button,
      new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0)
    );
    panel.setPreferredSize(new Dimension(300, 100));
    setContentPane(panel);

    pack();
    setLocationRelativeTo(null);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  private void openDocument() {

    // prepare sample text

    final String documentName = "Document #" + nextDocumentNumber++;

    // create and remember document
    final MyDocument document = new MyDocument(documentName, createDocumentText());
    documents.add(document);

    final MyDocumentWindow window = new MyDocumentWindow(this, document);
    window.show();
  }

  private String createDocumentText() {
    String text = "";
    for (int i=0; i < 3000; i++){
      text += "Line #" + i + "\n";
    }
    return text;
  }

  public static void main(final String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception ignored) {
    }

    // Create and show the main frame
    final MyMainFrame frame = new MyMainFrame();
    frame.setVisible(true);
  }
}
