package franca.java;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
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
      TreePath path = new TreePath(node.getPath());
      tree.setSelectionPath(path);
      tree.scrollPathToVisible(path);
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
      DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) node;
      if (dmtn.getUserObject().equals(value)) {
        return dmtn;
      }
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      DefaultMutableTreeNode found = findNode(node.getChildAt(i), value);
      if (found != null) return found;
    }
    return null;
  }

  // Рендерер для стандартного выделения (подсветка выбранного элемента)
  class HighlightTreeRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

      super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

      // Если элемент выделен — стандартное выделение Swing
      if (sel) {
        setBackground(new Color(51, 153, 255)); // синее выделение
        setForeground(Color.WHITE);
        setOpaque(true);
      } else {
        // Если не выделен, но есть bounds в модели — показываем, что он есть на канвасе
        if (document.getElementBounds(value) != null) {
          setBackground(new Color(200, 255, 200)); // светло-зелёный фон
          setForeground(Color.BLACK);
          setOpaque(true);
        } else {
          setBackground(null);
          setOpaque(false);
        }
      }
      return this;
    }
  }
}
