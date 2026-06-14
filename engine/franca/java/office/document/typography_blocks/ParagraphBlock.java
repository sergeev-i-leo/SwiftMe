package franca.java.office.document.typography_blocks;

import franca.java.office.document.Block;

public class ParagraphBlock extends Block {

  private String text = "";

  public String getClassName() {
    return "ParagraphBlock";
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
