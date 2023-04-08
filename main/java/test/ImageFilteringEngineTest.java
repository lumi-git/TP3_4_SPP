import org.junit.Test;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ImageFilteringEngineTest {

    private IImageFilteringEngine engine = new SingleThreadedImageFilteringEngine(); // Replace 'MyImageFilteringEngine' with your actual implementation

    @Test
    public void  testGaussianContourExtractorFilter() throws IOException {
        // TODO: Add test cases for white, black, red, green, blue rectangles
        // and provided images (FourCircles.png and 15226222451_5fd668d81a_c.jpg)

        // Example test case:
        BufferedImage inputImage = ImageIO.read(new File("./TEST_IMAGES/FourCircles.png"));
        BufferedImage expectedOutputImage = ImageIO.read(new File("./TEST_IMAGES/FourCircles_gaussian_contour.png"));

        engine.setImg(inputImage);
        engine.applyFilter(new GrayLevelFilter());
        engine.applyFilter(new GaussianContourExtractorFilter());

        try {
            engine.writeOutPngImage("./TEST_IMAGES/FourCircles_gaussian_contour_TEST.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedImage actualOutputImage = ImageIO.read(new File("./TEST_IMAGES/FourCircles_gaussian_contour_TEST.png"));
        assertImagesEqual(expectedOutputImage, actualOutputImage);
    }

    @Test
    public void testGrayLevelFilter() throws IOException {
        // TODO: Add test cases for white, black, red, green, blue rectangles
        // and provided images (FourCircles.png and 15226222451_5fd668d81a_c.jpg)

        // Example test case:
        BufferedImage inputImage = ImageIO.read(new File("./TEST_IMAGES/FourCircles.png"));
        BufferedImage expectedOutputImage = ImageIO.read(new File("./TEST_IMAGES/FourCircles_gray.png"));

        engine.setImg(inputImage);
        engine.applyFilter(new GrayLevelFilter());



        try {
            engine.writeOutPngImage("./TEST_IMAGES/FourCircles_gray_TEST.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedImage actualOutputImage = ImageIO.read(new File("./TEST_IMAGES/FourCircles_gray_TEST.png"));



        assertImagesEqual(expectedOutputImage, actualOutputImage);
    }

    private void assertImagesEqual(BufferedImage expected, BufferedImage actual) {
        assertEquals(expected.getWidth(), actual.getWidth());
        assertEquals(expected.getHeight(), actual.getHeight());

        for (int x = 0; x < expected.getWidth(); x++) {
            for (int y = 0; y < expected.getHeight(); y++) {
                assertEquals(expected.getRGB(x, y), actual.getRGB(x, y));
            }
        }
    }
}
