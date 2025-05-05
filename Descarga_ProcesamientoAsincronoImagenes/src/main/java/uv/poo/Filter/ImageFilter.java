package uv.poo.Filter;

import java.awt.image.BufferedImage;

public interface ImageFilter {
    BufferedImage apply(BufferedImage src);
    String name();  // ej. "sepia", "bw", "sharpen"
}
