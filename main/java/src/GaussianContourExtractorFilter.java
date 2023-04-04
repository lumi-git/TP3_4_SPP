import java.awt.image.BufferedImage;

public class GaussianContourExtractorFilter implements IFilter {

    @Override
    public int getMargin() {
        return 5;
    }

    @Override
    public void applyFilterAtPoint(int x, int y, BufferedImage imgIn, BufferedImage imgOut) {
        double deltaX = 0.0;
        double deltaY = 0.0;

        for (int dx = -5; dx <= 5; dx++) {
            for (int dy = -5; dy <= 5; dy++) {
                int newX = x + dx;
                int newY = y + dy;

                if (newX >= 0 && newX < imgIn.getWidth() && newY >= 0 && newY < imgIn.getHeight()) {
                    int newRgb = imgIn.getRGB(newX, newY);
                    int newBlue = newRgb & 0x000000FF;

                    double gaussianWeight = Math.exp(-0.25 * (dx * dx + dy * dy));

                    deltaX += Math.signum(dx) * newBlue * gaussianWeight;
                    deltaY += Math.signum(dy) * newBlue * gaussianWeight;
                }
            }
        }

        double gradientNorm = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        int newBlue = (int) Math.max(0, 255 - 0.5 * gradientNorm);

        int newRgb = (newBlue << 16) | (newBlue << 8) | newBlue;
        imgOut.setRGB(x, y, newRgb);
    }
}