package franca.java;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.*;

public class DocumentModel {

  private static DocumentModel instance;
  private DefaultMutableTreeNode documentRoot;
  private Map<Object, Rectangle> elementBounds;
  private Map<String, Object> idToNode;        // ← идентификатор → узел
  private Map<Object, String> nodeToId;        // ← узел → идентификатор
  private int nextId = 1;

  private DocumentModel() {
    documentRoot = createSampleDocument();
    elementBounds = new HashMap<>();
    idToNode = new HashMap<>();
    nodeToId = new HashMap<>();
    assignIds(documentRoot);  // проставляем ID
  }

  public static DocumentModel getInstance() {
    if (instance == null) {
      instance = new DocumentModel();
    }
    return instance;
  }

  private void assignIds(DefaultMutableTreeNode node) {
    String id = "node_" + nextId++;
    node.setUserObject(new NodeData(node.getUserObject().toString(), id));
    idToNode.put(id, node);
    nodeToId.put(node, id);

    for (int i = 0; i < node.getChildCount(); i++) {
      assignIds((DefaultMutableTreeNode) node.getChildAt(i));
    }
  }

  private DefaultMutableTreeNode createSampleDocument() {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Document");
    DefaultMutableTreeNode paragraph = new DefaultMutableTreeNode("Paragraph");

    DefaultMutableTreeNode word1 = new DefaultMutableTreeNode("Word: Hello");
    word1.add(new DefaultMutableTreeNode("attr: bold"));
    paragraph.add(word1);

    paragraph.add(new DefaultMutableTreeNode("Space"));

    DefaultMutableTreeNode word2 = new DefaultMutableTreeNode("Word: World");
    word2.add(new DefaultMutableTreeNode("attr: italic"));
    paragraph.add(word2);

    root.add(paragraph);
    return root;
  }

  public DefaultMutableTreeNode getDocumentRoot() {
    return documentRoot;
  }

  public void setElementBounds(Object element, Rectangle bounds) {
    elementBounds.put(element, bounds);
  }

  public Rectangle getElementBounds(Object element) {
    return elementBounds.get(element);
  }

  public String getId(Object node) {
    return nodeToId.get(node);
  }

  public Object getNodeById(String id) {
    return idToNode.get(id);
  }

  // Вспомогательный класс для хранения данных с ID
  public static class NodeData {
    public String text;
    public String id;

    public NodeData(String text, String id) {
      this.text = text;
      this.id = id;
    }

    @Override
    public String toString() {
      return text;  // для отображения в дереве
    }
  }
}
