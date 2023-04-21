import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class MultiThreadedImageFilteringEngine extends FilteringEngineSkeleton {


  public static CyclicBarrier barrier;


  Runnable barrierAction = () -> {
      System.out.println("Barrier reached");
  };

  int numWorkers;
  ArrayList<ApplyingWorker> workers;

  MultiThreadedImageFilteringEngine(int k) {
    super();
    numWorkers = k;
    barrier = new CyclicBarrier(numWorkers,barrierAction);
    initWorkers();
  }

  MultiThreadedImageFilteringEngine() {
    super();

    numWorkers = 1;
    barrier = new CyclicBarrier(numWorkers,barrierAction);
    initWorkers();
  }

  /**
   *
   * Create a new worker based on the number wanted
   *
   */
  private void initWorkers() {
    barrier = new CyclicBarrier(numWorkers,barrierAction);
    workers = new ArrayList<ApplyingWorker>();
    for (int i = 0; i < numWorkers; i++) {
      workers.add(new ApplyingWorker(i,barrier));
    }
  }

  /**
   *
   * Set up the workers with the filter, the input image, the output image and the number of workers
   *
   * Based on the number of workers needed, each worker will be assigned a different part of the image to filter
   *
   * @param filter The filter to apply
   * @param outImg The output image
   * @param inImg The input image
   * @param numWorkers The number of workers
   */
  private void setupWorkers(IFilter filter, BufferedImage outImg, BufferedImage inImg,
      int numWorkers) {
    int imgWidth = inImg.getWidth();
    int imgHeight = inImg.getHeight();
    int chunkWidth = imgWidth / numWorkers;
    int remainder = imgWidth % numWorkers; // Handling remainder chunks

    int startX = 0; // Starting x-coordinate of each chunk
    int startY = 0; // Starting y-coordinate of each chunk

    for (ApplyingWorker w : workers) {
      // Set the filter, input image, output image, and starting coordinates for each worker
      w.setFilter(filter);
      w.setImgIn(inImg);
      w.setImgOut(outImg);
      w.setStartX(startX);
      w.setStartY(startY);

      // Calculate the ending x-coordinate of the chunk
      int endX = startX + chunkWidth + (remainder > 0 ? 1 : 0);
      // Adjust the ending x-coordinate to ensure that the chunk covers enough pixels to reach the image boundary
      endX = Math.min(endX, imgWidth);

      // Calculate the ending y-coordinate of the chunk
      int endY = startY + imgHeight;

      // Update starting x-coordinate for the next chunk
      startX = endX;

      // Update remainder chunks
      remainder--;

      // If the chunk extends beyond the image width, reset the starting x-coordinate and update the y-coordinate
      if (startX >= imgWidth) {
        startX = 0;
        startY += imgHeight;
      }

      // Set the ending x-coordinate and y-coordinate for each worker
      w.setEndX(endX);
      w.setEndY(endY);
    }
  }

  /**
   *
   * Start the workers and wait for all of them to finish
   *
   */
  private void startWorkers() {
    for (ApplyingWorker w : workers) {
      w.start();
    }

    for (ApplyingWorker w : workers) {
      try {
        w.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }



  @Override
  public void runFilter(IFilter filter, BufferedImage inImg_, BufferedImage outImg_) {
    initWorkers();
    setupWorkers(filter, outImg_, inImg_, numWorkers);
    startWorkers();

  }


}