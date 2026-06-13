package franca.java;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

public class SkiaPanel extends JPanel {

  private DocumentModel document;
  private Object highlightedElement;
  private Map<Object, Rectangle> elementBounds; // локальный кэш координат

  public SkiaPanel(DocumentModel document) {
    this.document = document;
    this.elementBounds = new HashMap<>();
    setBackground(Color.WHITE);

    // Обработка кликов мыши
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        Object clicked = getElementAt(e.getX(), e.getY());
        if (clicked != null) {
          setHighlightedElement(clicked);
          // Уведомляем слушателей о выборе элемента
          fireElementSelected(clicked);
        }
      }
    });
  }

  // Получить элемент по координатам
  public Object getElementAt(int x, int y) {
    for (Map.Entry<Object, Rectangle> entry : elementBounds.entrySet()) {
      if (entry.getValue().contains(x, y)) {
        return entry.getKey();
      }
    }
    return null;
  }

  // Установить подсвеченный элемент
  public void highlightElement(Object element) {
    this.highlightedElement = element;
    repaint();
  }

  private void setHighlightedElement(Object element) {
    this.highlightedElement = element;
    repaint();
  }

  // Слушатели для синхронизации с деревом
  private java.util.List<ElementSelectionListener> listeners = new java.util.ArrayList<>();

  public void addElementSelectionListener(ElementSelectionListener listener) {
    listeners.add(listener);
  }

  public void removeElementSelectionListener(ElementSelectionListener listener) {
    listeners.remove(listener);
  }

  private void fireElementSelected(Object element) {
    for (ElementSelectionListener listener : listeners) {
      listener.elementSelected(element);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    elementBounds.clear();

    // Рисуем дерево документа
    int y = 50;
    drawTree(g2d, document.getDocumentRoot(), 50, y);

    // Подсветка выбранного элемента
    if (highlightedElement != null) {
      Rectangle bounds = elementBounds.get(highlightedElement);
      if (bounds != null) {
        g2d.setColor(new Color(255, 255, 0, 100));
        g2d.fill(bounds);
        g2d.setColor(Color.YELLOW);
        g2d.draw(bounds);
      }
    }
  }

  private void drawTree(Graphics2D g2d, DefaultMutableTreeNode node, int x, int y) {
    String text = node.getUserObject().toString();
    FontMetrics fm = g2d.getFontMetrics();
    Rectangle2D rect = fm.getStringBounds(text, g2d);
    Rectangle bounds = new Rectangle(x, y, (int)rect.getWidth() + 20, (int)rect.getHeight() + 10);

    // Сохраняем координаты элемента
    elementBounds.put(node, bounds);
    document.setElementBounds(node, bounds);

    g2d.setColor(Color.BLACK);
    g2d.drawString(text, x + 10, y + fm.getAscent());
    g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

    // Рекурсивно рисуем детей
    int childY = y + (int)rect.getHeight() + 30;
    for (int i = 0; i < node.getChildCount(); i++) {
      drawTree(g2d, (DefaultMutableTreeNode) node.getChildAt(i), x + 20, childY);
      childY += 40;
    }
  }

  // Интерфейс для слушателей
  public interface ElementSelectionListener {
    void elementSelected(Object element);
  }
}
