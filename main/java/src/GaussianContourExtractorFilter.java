import java.awt.Color;
import java.awt.image.BufferedImage;

public class GaussianContourExtractorFilter implements IFilter {

    @Override
    public int getMargin() {
        return 5;
    }

    @Override
    public void applyFilterAtPoint(int x, int y, BufferedImage imgIn, BufferedImage imgOut) {



        double gradX = 0;
        double gradY = 0;
        for (int dx=-5; dx < 6;dx++){
            for (int dy=-5; dy < 6; dy++){
                    double exp = Math.exp((Math.pow(dx, 2)+Math.pow(dy,2))/-4);
                    int blue = imgIn.getRGB(x+dx,y+dy) & 0x000000FF;
                    gradX += Math.signum(dx)*blue*exp;
                    gradY += Math.signum(dy)*blue*exp;
            }
        }
        //System.out.println("gradX : "+gradX+" gradY : "+gradY);
        double grad = Math.sqrt(Math.pow(gradX,2)+Math.pow(gradY,2));
        //System.out.println("grad value : " + grad);
        int blue = Math.max(0,255 - (int)grad/2);
        int newRgb = ( ( (blue << 8) | blue ) << 8 ) | blue ;
        imgOut.setRGB(x-getMargin(),y-getMargin(),newRgb);

    }
}