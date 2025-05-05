package uv.poo.Filter;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Filtro que aplica un efecto de 'sharpen' (enfoque) a una imagen.
 */
public class SharpenFilter implements ImageFilter {

    @Override
    public String name() {
        return "sharpen";
    }

    @Override
    public BufferedImage apply(BufferedImage src) {
        // Matríz de convolución para realzar bordes
        float[] kernelData = {
                0f, -1f, 0f,
                -1f,  5f, -1f,
                0f, -1f, 0f
        };
        Kernel kernel = new Kernel(3, 3, kernelData);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(src, null);
    }
}