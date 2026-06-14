package franca.java.office.document;

import franca.java.expected.TranspilableClass;
import franca.java.data.json.JsonArray;

import java.util.ArrayList;

public class Block extends TranspilableClass {

  public Block parentBlock = null;

  public JsonArray classes = new JsonArray();
  public JsonArray style = new JsonArray();
  public JsonArray attributes = new JsonArray();

  private ArrayList<Block> blocks = null;

  public String getClassName() {
    return "Block";
  }

  public void addBlock(Block block) {
    if (blocks == null) {
      blocks = new ArrayList<>();
    }
    blocks.add(block);
    block.parentBlock = this;
  }

  public ArrayList<Block> getBlocks() {
    return blocks;
  }

}
