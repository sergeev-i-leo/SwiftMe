package franca.java;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Enumeration;

public class DocumentTreePanel extends JPanel {

  private JTree tree;
  private DocumentModel document;

  public DocumentTreePanel(DocumentModel document) {
    this.document = document;
    setLayout(new BorderLayout());

    tree = new JTree(document.getDocumentRoot());
    tree.setCellRenderer(new HighlightTreeRenderer());
    tree.setSelectionRow(0); // выделяем корень по умолчанию

    add(new JScrollPane(tree), BorderLayout.CENTER);
  }

  public void addTreeSelectionListener(javax.swing.event.TreeSelectionListener listener) {
    tree.addTreeSelectionListener(listener);
  }

  public void removeTreeSelectionListener(javax.swing.event.TreeSelectionListener listener) {
    tree.removeTreeSelectionListener(listener);
  }

  public Object getSelectedValue() {
    TreePath path = tree.getSelectionPath();
    if (path != null) {
      return path.getLastPathComponent();
    }
    return null;
  }

  public void setSelectedValue(Object value) {
    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
    TreeNode root = (TreeNode) model.getRoot();

    // Поиск узла в дереве
    DefaultMutableTreeNode node = findNode(root, value);
    if (node != null) {
      // Раскрываем всех родителей
      expandAllParents(node);

      // Выделяем узел
      TreePath path = new TreePath(node.getPath());
      tree.setSelectionPath(path);

      // Скроллим к выделенному узлу
      tree.scrollPathToVisible(path);
    }
  }

  private void expandAllParents(DefaultMutableTreeNode node) {
    TreeNode[] path = node.getPath();
    for (int i = 1; i < path.length; i++) { // i=1 пропускаем корень (не нужно раскрывать)
      tree.expandPath(new TreePath(Arrays.copyOf(path, i + 1)));
    }
  }

  public void expandAll() {
    TreeNode root = (TreeNode) tree.getModel().getRoot();
    expandAll(tree, new TreePath(root));
  }

  private void expandAll(JTree tree, TreePath parent) {
    TreeNode node = (TreeNode) parent.getLastPathComponent();
    if (node.getChildCount() >= 0) {
      for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
        TreeNode n = (TreeNode) e.nextElement();
        TreePath path = parent.pathByAddingChild(n);
        expandAll(tree, path);
      }
    }
    tree.expandPath(parent);
  }

  private DefaultMutableTreeNode findNode(TreeNode node, Object value) {
    if (node instanceof DefaultMutableTreeNode) {
      DefaultMutableTreeNode mutableTreeNode = (DefaultMutableTreeNode) node;
      if (mutableTreeNode.getUserObject().equals(value)) {
        return mutableTreeNode;
      }
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      DefaultMutableTreeNode found = findNode(node.getChildAt(i), value);
      if (found != null) return found;
    }
    return null;
  }

  class HighlightTreeRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,  boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      Object userObj = ((DefaultMutableTreeNode) value).getUserObject();
      String displayText;

      if (userObj instanceof DocumentModel.NodeData) {
        DocumentModel.NodeData data = (DocumentModel.NodeData) userObj;
        displayText = data.text + " [" + data.id + "]";
      } else {
        displayText = userObj.toString();
      }

      super.getTreeCellRendererComponent(tree, displayText, sel, expanded, leaf, row, hasFocus);

      if (sel) {
        setBackground(new Color(51, 153, 255));
        setForeground(Color.WHITE);
        setOpaque(true);
      } else {
        setBackground(null);
        setOpaque(false);
      }
      return this;
    }
  }
}
