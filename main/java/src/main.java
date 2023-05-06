/**
 * SimpleImageProcessingExample.java
 * <p>
 * Time-stamp: <2017-03-09 22:26:51 ftaiani>
 *
 * @author Francois Taiani   <francois.taiani@irisa.fr> $Id$
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

/**
 * @author Francois Taiani   <francois.taiani@irisa.fr>
 */
public class main {

    static public void main(String[] args) {

        //for a silent test, set the debug to false
        SETTINGS.DEBUG = false;

        //Graphs1();
        Graphs2();


    }

    public static void Graphs1() {
        int n = 10;
        IFilter[] filters = {new GrayLevelFilter(), new GaussianContourExtractorFilter()};

        ArrayList<Double> originalSeqTimes1 = new ArrayList<Double>();
        ArrayList<Double> multiThreadTimes1 = new ArrayList<Double>();

        double SingleTrheadedTime1 = TimePerformFiltersOnImageWithName("./TEST_IMAGES/15226222451_75d515f540_o.jpg", new SingleThreadedImageFilteringEngine(), filters[0]);


        for (int i = 1; i <= n; i++) {
            double multiThreadTime = TimePerformFiltersOnImageWithName("./TEST_IMAGES/15226222451_75d515f540_o.jpg", new MultiThreadedImageFilteringEngine(i), filters[0]);
            originalSeqTimes1.add(SingleTrheadedTime1);
            multiThreadTimes1.add(multiThreadTime);
            System.out.println("GrayFilter Done with " + i + " threads");
        }

        // Measurements for filter 2 (GaussianContourExtractorFilter)
        ArrayList<Double> originalSeqTimes2 = new ArrayList<Double>();
        ArrayList<Double> multiThreadTimes2 = new ArrayList<Double>();

        double SingleTrheadedTime2 = TimePerformFiltersOnImageWithName("./TEST_IMAGES/15226222451_75d515f540_o.jpg", new SingleThreadedImageFilteringEngine(), filters[1]);

        for (int i = 1; i <= n; i++) {

            double multiThreadTime = TimePerformFiltersOnImageWithName("./TEST_IMAGES/15226222451_75d515f540_o.jpg", new MultiThreadedImageFilteringEngine(i), filters[1]);
            originalSeqTimes2.add(SingleTrheadedTime2);
            multiThreadTimes2.add(multiThreadTime);
            System.out.println("GaussFilter Done with " + i + " threads");
        }


        System.out.println("Times multi : " + multiThreadTimes1);
        System.out.println("Times single : " + originalSeqTimes1);
        System.out.println("Times multi : " + multiThreadTimes2);
        System.out.println("Times single : " + originalSeqTimes2);


        Utils.plotGraph("GrayLevelFilter : Performance", originalSeqTimes1, multiThreadTimes1);
        Utils.plotGraph("GaussianContourExtractorFilter : Performance", originalSeqTimes2, multiThreadTimes2);
    }

