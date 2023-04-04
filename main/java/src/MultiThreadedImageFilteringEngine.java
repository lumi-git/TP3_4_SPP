import java.awt.image.BufferedImage;

public class MultiThreadedImageFilteringEngine implements IImageFilteringEngine {
    int k;
    MultiThreadedImageFilteringEngine(int nbFred) {
        k= nbFred;
    }

    @Override
    public void loadImage(String inputImage) throws Exception {

    }

    @Override
    public void writeOutPngImage(String outFile) throws Exception {

    }

    @Override
    public void setImg(BufferedImage newImg) {

    }

    @Override
    public BufferedImage getImg() {
        return null;
    }

    @Override
    public void applyFilter(IFilter someFilter) {

    }
}
