package franca.java;

import franca.java.parsers.json.JsonArray;
import franca.java.parsers.json.JsonElement;
import franca.java.parsers.json.JsonObject;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;

public class JsonTreePanel extends JPanel {

  private JTree tree;
  private JsonElement rootElement;

  public JsonTreePanel() {
    setLayout(new BorderLayout());
    tree = new JTree();
    tree.setCellRenderer(new JsonTreeCellRenderer());
    add(new JScrollPane(tree), BorderLayout.CENTER);
  }

  public void refresh(JsonElement root) {
    this.rootElement = root;
    DefaultMutableTreeNode rootNode = buildTree(root);
    tree.setModel(new DefaultTreeModel(rootNode));
    expandAll(tree, new TreePath(rootNode));
    repaint();
  }

  private DefaultMutableTreeNode buildTree(JsonElement element) {
    if (element == null) {
      return new DefaultMutableTreeNode("null");
    }

    // JsonArray
    JsonArray array = element.asJsonArray();
    if (array != null) {
      DefaultMutableTreeNode arrayNode = new DefaultMutableTreeNode("Array[" + array.size() + "]");
      for (int i = 0; i < array.size(); i++) {
        DefaultMutableTreeNode childNode = buildTree(array.get(i));
        arrayNode.add(childNode);
      }
      return arrayNode;
    }

    // JsonObject
    JsonObject obj = element.asJsonObject();
    if (obj != null) {
      DefaultMutableTreeNode objNode = new DefaultMutableTreeNode("Object");
      ArrayList<String> keys = obj.keys();

      for (String key : keys) {
        JsonElement value = obj.get(key);
        String displayText = key + ": " + getShortValue(value);
        DefaultMutableTreeNode keyNode = new DefaultMutableTreeNode(displayText);

        // Если значение сложное (объект или массив) — раскрываем рекурсивно
        if (value != null && (value.asJsonObject() != null || value.asJsonArray() != null)) {
          DefaultMutableTreeNode childNode = buildTree(value);
          keyNode.add(childNode);
        }

        objNode.add(keyNode);
      }
      return objNode;
    }

    // Примитивы
    return buildPrimitiveNode(element);
  }

  private String getShortValue(JsonElement element) {
    if (element == null) return "null";

    JsonArray arr = element.asJsonArray();
    if (arr != null) return "Array[" + arr.size() + "]";

    JsonObject obj = element.asJsonObject();
    if (obj != null) return "Object";

    String str = element.getStringValue();
    if (str != null) return str;  // ← без кавычек

    Integer intVal = element.getIntegerValue();
    if (intVal != null) return String.valueOf(intVal);

    Double dblVal = element.getDoubleValue();
    if (dblVal != null) return String.valueOf(dblVal);

    Boolean boolVal = element.getBooleanValue();
    if (boolVal != null) return String.valueOf(boolVal);

    return "null";
  }

  private DefaultMutableTreeNode buildPrimitiveNode(JsonElement element) {
    String str = element.getStringValue();
    if (str != null) {
      return new DefaultMutableTreeNode(str);  // ← без кавычек
    }

    Integer intVal = element.getIntegerValue();
    if (intVal != null) {
      return new DefaultMutableTreeNode(String.valueOf(intVal));
    }

    Double doubleVal = element.getDoubleValue();
    if (doubleVal != null) {
      return new DefaultMutableTreeNode(String.valueOf(doubleVal));
    }

    Boolean boolVal = element.getBooleanValue();
    if (boolVal != null) {
      return new DefaultMutableTreeNode(String.valueOf(boolVal));
    }

    return new DefaultMutableTreeNode("null");
  }

  private void expandAll(JTree tree, TreePath parent) {
    javax.swing.tree.TreeNode node = (javax.swing.tree.TreeNode) parent.getLastPathComponent();
    if (node.getChildCount() >= 0) {
      for (int i = 0; i < node.getChildCount(); i++) {
        javax.swing.tree.TreeNode child = node.getChildAt(i);
        TreePath path = parent.pathByAddingChild(child);
        expandAll(tree, path);
      }
    }
    tree.expandPath(parent);
  }

  private static class JsonTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final Color COLOR_OBJECT = new Color(128, 0, 128);      // фиолетовый
    private static final Color COLOR_ARRAY = new Color(0, 0, 128);         // тёмно-синий
    private static final Color COLOR_STRING = new Color(0, 128, 0);        // зелёный
    private static final Color COLOR_NUMBER = new Color(0, 0, 255);        // синий
    private static final Color COLOR_BOOLEAN = new Color(255, 128, 0);     // оранжевый
    private static final Color COLOR_NULL = new Color(128, 128, 128);      // серый

    private Font defaultFont;

    public JsonTreeCellRenderer() {
      defaultFont = UIManager.getFont("Tree.font");
      setFont(defaultFont);
      setIcon(null);  // убираем стандартные иконки
      setClosedIcon(null);
      setOpenIcon(null);
      setLeafIcon(null);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

      super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
      setIcon(null);

      if (value instanceof DefaultMutableTreeNode) {
        Object userObj = ((DefaultMutableTreeNode) value).getUserObject();

        if (userObj instanceof String) {
          String text = (String) userObj;

          if (text.startsWith("Array")) {
            setForeground(COLOR_ARRAY);
          } else if (text.equals("Object")) {
            setForeground(COLOR_OBJECT);
          } else if (text.startsWith("\"") && text.endsWith("\"")) {
            setForeground(COLOR_STRING);
          } else if (text.equals("true") || text.equals("false")) {
            setForeground(COLOR_BOOLEAN);
          } else if (text.equals("null")) {
            setForeground(COLOR_NULL);
          } else {
            // пробуем определить число
            try {
              Double.parseDouble(text);
              setForeground(COLOR_NUMBER);
            } catch (NumberFormatException e) {
              setForeground(Color.BLACK);
            }
          }
        }
      }

      if (sel) {
        setBackground(new Color(51, 153, 255));
        setForeground(Color.WHITE);
      } else {
        setBackground(null);
      }

      return this;
    }
  }
}