    public static void Graphs2() {
        int n = 4;
        IFilter[] filters = {new GrayLevelFilter(), new GaussianContourExtractorFilter()};

        String imageFolderPath = "./TEST_IMAGES/";
        File imageFolder = new File(imageFolderPath);
        String regex = "15226222451_.*\\.jpg";
        Pattern pattern = Pattern.compile(regex);

        ArrayList<String> imageFiles = new ArrayList<>();
        for (File file : imageFolder.listFiles()) {
            if (pattern.matcher(file.getName()).matches()) {
                System.out.println("Found image file: " + file.getName());
                imageFiles.add(file.getPath());
            }
        }

        ArrayList<Double> originalSeqTimes1 = new ArrayList<Double>();
        ArrayList<Double> multiThreadTimes1 = new ArrayList<Double>();




        for (String imageFile : imageFiles) {
            double SingleTrheadedTime = TimePerformFiltersOnImageWithName(imageFile, new SingleThreadedImageFilteringEngine(), filters[0]);
            System.out.println("GrayFilter Done with " + imageFile + " with 1 threads");
            double multiThreadTime = TimePerformFiltersOnImageWithName(imageFile, new MultiThreadedImageFilteringEngine(n), filters[0]);
            System.out.println("GrayFilter Done with " + imageFile + " with " + n + " threads");
            originalSeqTimes1.add(SingleTrheadedTime);
            multiThreadTimes1.add(multiThreadTime);


        }

        ArrayList<Double> originalSeqTimes2 = new ArrayList<Double>();
        ArrayList<Double> multiThreadTimes2 = new ArrayList<Double>();


        for (String imageFile : imageFiles) {
            double SingleTrheadedTime = TimePerformFiltersOnImageWithName(imageFile, new SingleThreadedImageFilteringEngine(), filters[1]);
            System.out.println("GaussFilter Done with " + imageFile + " with 1 threads");
            double multiThreadTime = TimePerformFiltersOnImageWithName(imageFile, new MultiThreadedImageFilteringEngine(n), filters[1]);
            System.out.println("GaussFilter Done with " + imageFile + " with " + n + " threads");
            originalSeqTimes2.add(SingleTrheadedTime);
            multiThreadTimes2.add(multiThreadTime);
        }


        ArrayList<Integer> ImageSizes = new ArrayList<Integer>();

        for (String imageFile : imageFiles) {
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File(imageFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageSizes.add(img.getWidth() * img.getHeight());
        }

        System.out.println("Times multi : " + multiThreadTimes1);
        System.out.println("Times single : " + originalSeqTimes1);
        System.out.println("Times multi : " + multiThreadTimes2);
        System.out.println("Times single : " + originalSeqTimes2);

        Utils.plotGraphWithImageSize("GrayLevelFilter : Performance", ImageSizes ,originalSeqTimes1, multiThreadTimes1);
        Utils.plotGraphWithImageSize("GaussianContourExtractorFilter : Performance", ImageSizes ,originalSeqTimes2, multiThreadTimes2);
    }


    public static double TimePerformFiltersOnImageWithName(String imageName, FilteringEngineSkeleton engine, IFilter filter) {
        double time = 0;
        try {
            engine.loadImage(imageName);
            engine.applyFilter(filter);
            time = engine.getExeTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }


    public static void ex2() {

        try {

            IImageFilteringEngine im = new MultiThreadedImageFilteringEngine(2);
            im.loadImage("./TEST_IMAGES/15226222451_5fd668d81a_c.jpg");
            im.applyFilter(new GrayLevelFilter());
            im.applyFilter(new GaussianContourExtractorFilter());
            im.writeOutPngImage("./TEST_IMAGES/15226222451_5fd668d81a_c_gaussian_contour.png");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void ex2_Circles() {
        try {

            IImageFilteringEngine im = new MultiThreadedImageFilteringEngine(10);
            im.loadImage("./TEST_IMAGES/FourCircles.png");

            im.applyFilter(new GrayLevelFilter());
            im.applyFilter(new GaussianContourExtractorFilter());

            im.writeOutPngImage("./TEST_IMAGES/FourCircles_gaussian_contour_TEST.png");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ex1() {
        try {

            IImageFilteringEngine im = new SingleThreadedImageFilteringEngine();
            im.loadImage("./TEST_IMAGES/15226222451_5fd668d81a_c.jpg");
            im.applyFilter(new GrayLevelFilter());
            im.applyFilter(new GaussianContourExtractorFilter());
            im.writeOutPngImage("./TEST_IMAGES/15226222451_5fd668d81a_c_gaussian_contour.png");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ex1OnCircles() {
        try {

            IImageFilteringEngine im = new SingleThreadedImageFilteringEngine();
            im.loadImage("./TEST_IMAGES/FourCircles.png");

            im.applyFilter(new GrayLevelFilter());
            im.applyFilter(new GaussianContourExtractorFilter());

            im.writeOutPngImage("./TEST_IMAGES/FourCircles_gaussian_contour_TEST.png");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void ex1_FromGray() {
        try {
            IImageFilteringEngine im = new SingleThreadedImageFilteringEngine();
            im.loadImage("./TEST_IMAGES/15226222451_5fd668d81a_c_gray.png");

            im.applyFilter(new GaussianContourExtractorFilter());
            im.writeOutPngImage("./TEST_IMAGES/15226222451_5fd668d81a_c_gaussian_contour.png");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void ExampleGive() throws IOException {
        // reading image in
        BufferedImage inImg = ImageIO.read(new File("TEST_IMAGES/15226222451_5fd668d81a_c.jpg"));
        // creating new image
        BufferedImage outImg = new BufferedImage(inImg.getWidth(),
                inImg.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        // generating new image from original
        for (int x = 0; x < inImg.getWidth(); x++) {
            for (int y = 0; y < inImg.getHeight(); y++) {
                int rgb = inImg.getRGB(x, y);
                // extracting red, green and blue components from rgb integer
                int red = (rgb >> 16) & 0x000000FF;
                int green = (rgb >> 8) & 0x000000FF;
                int blue = (rgb) & 0x000000FF;
                // computing new color from extracted components
                int newRgb = (((green << 8) | blue) << 8) | red; // rotating RGB values
                outImg.setRGB(x, y, newRgb);
            } // EndFor y
        } // EndFor x

        // writing out new image
        File f = new File("TEST_IMAGES/tmp.png");
        ImageIO.write(outImg, "png", f);
    }


} // EndClass SimpleImageProcessingExample
