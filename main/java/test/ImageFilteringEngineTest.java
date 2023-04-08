import org.junit.Test;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ImageFilteringEngineTest {

    private IImageFilteringEngine engine = new SingleThreadedImageFilteringEngine(); // Replace 'MyImageFilteringEngine' with your actual implementation

    @Test
    public void testGrayLevelFilter() throws IOException {
        // TODO: Add test cases for white, black, red, green, blue rectangles
        // and provided images (FourCircles.png and 15226222451_5fd668d81a_c.jpg)

        // Example test case:
        BufferedImage inputImage = ImageIO.read(new File("path/to/input/image.png"));
        BufferedImage expectedOutputImage = ImageIO.read(new File("path/to/expected/output/image.png"));

        engine.setImg(inputImage);
        engine.applyFilter(new GrayLevelFilter());
        BufferedImage actualOutputImage = engine.getImg();
        assertImagesEqual(expectedOutputImage, actualOutputImage);
    }

    @Test
    public void testGaussianContourExtractorFilter() throws IOException {
        // TODO: Add test cases for white, black, red, green, blue rectangles
        // and provided images (FourCircles.png and 15226222451_5fd668d81a_c.jpg)

        // Example test case:
        BufferedImage inputImage = ImageIO.read(new File("path/to/input/image.png"));
        BufferedImage expectedOutputImage = ImageIO.read(new File("path/to/expected/output/image.png"));
        engine.setImg(inputImage);
        engine.applyFilter(new GaussianContourExtractorFilter());
        BufferedImage actualOutputImage = engine.getImg();

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