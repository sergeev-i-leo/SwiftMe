package franca.java.graphics.test_components;

import franca.java.core.contracted.ContractedStringBuffer;
import franca.java.graphics.views.Page;
import franca.java.graphics.views.View;
import franca.java.core.data.html.HtmlParser;
import franca.java.core.data.json.JsonArray;
import franca.java.graphics.device.Device;
import franca.java.graphics.painter.Painter;

public class HtmlParserView extends View {

  private int state = 0;
  private JsonArray jsonArray = null;
  private String htmlOutput = null;

  @Override
  public void paint(Device device, Painter painter, Page page) {

    switch (state) {
      case 0:
        painter.paintText("WAITING", 100f, 100f, "", 255);
        break;
      case 1:
        painter.paintText("READING", 100f, 100f, "", 255);
        break;
      case 2:
        painter.paintText("WRITING", 100f, 100f, "", 255);
        break;
      case 200:
        painter.paintText("200", 100f, 100f, "", 255);
        break;
      case 404:
        painter.paintText("404", 100f, 100f, "", 255);
        break;
      default:
        painter.paintText("500", 100f, 100f, "", 255);
        break;
    }
  }

  @Override
  public void handlePointerDown(Device device, Page page, float painterX, float painterY, float pointedX, float pointedY, int buttonNumber) {
    if (state == 0) {
      state = 1;
      device.requestRepainting();
      device.readFile("test.html", result -> {
        if (result != null) {
          HtmlParser htmlParser = new HtmlParser();
          // look for debugging level in input
          htmlParser.debuggingLevel = 1;
          jsonArray = htmlParser.parse(result);
          delete(htmlParser);
          ContractedStringBuffer contractedStringBuffer = new ContractedStringBuffer();
          htmlParser.toStringBuffer(jsonArray, contractedStringBuffer);
          htmlOutput = contractedStringBuffer.getString();
          delete(contractedStringBuffer);
          device.writeFile("test-output.html", htmlOutput, operationResult -> {
            if ((operationResult != null) && (operationResult == 200)) {
              state = 200;
            } else {
              state = 404;
            }
            device.requestRepainting();
          });
        } else {
          state = 404;
        }
        device.requestRepainting();
      });
    }
    if (state == 200) {
      if (jsonArray != null) {
        state = 2;
        device.requestRepainting();
        ContractedStringBuffer contractedStringBuffer = new ContractedStringBuffer();
        jsonArray.serialize(contractedStringBuffer, null);
        device.writeFile("html-0.tmp", contractedStringBuffer.toString(), operationResult -> {
          if ((operationResult != null) && (operationResult == 200)) {
            state = 0;
          } else {
            state = 404;
          }
          device.requestRepainting();
        });
      }
    }
  }
}
