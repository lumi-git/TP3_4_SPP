import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public abstract class FilteringEngineSkeleton implements IImageFilteringEngine {

    protected final String PRIVATE_FILE_PATH = "TEST_IMAGES/ENGINETPM.png";


    // reading image in
    BufferedImage inImg;
    // creating new image
    BufferedImage outImg;
    File tmpFile;
    int num;

    double ExeTime;


    FilteringEngineSkeleton() {
        tmpFile = new File(PRIVATE_FILE_PATH);
        tmpFile.deleteOnExit();
        num = 1;
        outImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        ExeTime = 0;
    }


    /**
     * Given a filter, apply it to the image.
     * <p>
     * This methode takes in account if others of filters that has been applied before by applying it to a TMP image.
     * <p>
     * The actual computation is done by the runFilter method.
     */
    @Override
    public void applyFilter(IFilter someFilter) {

        Utils.printDebug("StartApplying Filter " + num);
        Utils.printDebug("Filter Info : " + someFilter.getClass().getName() + " with margin : "
                + someFilter.getMargin());
        // generating new image from original
        ExeTime -= System.currentTimeMillis()/1000.0;
        if (num == 1) {
            setOutImg(someFilter.getMargin());
            runFilter(someFilter, inImg, outImg);
            try {
                writeOutPngImage(tmpFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                inImg = ImageIO.read(tmpFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setOutImg(someFilter.getMargin());
            runFilter(someFilter, inImg, outImg);
            try {
                writeOutPngImage(tmpFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ExeTime += System.currentTimeMillis()/1000.0;
        Utils.printDebug("EndApplying Filter " + num);
        num++;


    }

    /**
     * Run the filter on the image. This method is abstract and must be implemented by the child class.
     * <p>
     * You Have to take in account that the filter can have a margin and can lead to bounds errors if not bounded before.
     * <p>
     * It is called by the applyFilter method.
     *
     * @param someFilter filter to apply to the image
     * @param inImg      image to apply the filter on
     * @param outImg     image to write the result of the filter
     */
    public abstract void runFilter(IFilter someFilter, BufferedImage inImg, BufferedImage outImg);

    /**
     * Load an image from a file.
     *
     * @param inputImage path to the image
     * @throws Exception if the image is not found
     */
    @Override
    public void loadImage(String inputImage) throws Exception {
        inImg = ImageIO.read(new File(inputImage));
        Utils.printDebug("Image loaded from : " + inputImage);
        reset();
    }

    /**
     * Write the image to a file.
     *
     * @param outFile path to the output file
     * @throws Exception if the file is not found
     */
    @Override
    public void writeOutPngImage(String outFile) throws Exception {
        File f = new File(outFile);
        ImageIO.write(outImg, "png", f);
        Utils.printDebug("Output image written at : " + outFile);
    }


    /**
     * Set the image to be filtered.
     *
     * @param newImg new image to be filtered
     */
    @Override
    public void setImg(BufferedImage newImg) {
        inImg = newImg;
        Utils.printDebug("New Image set : " + newImg);

    }


    /**
     * Get the image that has been filtered.
     *
     * @return the image that has been filtered
     */
    @Override
    public BufferedImage getImg() {
        reset();
        return outImg;

    }


    /**
     * Initialize the output image based on the filter that will be applied on the input.
     * <p>
     * Can lead to bounds errors that you have to take in account.
     *
     * @param margin margin of the filter
     */
    protected void setOutImg(int margin) {
        outImg = new BufferedImage(inImg.getWidth() - (margin * 2),
                inImg.getHeight() - (margin * 2),
                BufferedImage.TYPE_INT_RGB);

    }

    /**
     * Reset the number of filters applied.
     * <p>
     * Used when the input image is changed.
     */
    protected void reset() {
        num = 1;
        ExeTime = 0;
    }

    public double getExeTime() {
        return ExeTime;
    }

}