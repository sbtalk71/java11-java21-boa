package com.yourkit.examples;

public class MyDocument {
  private String myText;
  private String myDocumentName;
  private int[] myOtherData;

  public MyDocument(final String documentName, final String text) {
    myText = text;
    myDocumentName = documentName;
    myOtherData = new int[1024*1024]; // imagine each document has a lot of data associated with it 
  }

  public String getText() {
    return myText;
  }

  public String getDocumentName() {
    return myDocumentName;
  }
}
