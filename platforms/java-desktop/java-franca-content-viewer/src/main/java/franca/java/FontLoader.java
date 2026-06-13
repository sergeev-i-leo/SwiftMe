package franca.java;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {

  private static Font oswaldBold;

  static {
    loadFonts();
  }

  private static void loadFonts() {
    try (InputStream is = FontLoader.class.getResourceAsStream("/fonts/oswald_bold.ttf")) {
      if (is != null) {
        oswaldBold = Font.createFont(Font.TRUETYPE_FONT, is);
        oswaldBold = oswaldBold.deriveFont(14f);  // базовый размер 14
      } else {
        System.err.println("Font not found, using fallback");
        oswaldBold = new Font("Dialog", Font.PLAIN, 14);
      }
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
      oswaldBold = new Font("Dialog", Font.PLAIN, 14);
    }
  }

  public static Font getOswaldBold() {
    return oswaldBold;
  }

  public static Font getOswaldBold(float size) {
    return oswaldBold.deriveFont(size);
  }
}
