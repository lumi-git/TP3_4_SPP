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
import java.util.Collections;
import java.util.Comparator;

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
        int numRuns = 25; // Number of runs for each experiment
        IFilter[] filters = {new GrayLevelFilter(), new GaussianContourExtractorFilter()};

        ArrayList<ArrayList<Double>> originalSeqTimes1 = new ArrayList<>();
        ArrayList<ArrayList<Double>> multiThreadTimes1 = new ArrayList<>();

        // Initialize lists of lists
        for (int i = 0; i <= n; i++) {
            originalSeqTimes1.add(new ArrayList<>());
            multiThreadTimes1.add(new ArrayList<>());
        }

        for (int run = 0; run < numRuns; run++) {
            double singleThreadedTime1 = TimePerformFiltersOnImageWithName("./TEST_IMAGES/15226222451_75d515f540_o.jpg", new SingleThreadedImageFilteringEngine(), filters[0]);

            for (int i = 1; i <= n; i++) {
                double multiThreadTime = TimePerformFiltersOnImageWithName("./TEST_IMAGES/15226222451_75d515f540_o.jpg", new MultiThreadedImageFilteringEngine(i), filters[0]);
                originalSeqTimes1.get(i).add(singleThreadedTime1);
                multiThreadTimes1.get(i).add(multiThreadTime);
                System.out.println("GrayFilter Done with " + i + " threads (run " + (run + 1) + "/" + numRuns + ")");
            }
        }

        ArrayList<ArrayList<Double>> originalSeqTimes2 = new ArrayList<>();
        ArrayList<ArrayList<Double>> multiThreadTimes2 = new ArrayList<>();

        // Initialize lists of lists
        for (int i = 0; i <= n; i++) {
            originalSeqTimes2.add(new ArrayList<>());
            multiThreadTimes2.add(new ArrayList<>());
        }

        for (int run = 0; run < numRuns; run++) {
            double singleThreadedTime2 = TimePerformFiltersOnImageWithName("./TEST_IMAGES/15226222451_75d515f540_o.jpg", new SingleThreadedImageFilteringEngine(), filters[1]);

            for (int i = 1; i <= n; i++) {
                double multiThreadTime = TimePerformFiltersOnImageWithName("./TEST_IMAGES/15226222451_75d515f540_o.jpg", new MultiThreadedImageFilteringEngine(i), filters[1]);
                originalSeqTimes2.get(i).add(singleThreadedTime2);
                multiThreadTimes2.get(i).add(multiThreadTime);
                System.out.println("GaussianFilter Done with " + i + " threads (run " + (run + 1) + "/" + numRuns + ")");
            }
        }


        System.out.println("Times multi : " + multiThreadTimes1);
        System.out.println("Times single : " + originalSeqTimes1);
        System.out.println("Times multi : " + multiThreadTimes2);
        System.out.println("Times single : " + originalSeqTimes2);


        Utils.plotGraphCat("GrayLevelFilter : Performance", originalSeqTimes1, multiThreadTimes1, SETTINGS.DEBUG);
        Utils.plotGraphCat("GaussianContourExtractorFilter : Performance", originalSeqTimes2, multiThreadTimes2, SETTINGS.DEBUG);
    }


    public static void Graphs2() {
        int n = 4;
        int numRuns = 25; // Number of runs for each experiment
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

        // Sort image files by size
        Collections.sort(imageFiles, new Comparator<String>() {
            public int compare(String file1, String file2) {
                BufferedImage img1 = null, img2 = null;
                try {
                    img1 = ImageIO.read(new File(file1));
                    img2 = ImageIO.read(new File(file2));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int size1 = img1.getWidth() * img1.getHeight();
                int size2 = img2.getWidth() * img2.getHeight();

                return Integer.compare(size1, size2);
            }
        });


        ArrayList<ArrayList<Double>> originalSeqTimes1 = new ArrayList<>();
        ArrayList<ArrayList<Double>> multiThreadTimes1 = new ArrayList<>();

        for (int i = 0; i < imageFiles.size(); i++) {
            originalSeqTimes1.add(new ArrayList<>());
            multiThreadTimes1.add(new ArrayList<>());
        }

        for (int run = 0; run < numRuns; run++) {
            for (int i = 0; i < imageFiles.size(); i++) {
                String imageFile = imageFiles.get(i);
                double SingleTrheadedTime = TimePerformFiltersOnImageWithName(imageFile, new SingleThreadedImageFilteringEngine(), filters[0]);
                System.out.println("GrayFilter on " + imageFile + " Done No thread (run " + (run + 1) + "/" + numRuns + ")");
                double multiThreadTime = TimePerformFiltersOnImageWithName(imageFile, new MultiThreadedImageFilteringEngine(n), filters[0]);
                System.out.println("GrayFilter on " + imageFile + " Done with " + n + " threads (run " + (run + 1) + "/" + numRuns + ")");
                originalSeqTimes1.get(i).add(SingleTrheadedTime);
                multiThreadTimes1.get(i).add(multiThreadTime);
            }
        }

        ArrayList<ArrayList<Double>> originalSeqTimes2 = new ArrayList<>();
        ArrayList<ArrayList<Double>> multiThreadTimes2 = new ArrayList<>();

        for (int i = 0; i < imageFiles.size(); i++) {
            originalSeqTimes2.add(new ArrayList<>());
            multiThreadTimes2.add(new ArrayList<>());
        }

        for (int run = 0; run < numRuns; run++) {
            for (int i = 0; i < imageFiles.size(); i++) {
                String imageFile = imageFiles.get(i);
                double SingleTrheadedTime = TimePerformFiltersOnImageWithName(imageFile, new SingleThreadedImageFilteringEngine(), filters[1]);
                System.out.println("GaussianFilter on " + imageFile + " Done No thread (run " + (run + 1) + "/" + numRuns + ")");
                double multiThreadTime = TimePerformFiltersOnImageWithName(imageFile, new MultiThreadedImageFilteringEngine(n), filters[1]);
                System.out.println("GaussianFilter on " + imageFile + " Done with " + n + " threads (run " + (run + 1) + "/" + numRuns + ")");
                originalSeqTimes2.get(i).add(SingleTrheadedTime);
                multiThreadTimes2.get(i).add(multiThreadTime);
            }
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

        Utils.plotGraphWithImageSize("GrayLevelFilter : Performance", ImageSizes, originalSeqTimes1, multiThreadTimes1, SETTINGS.DEBUG);
        Utils.plotGraphWithImageSize("GaussianContourExtractorFilter : Performance", ImageSizes, originalSeqTimes2, multiThreadTimes2, SETTINGS.DEBUG);
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
