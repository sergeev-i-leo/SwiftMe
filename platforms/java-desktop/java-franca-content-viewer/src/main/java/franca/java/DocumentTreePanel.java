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
    tree.setSelectionRow(0);

    add(new JScrollPane(tree), BorderLayout.CENTER);
  }

  public void refresh() {
    tree.setModel(new DefaultTreeModel(document.getDocumentRoot()));
    tree.repaint();
    expandAll(); // если нужно
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
      DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
      return selectedNode;
    }
    return null;
  }

  public void setSelectedValue(Object value) {
    // Получаем ID из переданного объекта
    String targetId = null;
    if (value instanceof DefaultMutableTreeNode) {
      Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
      if (userObject instanceof DocumentModel.NodeData) {
        targetId = ((DocumentModel.NodeData) userObject).id;
      }
    } else {
      targetId = document.getId(value);
    }

    if (targetId == null) {
      System.err.println("Cannot find ID for value: " + value);
      return;
    }

    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
    TreeNode root = (TreeNode) model.getRoot();

    DefaultMutableTreeNode node = findNodeById(root, targetId);
    if (node != null) {
      expandAllParents(node);
      TreePath path = new TreePath(node.getPath());
      tree.setSelectionPath(path);
      tree.scrollPathToVisible(path);
    } else {
      System.err.println("Node not found for ID: " + targetId);
    }
  }

  private DefaultMutableTreeNode findNodeById(TreeNode node, String targetId) {
    if (node instanceof DefaultMutableTreeNode) {
      DefaultMutableTreeNode mutableTreeNode = (DefaultMutableTreeNode) node;
      Object userObject = mutableTreeNode.getUserObject();

      String nodeId = null;
      if (userObject instanceof DocumentModel.NodeData) {
        nodeId = ((DocumentModel.NodeData) userObject).id;
      }

      if (targetId != null && targetId.equals(nodeId)) {
        return mutableTreeNode;
      }
    }

    for (int i = 0; i < node.getChildCount(); i++) {
      DefaultMutableTreeNode found = findNodeById(node.getChildAt(i), targetId);
      if (found != null) return found;
    }
    return null;
  }

  private void expandAllParents(DefaultMutableTreeNode node) {
    TreeNode[] path = node.getPath();
    for (int i = 1; i < path.length; i++) {
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

  class HighlightTreeRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

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
