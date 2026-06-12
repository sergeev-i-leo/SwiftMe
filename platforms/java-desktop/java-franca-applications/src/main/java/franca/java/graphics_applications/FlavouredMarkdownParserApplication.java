package franca.java.graphics_applications;

import javax.swing.*;

import franca.java.graphics.GraphicsApplication;
import franca.java.graphics.GraphicsRouter;
import franca.java.graphics.views.Page;

import franca.java.test_components.FlavouredMarkdownParserView;

public class FlavouredMarkdownParserApplication {

  public static void main(String[] args) {
    GraphicsRouter graphicsRouter = new GraphicsRouter();
    Page page = new Page(graphicsRouter);
    page.views.add(new FlavouredMarkdownParserView());

    graphicsRouter.pushPage(page);

    JFrame frame = new JFrame("GraphicsTestApplication");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    GraphicsApplication graphicsApplication = new GraphicsApplication(graphicsRouter);
    frame.add(graphicsApplication);

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
