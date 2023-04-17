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


  FilteringEngineSkeleton() {
    tmpFile = new File(PRIVATE_FILE_PATH);
    tmpFile.deleteOnExit();
    num = 0;
    outImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
  }


  /**
   * Given a filter, apply it to the image.
   *
   * This methode takes in account if others of filters that has been applied before by applying it to a TMP image.
   *
   * The actual computation is done by the runFilter method.
   *
   */
  @Override
  public void applyFilter(IFilter someFilter) {
    Utils.printDebug("StartApplying Filter " + num);
    Utils.printDebug("Filter Info : " + someFilter.getClass().getName() + " with margin : "
        + someFilter.getMargin());
    // generating new image from original
    if (num == 0) {
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
    Utils.printDebug("EndApplying Filter " + num);
    num++;

  }

  /**
   * Run the filter on the image. This method is abstract and must be implemented by the child class.
   *
   * You Have to take in account that the filter can have a margin and can lead to bounds errors if not bounded before.
   *
   * It is called by the applyFilter method.
   *
   * @param someFilter
   * @param inImg
   * @param outImg
   */
  public abstract void runFilter(IFilter someFilter, BufferedImage inImg, BufferedImage outImg);
  /**
   * Load an image from a file.
   *
   * @param inputImage
   * @throws Exception
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
   * @param outFile
   * @throws Exception
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
   * @param newImg
   */
  @Override
  public void setImg(BufferedImage newImg) {
    inImg = newImg;
    Utils.printDebug("New Image set : " + newImg);

  }


  /**
   * Get the image that has been filtered.
   *
   * @return
   */
  @Override
  public BufferedImage getImg() {
    reset();
    return outImg;

  }


  /**
   * Initialize the output image based on the filter that will be applied on the input.
   *
   * Can lead to bounds errors that you have to take in account.
   *
   * @param margin
   */
  protected void setOutImg(int margin) {
    outImg = new BufferedImage(inImg.getWidth() - (margin * 2),
        inImg.getHeight() - (margin * 2),
        BufferedImage.TYPE_INT_RGB);

  }

  /**
   * Reset the number of filters applied.
   *
   * Used when the input image is changed.
   *
   */
  protected void reset() {
    num = 0;
  }


}