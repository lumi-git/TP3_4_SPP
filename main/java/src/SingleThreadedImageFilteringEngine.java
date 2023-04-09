import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SingleThreadedImageFilteringEngine implements IImageFilteringEngine {

    private final String PRIVATE_FILE_PATH = "./TEST_IMAGES/ENGINETPM.png";

    private final boolean DEBUG = true;

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
        outImg = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void loadImage(String inputImage) throws Exception {
        inImg  = ImageIO.read(new File(inputImage));
        printDebug("Image loaded from : " + inputImage);
        reset();
    }

    @Override
    public void writeOutPngImage(String outFile) throws Exception {
        File f = new File(outFile);
        ImageIO.write(outImg, "png", f);
        printDebug("Output image written at : " + outFile);
    }

    @Override
    public void setImg(BufferedImage newImg) {
        inImg = newImg;
        printDebug("New Image set : " + newImg);

    }


    @Override
    public BufferedImage getImg() {
        reset();
        return outImg;

    }

    @Override
    public void applyFilter(IFilter someFilter) {
        printDebug("StartApplying Filter " + num );
        printDebug("Filter Info : " + someFilter.getClass().getName() + " with margin : " + someFilter.getMargin());
        // generating new image from original
        if ( num == 0 ) {
            setOutImg(someFilter.getMargin());
            runFilter(someFilter, inImg, outImg);
            try{
                writeOutPngImage(tmpFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            try{
                inImg  = ImageIO.read(tmpFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setOutImg(someFilter.getMargin());
            runFilter(someFilter, inImg, outImg);
            try{
                writeOutPngImage(tmpFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        printDebug("EndApplying Filter " + num );
        num++;

    }

    private void runFilter(IFilter filter,BufferedImage inImg_, BufferedImage outImg_){
        int max_X = inImg_.getWidth() - ((filter.getMargin())) ;
        int max_Y = inImg_.getHeight() - ((filter.getMargin())) ;
        int min_X = filter.getMargin();
        int min_Y = filter.getMargin();

        // generating new image from original
        for (int x = min_X; x < max_X; x++) {
            for (int y = min_Y; y < max_Y; y++) {
                filter.applyFilterAtPoint(x, y, inImg_, outImg_);
            } // EndFor y
        } // EndFor x

    }

    private void setOutImg(int margin) {
        outImg = new BufferedImage(inImg.getWidth()-(margin*2),
                inImg.getHeight()-(margin*2),
                BufferedImage.TYPE_INT_RGB);

    }

    private void reset() {
        num = 0;
    }

    private void printDebug(String msg) {
        if(DEBUG)
            System.out.println("ImageFilteringEngine_DEBUG |> " + msg);
    }


}