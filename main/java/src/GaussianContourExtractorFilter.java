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
    for (int dy = -5; dy < 6; dy++) {
      for (int dx = -5; dx < 6; dx++) {
        double exp = Math.exp((Math.pow(dx, 2) + Math.pow(dy, 2)) / -4);
        int blue = imgIn.getRGB(x + dx, y + dy) & 0x000000FF;
        gradX += (sign(dx) * blue * exp);
        gradY += (sign(dy) * blue * exp);
      }
    }
    //System.out.println("gradX : "+gradX+" gradY : "+gradY);
    double norm = Math.sqrt(Math.pow(gradX, 2) + Math.pow(gradY, 2));
    //System.out.println("grad value : " + grad);
    int blue = (int) Math.round(Math.max(0, 255 - norm / 2) );
    int shade = blue & 0x000000FF;
    int newRgb = shade << 16 | shade << 8 | shade;
    imgOut.setRGB(x - getMargin(), y - getMargin(), newRgb);

  }

  public int sign(int value ){
    if ( value < 0 )
      return -1;
    else if ( value > 0 )
        return 1;
    else
      return 0;
  }

}