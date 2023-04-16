import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class MultiThreadedImageFilteringEngine implements IImageFilteringEngine {

    private final String PRIVATE_FILE_PATH = "TEST_IMAGES/ENGINETPM.png";

    public final static boolean DEBUG = true;

    // reading image in
    BufferedImage inImg;
    // creating new image
    BufferedImage outImg;

    int numWorkers;

    File tmpFile;
    int num;

    ArrayList<ApplyingWorker> workers;

    MultiThreadedImageFilteringEngine(int k ) {
        numWorkers = k;
        initWorkers();

        tmpFile = new File(PRIVATE_FILE_PATH);
        tmpFile.deleteOnExit();
        num = 0;
        outImg = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
    }

    MultiThreadedImageFilteringEngine() {
        numWorkers = 1;
        initWorkers();

        tmpFile = new File(PRIVATE_FILE_PATH);
        tmpFile.deleteOnExit();
        num = 0;
        outImg = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
    }

    private void initWorkers() {
        workers = new ArrayList<ApplyingWorker>();
        for ( int i = 0; i < numWorkers; i++ ) {
            workers.add(new ApplyingWorker(i));
        }
    }

    private void setupWorkers(IFilter filter, BufferedImage outImg, BufferedImage inImg, int numWorkers) {
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

    private void startWorkers() {

        for ( ApplyingWorker w : workers ) {
            w.start();
        }


        for ( ApplyingWorker w : workers ) {

            try {
                w.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
        initWorkers();
        setupWorkers(filter, outImg_, inImg_, numWorkers);
        startWorkers();

    }

    private void setOutImg(int margin) {
        outImg = new BufferedImage(inImg.getWidth()-(margin*2),
            inImg.getHeight()-(margin*2),
            BufferedImage.TYPE_INT_RGB);

    }

    private void reset() {
        num = 0;
    }

    public static void printDebug(String msg) {
        if(DEBUG)
            System.out.println("ImageFilteringEngine_DEBUG |> " + msg);
    }


}