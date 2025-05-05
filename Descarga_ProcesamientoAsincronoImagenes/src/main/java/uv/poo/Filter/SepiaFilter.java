package uv.poo.Filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Filtro que aplica un tono sepia a una imagen.
 */
public class SepiaFilter implements ImageFilter {

    @Override
    public String name() {
        return "sepia";
    }

    @Override
    public BufferedImage apply(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage sepia = new BufferedImage(width, height, src.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color c = new Color(src.getRGB(x, y));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();

                int tr = clamp((int) (0.393 * r + 0.769 * g + 0.189 * b));
                int tg = clamp((int) (0.349 * r + 0.686 * g + 0.168 * b));
                int tb = clamp((int) (0.272 * r + 0.534 * g + 0.131 * b));

                sepia.setRGB(x, y, new Color(tr, tg, tb).getRGB());
            }
        }
        return sepia;
    }

    /**
     * Asegura que el valor quede entre 0 y 255.
     */
    private int clamp(int val) {
        return Math.min(255, Math.max(0, val));
    }
}
