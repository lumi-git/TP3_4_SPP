/**
 * SimpleImageProcessingExample.java
 * 
 * Time-stamp: <2017-03-09 22:26:51 ftaiani>
 * @author Francois Taiani   <francois.taiani@irisa.fr>
 * $Id$
 * 
 */

import javax.imageio.ImageIO;
import java.awt.image.*;

import java.io.File;
import java.io.IOException;

/**
 * @author Francois Taiani   <francois.taiani@irisa.fr>
 */
public class SimpleImageProcessingExample {

  static public void main(String[] args) throws Exception {
      ex1();
  } // EndMain

    public static void  ex1(){
        try{

            IImageFilteringEngine im = new SingleThreadedImageFilteringEngine();
            im.loadImage("./TEST_IMAGES/15226222451_5fd668d81a_c.jpg");
            im.applyFilter(new GrayLevelFilter());
            im.applyFilter(new GaussianContourExtractorFilter());

            im.writeOutPngImage("./TEST_IMAGES/15226222451_5fd668d81a_c_gaussian_contour.png");

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void  ex1OnCircles() {
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
        try{
            IImageFilteringEngine im = new SingleThreadedImageFilteringEngine();
            im.loadImage("./TEST_IMAGES/15226222451_5fd668d81a_c_gray.png");


            im.applyFilter(new GaussianContourExtractorFilter());
            im.writeOutPngImage("./TEST_IMAGES/15226222451_5fd668d81a_c_gaussian_contour.png");
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }



    public static void ExampleGive() throws IOException {
        // reading image in
        BufferedImage inImg  = ImageIO.read(new File("TEST_IMAGES/15226222451_5fd668d81a_c.jpg"));
        // creating new image
        BufferedImage outImg = new BufferedImage(inImg.getWidth(),
                inImg.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        // generating new image from original
        for (int x = 0; x < inImg.getWidth(); x++) {
            for (int y = 0; y < inImg.getHeight(); y++) {
                int rgb    = inImg.getRGB(x,y);
                // extracting red, green and blue components from rgb integer
                int red    = (rgb >> 16) & 0x000000FF;
                int green  = (rgb >>  8) & 0x000000FF;
                int blue   = (rgb      ) & 0x000000FF;
                // computing new color from extracted components
                int newRgb = ( ( (green << 8) | blue ) << 8 ) | red ; // rotating RGB values
                outImg.setRGB(x,y,newRgb);
            } // EndFor y
        } // EndFor x

        // writing out new image
        File f = new File("TEST_IMAGES/tmp.png");
        ImageIO.write(outImg, "png", f);
    }



} // EndClass SimpleImageProcessingExample
