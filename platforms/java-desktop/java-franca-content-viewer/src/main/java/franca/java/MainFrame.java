package franca.java;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;

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

    // Твой готовый компонент
    FileSystemListPanel fileSystemPanel = new FileSystemListPanel();
    fileSystemPanel.setOnFileSelected(() -> {
      File selectedFile = fileSystemPanel.getSelectedFile();
      if ((selectedFile != null) && (selectedFile.getName().endsWith(".html"))) {
        try {
          String content = new String(Files.readAllBytes(selectedFile.toPath()));
          document.loadFromJson(content);

          rightTreePanel.refresh();
          if (skiaPanel != null) {
            skiaPanel.refresh();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if ((selectedFile != null) && (selectedFile.getName().endsWith(".md"))) {
        try {
          String content = new String(Files.readAllBytes(selectedFile.toPath()));
          document.loadFromJson(content);

          // Обновляем деревья
          rightTreePanel.refresh();
          if (skiaPanel != null) {
            skiaPanel.refresh();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    splitPane.setLeftComponent(fileSystemPanel);

    rightTreePanel = new DocumentTreePanel(document);
    splitPane.setRightComponent(rightTreePanel);
    splitPane.setDividerLocation(300);

    return splitPane;
  }

  private JSplitPane createSkiaPanel() {
    JSplitPane splitPane = new JSplitPane();

    skiaPanel = new SkiaPanel(document);
    splitPane.setLeftComponent(skiaPanel);

    DocumentTreePanel syncTreePanel = new DocumentTreePanel(document);
    skiaPanel.setRightTree(syncTreePanel);
    splitPane.setRightComponent(syncTreePanel);
    splitPane.setDividerLocation(600);

    // 1. Клик в дереве → подсветка на SkiaPanel
    syncTreePanel.addTreeSelectionListener(e -> {
      Object selected = syncTreePanel.getSelectedValue();
      System.out.println("Tree selected: " + selected); // отладка
      if (selected != null) {
        skiaPanel.highlightElement(selected);
        if (rightTreePanel != null) {
          rightTreePanel.setSelectedValue(selected);
        }
      }
    });

    // 2. Клик на SkiaPanel → выделение в дереве
    skiaPanel.addElementSelectionListener(selected -> {
      System.out.println("Skia selected: " + selected); // отладка
      if (selected != null) {
        // Проверяем, что syncTreePanel может найти этот узел
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
