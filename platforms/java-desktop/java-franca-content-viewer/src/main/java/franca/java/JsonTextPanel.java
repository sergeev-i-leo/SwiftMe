package franca.java;

import javax.swing.*;
import java.awt.*;

public class JsonTextPanel extends JPanel {

  private JTextArea textArea;

  public JsonTextPanel() {
    setLayout(new BorderLayout());
    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setFont(FontLoader.getOswaldBold(14));
    textArea.setWrapStyleWord(false);
    textArea.setLineWrap(false);
    add(new JScrollPane(textArea), BorderLayout.CENTER);
  }

  public void setJsonText(String jsonText) {
    textArea.setText(jsonText);
    textArea.setCaretPosition(0);
  }
}
