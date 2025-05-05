package uv.poo.Filter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Filtro que convierte una imagen a blanco y negro (escala de grises).
 */
public class BlackAndWhiteFilter implements ImageFilter {

    @Override
    public String name() {
        return "bw";
    }

    @Override
    public BufferedImage apply(BufferedImage src) {
        BufferedImage gray = new BufferedImage(
                src.getWidth(),
                src.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY
        );
        Graphics g = gray.getGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return gray;
    }
}
