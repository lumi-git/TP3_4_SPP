import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.Test;

public class MultiThreadedFilteringEngineTest {

  private IImageFilteringEngine engine = new MultiThreadedImageFilteringEngine(
      10); // Replace 'MyImageFilteringEngine' with your actual implementation

  @Test
  public void testGaussianContourExtractorFilter() throws IOException {

    BufferedImage inputImage = ImageIO.read(new File("./TEST_IMAGES/FourCircles.png"));
    BufferedImage expectedOutputImage = ImageIO.read(
        new File("./TEST_IMAGES/FourCircles_gaussian_contour.png"));

    engine.setImg(inputImage);
    engine.applyFilter(new GrayLevelFilter());
    engine.applyFilter(new GaussianContourExtractorFilter());

    try {
      engine.writeOutPngImage("./TEST_IMAGES/FourCircles_gaussian_contour_TEST.png");
    } catch (Exception e) {
      e.printStackTrace();
    }

    BufferedImage actualOutputImage = ImageIO.read(
        new File("./TEST_IMAGES/FourCircles_gaussian_contour_TEST.png"));

    assertEquals("the width of the images are not the same", expectedOutputImage.getWidth(),
        actualOutputImage.getWidth());

    assertEquals("the height of the images are not the same", expectedOutputImage.getHeight(),
        actualOutputImage.getHeight());

    assertImagesEqual_Pixels(expectedOutputImage, actualOutputImage);
  }

  @Test
  public void TestSizeAfterGaussianContour() throws IOException {
    BufferedImage inputImage = ImageIO.read(new File("./TEST_IMAGES/FourCircles.png"));
    BufferedImage expectedOutputImage = ImageIO.read(
        new File("./TEST_IMAGES/FourCircles_gaussian_contour.png"));

    engine.setImg(inputImage);
    engine.applyFilter(new GrayLevelFilter());
    engine.applyFilter(new GaussianContourExtractorFilter());

    try {
      engine.writeOutPngImage("./TEST_IMAGES/FourCircles_gaussian_contour_TEST.png");
    } catch (Exception e) {
      e.printStackTrace();
    }

    BufferedImage actualOutputImage = ImageIO.read(
        new File("./TEST_IMAGES/FourCircles_gaussian_contour_TEST.png"));

    assertEquals("the width of the images are not the same", expectedOutputImage.getWidth(),
        actualOutputImage.getWidth());

    assertEquals("the height of the images are not the same", expectedOutputImage.getHeight(),
        actualOutputImage.getHeight());

  }

  @Test
  public void testGrayLevelFilter() throws IOException {

    BufferedImage inputImage = ImageIO.read(new File("./TEST_IMAGES/FourCircles.png"));
    BufferedImage expectedOutputImage = ImageIO.read(
        new File("./TEST_IMAGES/FourCircles_gray.png"));

    engine.setImg(inputImage);
    engine.applyFilter(new GrayLevelFilter());

    try {
      engine.writeOutPngImage("./TEST_IMAGES/FourCircles_gray_TEST.png");
    } catch (Exception e) {
      e.printStackTrace();
    }

    BufferedImage actualOutputImage = ImageIO.read(
        new File("./TEST_IMAGES/FourCircles_gray_TEST.png"));

    assertImagesEqual_Pixels(expectedOutputImage, actualOutputImage);
  }

  private void assertImagesEqual_Pixels(BufferedImage expected, BufferedImage actual) {

    for (int x = 0; x < expected.getWidth(); x++) {
      for (int y = 0; y < expected.getHeight(); y++) {
        assertEquals("Bug on pixels : " + x + " " + y, expected.getRGB(x, y), actual.getRGB(x, y));
      }
    }
  }
}
