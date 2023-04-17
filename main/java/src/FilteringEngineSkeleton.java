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

  public abstract void runFilter(IFilter someFilter, BufferedImage inImg, BufferedImage outImg);

  @Override
  public void loadImage(String inputImage) throws Exception {
    inImg = ImageIO.read(new File(inputImage));
    Utils.printDebug("Image loaded from : " + inputImage);
    reset();
  }

  @Override
  public void writeOutPngImage(String outFile) throws Exception {
    File f = new File(outFile);
    ImageIO.write(outImg, "png", f);
    Utils.printDebug("Output image written at : " + outFile);
  }

  @Override
  public void setImg(BufferedImage newImg) {
    inImg = newImg;
    Utils.printDebug("New Image set : " + newImg);

  }


  @Override
  public BufferedImage getImg() {
    reset();
    return outImg;

  }


  protected void setOutImg(int margin) {
    outImg = new BufferedImage(inImg.getWidth() - (margin * 2),
        inImg.getHeight() - (margin * 2),
        BufferedImage.TYPE_INT_RGB);

  }

  protected void reset() {
    num = 0;
  }


}