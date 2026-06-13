package franca.java;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {

  private DocumentModel document;

  private DocumentTreePanel rightTreePanel;
  private SkiaPanel skiaPanel;
  private GraphicsPanel graphicsPanel;

  public MainFrame() {
    setTitle("JavaFrancaContentViewer");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 800);
    setLocationRelativeTo(null);

    document = DocumentModel.getInstance();

    setJMenuBar(createMenuBar());

    JTabbedPane tabbedPane = new JTabbedPane();

    tabbedPane.addTab("Document Parser", createParserPanel());

    tabbedPane.addTab("Graphics View", new GraphicsPanel());

    skiaPanel = new SkiaPanel(document);
    tabbedPane.addTab("Skia View", createSkiaPanel());

    add(tabbedPane);
  }

  private JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    // Меню File
    JMenu fileMenu = new JMenu("File");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem exitItem = new JMenuItem("Exit");
    exitItem.addActionListener(e -> System.exit(0));

    fileMenu.add(openItem);
    fileMenu.add(saveItem);
    fileMenu.addSeparator();
    fileMenu.add(exitItem);

    // Меню Edit
    JMenu editMenu = new JMenu("Edit");
    JMenuItem copyItem = new JMenuItem("Copy");
    JMenuItem pasteItem = new JMenuItem("Paste");
    editMenu.add(copyItem);
    editMenu.add(pasteItem);

    menuBar.add(fileMenu);
    menuBar.add(editMenu);

    return menuBar;
  }

  private JSplitPane createParserPanel() {
    JSplitPane splitPane = new JSplitPane();
    splitPane.setLeftComponent(new FileSystemListPanel());

    rightTreePanel = new DocumentTreePanel(document);
    splitPane.setRightComponent(rightTreePanel);
    splitPane.setDividerLocation(300);
    return splitPane;
  }

  private JSplitPane createSkiaPanel() {
    JSplitPane splitPane = new JSplitPane();

    skiaPanel = new SkiaPanel(document);
    splitPane.setLeftComponent(skiaPanel);

    // Правая панель — синхронизированное дерево документа
    DocumentTreePanel syncTreePanel = new DocumentTreePanel(document);
    splitPane.setRightComponent(syncTreePanel);
    splitPane.setDividerLocation(600);

    // 1. Клик в дереве → подсветка на SkiaPanel
    syncTreePanel.addTreeSelectionListener(e -> {
      Object selected = syncTreePanel.getSelectedValue();
      if (selected != null) {
        skiaPanel.highlightElement(selected);
        // Также выделяем в правом дереве на вкладке 1
        if (rightTreePanel != null) {
          rightTreePanel.setSelectedValue(selected);
        }
      }
    });

    // 2. Клик на SkiaPanel → выделение в дереве
    skiaPanel.addElementSelectionListener(selected -> {
      if (selected != null) {
        syncTreePanel.setSelectedValue(selected);
        if (rightTreePanel != null) {
          rightTreePanel.setSelectedValue(selected);
        }
      }
    });

    return splitPane;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      new MainFrame().setVisible(true);
    });
  }
}
