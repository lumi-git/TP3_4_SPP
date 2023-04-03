import java.awt.image.BufferedImage;

public class GaussianContourExtractorFilter implements IFilter {

    private double[][] kernel = {{-1,-1,-1},{-1,8,-1},{-1,-1,-1}}; // 3x3 kernel for Laplacian of Gaussian

    @Override
    public int getMargin() {
        return 5;
    }

    @Override
    public void applyFilterAtPoint(int x, int y, BufferedImage imgIn, BufferedImage imgOut) {
        int rgb    = imgIn.getRGB(x,y);
        // extracting red, green and blue components from rgb integer
        int red    = (rgb >> 16) & 0x000000FF;
        int green  = (rgb >>  8) & 0x000000FF;
        int blue   = (rgb      ) & 0x000000FF;
        // computing new color from extracted components

        double sum = 0;
        double rSum = 0;
        double gSum = 0;
        double bSum = 0;
        int kernelSize = kernel.length;

        // iterate over the kernel
        for(int i = 0; i < kernelSize; i++) {
            for(int j = 0; j < kernelSize; j++) {
                int xIndex = x - kernelSize/2 + j;
                int yIndex = y - kernelSize/2 + i;
                // check if pixel is inside the image
                if(xIndex >= 0 && xIndex < imgIn.getWidth() && yIndex >= 0 && yIndex < imgIn.getHeight()) {
                    int pixel = imgIn.getRGB(xIndex, yIndex);
                    int redValue = (pixel >> 16) & 0xff;
                    int greenValue = (pixel >> 8) & 0xff;
                    int blueValue = pixel & 0xff;
                    double kernelValue = kernel[i][j];
                    sum += kernelValue;
                    rSum += redValue * kernelValue;
                    gSum += greenValue * kernelValue;
                    bSum += blueValue * kernelValue;
                }
            }
        }

        int newRed = (int) (rSum / sum);
        int newGreen = (int) (gSum / sum);
        int newBlue = (int) (bSum / sum);
        int newRgb = (newRed << 16) | (newGreen << 8) | newBlue;

        imgOut.setRGB(x, y, newRgb);
    }
}