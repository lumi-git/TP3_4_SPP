import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SingleThreadedImageFilteringEngine implements IImageFilteringEngine {

    private final String PRIVATE_FILE_PATH = "./TEST_IMAGES/ENGINETPM.png";
    // reading image in
    BufferedImage inImg;
    // creating new image
    BufferedImage outImg;

    File tmpFile;
    int num;

    SingleThreadedImageFilteringEngine() {
        tmpFile = new File(PRIVATE_FILE_PATH);
        tmpFile.deleteOnExit();
        num = 0;
    }

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
        if ( num == 0 ) {
            runFilter(someFilter, inImg, outImg);
            try{
                writeOutPngImage(tmpFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try{
                loadImage(tmpFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            runFilter(someFilter, inImg, outImg);
            try{
                writeOutPngImage(tmpFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    private void runFilter(IFilter filter,BufferedImage inImg_, BufferedImage outImg_){
        // generating new image from original
        for (int x = 0; x < inImg_.getWidth(); x++) {
            for (int y = 0; y < inImg_.getHeight(); y++) {
                filter.applyFilterAtPoint(x, y, inImg_, outImg_);
            } // EndFor y
        } // EndFor x

    }

    private void setOutImg() {
        outImg = new BufferedImage(inImg.getWidth(),
                inImg.getHeight(),
                BufferedImage.TYPE_INT_RGB);
    }

}
