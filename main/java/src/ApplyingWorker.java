import java.awt.image.BufferedImage;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

/**
 * This class is a thread that will apply a filter to a chunk of the image
 * <p>
 * It will be used by the MultiThreadedImageFilteringEngine to apply the filter to the image
 *
 * @author Ronan tremoureux
 */
public class ApplyingWorker extends Thread {


    private CyclicBarrier startBarrier;
    private CyclicBarrier endBarrier;


    /**
     * The start of the x coordinate of the chunk to process
     */
    private int startX;

    /**
     * The start of the y coordinate of the chunk to process
     */
    private int startY;

    /**
     * The end of the x coordinate of the chunk to process
     */
    private int endX;

    /**
     * The end of the y coordinate of the chunk to process
     */
    private int endY;

    /**
     * The image to read the pixels from
     */

    private BufferedImage imgIn;
    /**
     * The image to write the result to
     */
    private BufferedImage imgOut;

    /**
     * The filter to apply
     */
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

    ApplyingWorker(int id, CyclicBarrier startBarrier, CyclicBarrier endBarrier) {
        super("ApplyingWorker-" + id);
        this.startBarrier = startBarrier;
        this.endBarrier = endBarrier;
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

    /**
     * The run method of the thread
     * <p>
     * Check if the range is out of the bound, make it fit
     * <p>
     * Generate new image from original
     * <p>
     * This methode may use the same ressources as the other threads.
     */
    @Override
    public void run() {

        try {
            startBarrier.await(); // Wait for all threads to be ready to start processing
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

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

        // generating new image from original by applying the filter
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                filter.applyFilterAtPoint(x, y, imgIn, imgOut);
            } // EndFor y
        } // EndFor x

        //prints a message when finished
        Utils.printDebug(
                "Thread " + this.getName() + " (" + this.startX + " ; " + this.startY + ") -> (" + this.endX
                        + " ; " + this.endY + ") done");

        Utils.printDebug("Thread " + this.getName() + " waiting for others at the barrier");
        try {
            endBarrier.await(); // Signal that this thread has finished processing
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        Utils.printDebug("Thread " + this.getName() + " passed the barrier");
    }

}
