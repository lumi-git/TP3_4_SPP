import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ApplyingWorker extends Thread {

  static Lock lock = new ReentrantLock();

  private int startX;
  private int startY;

  private int endX;
  private int endY;

  private BufferedImage imgIn;
  private BufferedImage imgOut;
  private IFilter filter;

  public ApplyingWorker(int startX, int startY, int endX, int endY, BufferedImage imgIn,
      BufferedImage imgOut, IFilter filter, int id) {
    super("ApplyingWorker " + id);
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;
    this.imgIn = imgIn;
    this.imgOut = imgOut;
    this.filter = filter;
  }

  public ApplyingWorker(int id) {
    super("ApplyingWorker " + id);
  }


  public void setFilter(IFilter filter) {
    this.filter = filter;
  }

  public void setImgIn(BufferedImage imgIn) {
    this.imgIn = imgIn;
  }

  public void setImgOut(BufferedImage imgOut) {
    this.imgOut = imgOut;
  }

  public void setStartX(int x) {
    this.startX = x;
  }

  public void setStartY(int y) {
    this.startY = y;
  }

  public void setEndX(int endX) {
    this.endX = endX;
  }

  public void setEndY(int endY) {
    this.endY = endY;
  }

  @Override
  public void run() {

    int max_X = imgIn.getWidth() - ((filter.getMargin()));
    int max_Y = imgIn.getHeight() - ((filter.getMargin()));
    int min_X = filter.getMargin();
    int min_Y = filter.getMargin();

    //if the range is out of the bound, make it fit
    if (startX < min_X) {
      startX = min_X;
    }
    if (startY < min_Y) {
      startY = min_Y;
    }
    if (endX > max_X) {
      endX = max_X;
    }
    if (endY > max_Y) {
      endY = max_Y;
    }

    // generating new image from original
    for (int x = startX; x < endX; x++) {
      for (int y = startY; y < endY; y++) {
        //lock.lock();
        filter.applyFilterAtPoint(x, y, imgIn, imgOut);
        //lock.unlock();
      } // EndFor y
    } // EndFor x
    Utils.printDebug(
        "Thread " + this.getName() + " (" + this.startX + " ; " + this.startY + ") -> (" + this.endX
            + " ; " + this.endY + ") done");
  }

}
