import java.awt.image.BufferedImage;

public class SingleThreadedImageFilteringEngine extends FilteringEngineSkeleton {


  SingleThreadedImageFilteringEngine() {
    super();
  }

  /**
   * This method applies the filter to the image by processing each pixel by the filter given
   * @param filter The filter to apply
   * @param inImg_ The input image
   * @param outImg_ The output image
   */
  @Override
  public void runFilter(IFilter filter, BufferedImage inImg_, BufferedImage outImg_) {

    //this part is here to be sure the output image is in the bounds
    int max_X = inImg_.getWidth() - ((filter.getMargin()));
    int max_Y = inImg_.getHeight() - ((filter.getMargin()));
    int min_X = filter.getMargin();
    int min_Y = filter.getMargin();

    // generating new image from original
    for (int x = min_X; x < max_X; x++) {
      for (int y = min_Y; y < max_Y; y++) {
        filter.applyFilterAtPoint(x, y, inImg_, outImg_);
      } // EndFor y
    } // EndFor x

  }


}