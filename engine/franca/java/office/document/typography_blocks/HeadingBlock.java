package franca.java.office.document.typography_blocks;

import franca.java.office.document.Block;

public class HeadingBlock extends Block {

  public int level;
  private String text = "";

  public HeadingBlock(int level) {
    super();
    if ((level >= 1) && (level <= 6)) {
      this.level = level;
    } else {
      this.level = 1;
    }
  }

  public String getClassName() {
    return "HeadingBlock";
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
