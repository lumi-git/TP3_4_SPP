import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SingleThreadedImageFilteringEngine implements IImageFilteringEngine {

    // reading image in
    BufferedImage inImg;
    // creating new image
    BufferedImage outImg;


    @Override
    public void loadImage(String inputImage) throws Exception {
        inImg  = ImageIO.read(new File(inputImage));
        setOutImg();
    }

    @Override
    public void writeOutPngImage(String outFile) throws Exception {
        File f = new File(outFile);
        ImageIO.write(outImg, "png", f);
    }

    @Override
    public void setImg(BufferedImage newImg) {
        inImg = newImg;
        setOutImg();
    }




    @Override
    public BufferedImage getImg() {
        return outImg;
    }

    @Override
    public void applyFilter(IFilter someFilter) {
        // generating new image from original
        for (int x = 0; x < inImg.getWidth(); x++) {
            for (int y = 0; y < inImg.getHeight(); y++) {
                someFilter.applyFilterAtPoint(x, y, inImg, outImg);
            } // EndFor y
        } // EndFor x

    }

    private void setOutImg() {
        outImg = new BufferedImage(inImg.getWidth(),
                inImg.getHeight(),
                BufferedImage.TYPE_INT_RGB);
    }
}
